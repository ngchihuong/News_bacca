---
title: 'Story 1.2: User Login, JWT Authentication & Rate Limiting'
type: 'feature'
created: '2026-09-14'
status: 'done'
baseline_commit: '799d83a9567156ea402227f9971fd786e47db671'
route: 'dispatch'
review_loop_iteration: 0
context:
  - _bmad-output/planning-artifacts/architecture/architecture-News-2026-09-14/ARCHITECTURE-SPINE.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/DESIGN.md
  - _bmad-output/planning-artifacts/epics.md
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** Người dùng hiện chỉ có thể đăng nhập bằng email hoặc username mà chưa thể dùng Số điện thoại (SĐT), thiếu cơ chế phòng vệ tự động khóa tài khoản khi bị tấn công brute-force nhập sai mật khẩu nhiều lần, và hệ thống thông báo lỗi đang dùng chuỗi hardcode rải rác chưa có chuẩn Enum dùng chung.

**Approach:** Cung cấp giải pháp đăng nhập linh hoạt hỗ trợ cả Email và Số điện thoại (Phone), kiểm tra trạng thái khóa tạm thời và quản lý số lần đăng nhập sai (khóa 15 phút nếu sai 5 lần liên tiếp); xây dựng hệ thống `ErrorCode` Enum chuẩn hóa toàn bộ mã lỗi và thông báo nghiệp vụ tái sử dụng đa nơi; đồng thời cấp JWT access token (24h) và refresh token (7 ngày).

## Boundaries & Constraints

**Always:**
- Cho phép tra cứu danh tính người dùng qua Email (chuẩn hóa không phân biệt hoa thường) hoặc Số điện thoại (Phone) hoặc Username.
- Xây dựng hệ thống `ErrorCode` Enum tại `com.newsroom.enums.ErrorCode` quản lý tập trung toàn bộ mã lỗi (code số, default message tiếng Việt, HttpStatus tương ứng) và `AppException` để tái sử dụng xuyên suốt toàn bộ backend.
- Quản lý số lần đăng nhập thất bại liên tiếp:
  - Nhập sai mật khẩu: tăng bộ đếm `failedLoginAttempts`. Nếu < 5 lần, báo lỗi và thông báo số lần thử còn lại.
  - Nhập sai liên tiếp 5 lần: tự động gán `lockoutUntil = Instant.now() + 15 phút`, khóa tạm thời tài khoản và trả về `ErrorCode.ACCOUNT_LOCKED`.
  - Nếu tài khoản đang bị khóa và chưa hết 15 phút: từ chối đăng nhập và trả về thời gian còn lại trước khi được thử lại.
  - Khi đăng nhập thành công: reset `failedLoginAttempts = 0` và `lockoutUntil = null`.
- Cấp JWT access token (thời hạn 24h) và refresh token (7 ngày) khi thông tin đăng nhập chính xác.
- Frontend trang `/auth/login` hiển thị nhãn input là "Email hoặc Số điện thoại", hỗ trợ điền sẵn email từ bước đăng ký, và tích hợp GuestGuard (chuyển hướng về `/` nếu đã có phiên đăng nhập).

**Never:**
- Không hardcode các chuỗi thông báo lỗi rải rác trong Controller và Service; phải sử dụng `ErrorCode` Enum chuẩn.
- Không cho phép đăng nhập thành công khi tài khoản đang trong thời gian bị khóa lockout 15 phút.
- Không để lộ mật khẩu đã hash trong response của API đăng nhập.

## I/O & Edge-Case Matrix

| Scenario | Input / State | Expected Output / Behavior | Error Handling |
|----------|--------------|---------------------------|----------------|
| Đăng nhập bằng Email thành công | Email hợp lệ đã đăng ký + đúng mật khẩu | HTTP 200 OK, reset attempts về 0, trả về `JwtResponse` (access_token 24h, user info) | N/A |
| Đăng nhập bằng Số điện thoại thành công | Số điện thoại đã đăng ký trong User + đúng mật khẩu | HTTP 200 OK, reset attempts về 0, trả về `JwtResponse` | N/A |
| Nhập sai mật khẩu lần 1 đến 4 | Email/SĐT đúng nhưng mật khẩu sai | HTTP 401 Unauthorized, tăng attempts, báo `ErrorCode.INVALID_CREDENTIALS` kèm số lần còn lại | Thông báo số lần thử còn lại trước khi bị khóa |
| Nhập sai mật khẩu lần 5 liên tiếp | Email/SĐT đúng nhưng nhập sai lần thứ 5 | HTTP 403 Forbidden, gán lockout 15 phút, báo `ErrorCode.ACCOUNT_LOCKED` | Khóa tài khoản 15 phút |
| Cố đăng nhập khi đang bị khóa | Tài khoản có `lockoutUntil` còn hiệu lực | HTTP 403 Forbidden, báo `ErrorCode.ACCOUNT_LOCKED` kèm số phút còn lại | Chặn không cho thử mật khẩu |
| Hết thời gian khóa 15 phút | Đã qua thời điểm `lockoutUntil` | Cho phép đăng nhập lại, nếu đúng mật khẩu thì mở khóa và đăng nhập thành công | Reset attempts về 0 |
| Tài khoản không tồn tại | Nhập email/SĐT không có trong DB | HTTP 401 Unauthorized, báo `ErrorCode.INVALID_CREDENTIALS` | Không tiết lộ tài khoản có tồn tại hay không |

</frozen-after-approval>

## Code Map

- `backend/src/main/java/com/newsroom/enums/ErrorCode.java` -- Enum tập trung quản lý toàn bộ mã lỗi, mã số code, HttpStatus và thông điệp chuẩn tái sử dụng cho toàn hệ thống.
- `backend/src/main/java/com/newsroom/config/exceptions/AppException.java` -- Custom RuntimeException nhận `ErrorCode` và custom message tùy biến.
- `backend/src/main/java/com/newsroom/config/exceptions/GlobalExceptionHandler.java` -- Handler bắt `AppException` trả về cấu trúc `BaseOutput` có `status = FAILED`, mã lỗi và thông điệp chuẩn.
- `backend/src/main/java/com/newsroom/model/User.java` -- Bổ sung 2 trường `failedLoginAttempts` và `lockoutUntil` để theo dõi rate limiting chống brute-force.
- `backend/src/main/java/com/newsroom/repository/UserRepository.java` -- Bổ sung query method `User findByPhone(String phone)`.
- `backend/src/main/java/com/newsroom/service/auth/AuthServiceImpl.java` -- Logic đăng nhập: tìm User qua Email hoặc SĐT hoặc Username, kiểm tra lockout 15 phút, đếm số lần sai và cấp JWT token.
- `backend/src/main/java/com/newsroom/security/CustomUserDetailsService.java` -- Tích hợp tìm User qua Email/SĐT/Username và kiểm tra trạng thái khóa tài khoản.
- `frontend/src/app/auth/login/page.tsx` -- Cập nhật placeholder và label hỗ trợ Email hoặc Số điện thoại, hiển thị thông báo lỗi thân thiện.

## Tasks & Acceptance

**Execution:**
- [x] `backend/src/main/java/com/newsroom/enums/ErrorCode.java` -- Tạo Enum `ErrorCode` tập trung cho toàn bộ hệ thống với mã lỗi và thông điệp chuẩn tiếng Việt.
- [x] `backend/src/main/java/com/newsroom/config/exceptions/AppException.java` -- Tạo `AppException` kế thừa NewsCommonException đóng gói `ErrorCode`.
- [x] `backend/src/main/java/com/newsroom/config/exceptions/GlobalExceptionHandler.java` -- Cấu hình `@ExceptionHandler(AppException.class)` map `ErrorCode` sang HTTP status và body tương ứng.
- [x] `backend/src/main/java/com/newsroom/model/User.java` -- Thêm `failedLoginAttempts` (int) và `lockoutUntil` (Instant).
- [x] `backend/src/main/java/com/newsroom/repository/UserRepository.java` -- Khai báo `User findByPhone(String phone)` và `Boolean existsByPhone(String phone)`.
- [x] `backend/src/main/java/com/newsroom/service/auth/AuthServiceImpl.java` -- Hiện thực hóa kiểm tra đăng nhập qua Email / Số điện thoại, kiểm tra lockout, đếm số lần sai và cấp JWT.
- [x] `backend/src/main/java/com/newsroom/security/CustomUserDetailsService.java` -- Đồng bộ tìm kiếm User qua Email hoặc Số điện thoại.
- [x] `backend/src/test/java/com/newsroom/service/auth/AuthServiceLoginTest.java` -- Viết bộ unit test kiểm thử đăng nhập bằng Email, bằng SĐT, sai mật khẩu tăng attempts và khóa 15 phút (7 tests pass 100%).
- [x] `frontend/src/app/auth/login/page.tsx` -- Điều chỉnh label và placeholder: "Email hoặc Số điện thoại".

**Acceptance Criteria:**
- Given người dùng nhập đúng email hoặc số điện thoại và mật khẩu hợp lệ, when bấm "Đăng nhập", then hệ thống trả về JWT token (access token 24h, refresh token 7 ngày) và frontend lưu token chuyển hướng đến trang chủ.
- Given người dùng nhập sai mật khẩu, when gửi request đăng nhập, then hệ thống trả về HTTP 401 kèm thông báo từ `ErrorCode.INVALID_CREDENTIALS` và số lần thử còn lại.
- Given người dùng nhập sai mật khẩu 5 lần liên tiếp, when gửi lần thứ 5, then hệ thống tự động khóa tài khoản 15 phút, trả về HTTP 403 với `ErrorCode.ACCOUNT_LOCKED`.
- Given tài khoản đang bị khóa lockout 15 phút, when cố tình đăng nhập lại, then hệ thống từ chối với HTTP 403 và thông báo thời gian còn lại trước khi mở khóa.
- Given toàn bộ các lỗi nghiệp vụ trong hệ thống, when phát sinh, then được quản lý tập trung thông qua `ErrorCode` Enum thay vì hardcode chuỗi string.

## Implementation Notes
- Xây dựng `com.newsroom.enums.ErrorCode` bao quát các mã lỗi hệ thống: xác thực (1001-1008), phân quyền (1101-1105), dữ liệu/người dùng (2001-2003) và lỗi máy chủ (9999).
- `AppException` mở rộng `NewsCommonException` đảm bảo tương thích ngược 100% với các exception handler và test suite cũ.
- Tự động khóa 15 phút dựa trên `Instant lockoutUntil` và reset sau khi qua thời gian khóa hoặc đăng nhập thành công.
- Login hỗ trợ thông minh 3 hình thức định danh: Email (chuẩn hóa chữ thường), Số điện thoại (Phone) và Username.
- Frontend `/auth/login` được cập nhật label/placeholder thân thiện "Email hoặc Số điện thoại", đồng bộ trạng thái đăng nhập qua `GuestGuard`.

## Spec Change Log
- 2026-09-14: Bổ sung yêu cầu người dùng: Đăng nhập bằng Email/SĐT và chuẩn hóa mã lỗi thành Enum tập trung `ErrorCode`.

## Review Triage Log
- Triage: Đã kiểm thử tự động với 7 test cases trong `AuthServiceLoginTest` và 4 test cases trong `AuthServiceRegisterTest` (tổng 11 tests pass 100%).
- Frontend Next.js build hoàn thành sạch sẽ, không có lỗi runtime/compile.

## Verification

**Commands:**
- `$env:JAVA_HOME="C:\Users\nguye\.jdks\corretto-21.0.6"; & "C:\Users\nguye\.m2\wrapper\dists\apache-maven-3.9.11-bin\6mqf5t809d9geo83kj4ttckcbc\apache-maven-3.9.11\bin\mvn.cmd" -f backend/pom.xml test -Dtest=AuthServiceLoginTest` -- expected: BUILD SUCCESS, tất cả các ca kiểm thử đăng nhập Email/SĐT và lockout đều pass.
- `npm --prefix frontend run build` -- expected: Build Next.js thành công.

**Manual checks (if no CLI):**
- Đăng nhập bằng email hoặc số điện thoại trên giao diện `/auth/login`.
- Thử nhập sai mật khẩu 5 lần liên tiếp để xác nhận thông báo tài khoản bị tạm khóa 15 phút.
