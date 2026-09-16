---
title: 'Story 1.3: User Profile Management & Avatar Upload via MinIO'
type: 'feature'
created: '2026-09-14'
status: 'done'
baseline_commit: '67d06b65ce1dbf2c7d5f8ebde4ffc62cffbcf9e3'
route: 'dispatch'
review_loop_iteration: 0
context:
  - _bmad-output/planning-artifacts/architecture/architecture-News-2026-09-14/ARCHITECTURE-SPINE.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/DESIGN.md
  - _bmad-output/planning-artifacts/epics.md
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** Người dùng sau khi đăng nhập chưa có trang quản lý thông tin cá nhân, không thể cập nhật tên hiển thị, tiểu sử (bio), và chưa thể tải lên ảnh đại diện cá nhân (avatar) lưu trữ trên MinIO Object Storage; menu người dùng ở header vẫn đang hiển thị thông tin và ảnh mockup tĩnh chưa liên kết dữ liệu thật.

**Approach:** 
- Backend: Bổ sung trường `bio` vào model `User`. Cung cấp các API `/api/v1/user/profile` (GET, PUT) và `/api/v1/user/avatar` (POST upload avatar qua MinIO port 8887). Kiểm tra dung lượng (≤ 2MB) và định dạng MIME (JPG, PNG, WebP) nhị phân, chuẩn hóa thông báo qua `ErrorCode` Enum.
- Frontend: Xây dựng trang `/settings/profile` cho phép người dùng chỉnh sửa họ tên, số điện thoại, tiểu sử (bio tối đa 500 ký tự) và upload ảnh đại diện có preview trực quan. Đồng bộ thông tin thật của người dùng vào `DropdownMenu.tsx` và `AuthContext`.

## Boundaries & Constraints

**Always:**
- Kiểm tra tính xác thực người dùng (phải có JWT token hợp lệ) khi truy cập hoặc chỉnh sửa profile và upload avatar.
- Avatar chỉ chấp nhận định dạng ảnh hợp lệ: `image/jpeg`, `image/png`, `image/webp` với dung lượng tối đa 2MB (2,097,152 bytes).
- Upload avatar sử dụng `MinioService` sẵn có vào bucket cấu hình (`news`), lưu URL truy cập ảnh đại diện vào trường `avatarUrl` của document `User`.
- Giới hạn độ dài tiểu sử (`bio`): tối đa 500 ký tự.
- Chuẩn hóa mã lỗi liên quan đến file và bio thông qua `ErrorCode` Enum (`FILE_TOO_LARGE`, `FILE_INVALID_TYPE`, `BIO_TOO_LONG`).
- Trang `/settings/profile` có AuthGuard (chuyển hướng sang `/auth/login` nếu người dùng chưa đăng nhập).

**Never:**
- Không cho phép người dùng chưa đăng nhập tải lên avatar hoặc sửa thông tin cá nhân.
- Không cho phép cập nhật đè email hoặc username của người khác thông qua API cập nhật profile.
- Không chấp nhận file vượt quá 2MB hoặc định dạng file không an toàn (như exe, sh, html, svg có script).
- Không làm gián đoạn hiển thị nếu MinIO tạm thời không khả dụng; cung cấp avatar mặc định fallback ở frontend.

## I/O & Edge-Case Matrix

| Scenario | Input / State | Expected Output / Behavior | Error Handling |
|----------|--------------|---------------------------|----------------|
| Lấy thông tin cá nhân | JWT hợp lệ của người dùng đang đăng nhập | HTTP 200 OK, trả về DTO profile (`id`, `username`, `email`, `fullName`, `phone`, `bio`, `avatarUrl`, `role`) | Trả về 401 nếu chưa đăng nhập |
| Cập nhật profile thành công | Họ tên hợp lệ (2-100 ký tự), bio ≤ 500 ký tự | HTTP 200 OK, lưu vào DB, trả về thông tin đã cập nhật | N/A |
| Cập nhật bio vượt quá 500 ký tự | `bio` có độ dài 501 ký tự trở lên | HTTP 400 Bad Request, báo `ErrorCode.BIO_TOO_LONG` | Hiển thị cảnh báo số ký tự tại frontend |
| Upload avatar hợp lệ | File PNG/JPG/WebP dung lượng ≤ 2MB | HTTP 200 OK, upload lên MinIO, cập nhật `avatarUrl` trong User, trả về URL avatar | N/A |
| Upload avatar quá dung lượng | File ảnh có dung lượng > 2MB | HTTP 400 Bad Request, báo `ErrorCode.FILE_TOO_LARGE` | Chặn ngay tại client trước khi gửi và validate tại server |
| Upload file sai định dạng | File PDF, TXT, MP4 hoặc ảnh SVG chứa mã độc | HTTP 400 Bad Request, báo `ErrorCode.FILE_INVALID_TYPE` | Từ chối lưu, không đẩy vào MinIO |
| Menu người dùng Header | Người dùng đã đăng nhập mở menu | Hiển thị avatar thật, họ tên thật, email thật và link đến `/settings/profile` | Fallback avatar mặc định nếu chưa có ảnh |

</frozen-after-approval>

## Code Map

- `backend/src/main/java/com/newsroom/model/User.java` -- Bổ sung trường `bio` (tiểu sử).
- `backend/src/main/java/com/newsroom/enums/ErrorCode.java` -- Bổ sung các mã lỗi `FILE_TOO_LARGE`, `FILE_INVALID_TYPE`, `BIO_TOO_LONG`.
- `backend/src/main/java/com/newsroom/dto/user/UserProfileResponse.java` -- DTO trả về thông tin profile người dùng.
- `backend/src/main/java/com/newsroom/dto/user/UpdateProfileRequest.java` -- DTO nhận yêu cầu cập nhật họ tên, số điện thoại, bio.
- `backend/src/main/java/com/newsroom/service/IUserService.java` & `UserServiceImplement.java` -- Bổ sung các phương thức: `getProfile()`, `updateProfile()`, `updateAvatar()`.
- `backend/src/main/java/com/newsroom/controller/UserController.java` -- Controller REST API cho profile và avatar (`/api/v1/user`).
- `backend/src/test/java/com/newsroom/service/user/UserServiceProfileTest.java` -- Unit test kiểm thử lấy thông tin, cập nhật thông tin, upload avatar và xử lý lỗi dung lượng/định dạng.
- `frontend/src/app/settings/profile/page.tsx` -- Giao diện trang cài đặt thông tin cá nhân và upload avatar.
- `frontend/src/components/DropdownMenu.tsx` -- Đồng bộ hiển thị avatar, tên, email thật và liên kết đến `/settings/profile`.
- `frontend/src/types/backend.ts` -- Bổ sung `bio` và `avatarUrl` vào interface `UserLogin` / `UserProfile`.
- `frontend/src/lib/userApi.ts` -- Client API gọi các endpoint profile và upload avatar.

## Tasks & Acceptance

**Execution:**
- [x] `backend/src/main/java/com/newsroom/model/User.java` -- Bổ sung trường `private String bio;` vào document User.
- [x] `backend/src/main/java/com/newsroom/enums/ErrorCode.java` -- Bổ sung mã lỗi `FILE_TOO_LARGE(1201)`, `FILE_INVALID_TYPE(1202)`, `BIO_TOO_LONG(1203)`.
- [x] `backend/src/main/java/com/newsroom/dto/user/` -- Tạo `UserProfileResponse` và `UpdateProfileRequest`.
- [x] `backend/src/main/java/com/newsroom/service/IUserService.java` & `UserServiceImplement.java` -- Hiện thực hóa `getCurrentUserProfile`, `updateCurrentUserProfile`, `uploadAvatar`.
- [x] `backend/src/main/java/com/newsroom/controller/UserController.java` -- Xây dựng controller `/api/v1/user` với các endpoint: `GET /profile`, `PUT /profile`, `POST /avatar`.
- [x] `backend/src/test/java/com/newsroom/service/user/UserServiceProfileTest.java` -- Viết bộ unit test kiểm thử các ca thành công và biên (bio quá dài, file quá lớn, sai định dạng).
- [x] `frontend/src/lib/userApi.ts` & `frontend/src/types/backend.ts` -- Khai báo kiểu dữ liệu và API service profile.
- [x] `frontend/src/app/settings/profile/page.tsx` -- Xây dựng trang cài đặt tài khoản: form chỉnh sửa họ tên, bio (bộ đếm ký tự), phone, và component upload avatar kèm preview.
- [x] `frontend/src/components/DropdownMenu.tsx` -- Cập nhật dữ liệu thật từ `user`, link Settings trỏ về `/settings/profile`.

**Acceptance Criteria:**
- Given người dùng đã đăng nhập, when truy cập `/settings/profile`, then các trường họ tên, email (disabled), username (disabled), phone, bio và ảnh đại diện hiện tại được hiển thị đầy đủ.
- Given người dùng chọn file ảnh avatar hợp lệ (JPG, PNG, WebP ≤ 2MB), when bấm tải lên, then file được lưu vào MinIO, URL avatar được cập nhật vào DB, ảnh preview thay đổi tức thì và hiển thị thông báo thành công.
- Given người dùng chọn file > 2MB hoặc không đúng định dạng ảnh, when tải lên, then hệ thống từ chối và hiển thị thông báo lỗi rõ ràng từ `ErrorCode`.
- Given người dùng sửa họ tên và bio (≤ 500 ký tự), when bấm "Lưu thay đổi", then thông tin được cập nhật vào MongoDB và hiển thị thông báo thành công.
- Given người dùng mở Dropdown Menu ở Header, then avatar, tên và email của chính người dùng được hiển thị chính xác thay vì dữ liệu mock.

### Review Findings
- [x] [Review][Patch] Fix potential StringIndexOutOfBoundsException & NPE in MinioServiceImplement.formatFileName [backend/src/main/java/com/newsroom/service/implement/MinioServiceImplement.java:272]
- [x] [Review][Patch] Sanitize and validate fullName to prevent whitespace-only blank names, add bounds for phone [backend/src/main/java/com/newsroom/dto/user/UpdateProfileRequest.java:14]
- [x] [Review][Patch] Synchronize updated user profile and avatarUrl into localStorage on client state update [frontend/src/app/settings/profile/page.tsx:121]

## Implementation Notes
- Đã hoàn thành toàn bộ mã nguồn backend và frontend cho Story 1.3.
- Đã khắc phục 2 lỗi login & refresh cookie (lỗi mật khẩu hash, lỗi ResponseCookie secure cản trở HTTP localhost, sửa defaultValue và exception của refresh token, sửa lỗi logout deactivate user, sửa findFirstByPhone chống crash trùng số điện thoại).
- Đã chạy unit test `UserServiceProfileTest` (7/7 tests passed).
- Đã chạy build `npm run build` frontend Next.js thành công 100%.

## Spec Change Log

## Review Triage Log

## Verification

**Commands:**
- `$env:JAVA_HOME="C:\Users\nguye\.jdks\corretto-21.0.6"; & "C:\Users\nguye\.m2\wrapper\dists\apache-maven-3.9.11-bin\6mqf5t809d9geo83kj4ttckcbc\apache-maven-3.9.11\bin\mvn.cmd" -f backend/pom.xml test -Dtest=UserServiceProfileTest` -- expected: BUILD SUCCESS, tất cả các ca kiểm thử profile và upload avatar pass 100%.
- `npm --prefix frontend run build` -- expected: Build Next.js thành công sạch sẽ.

**Manual checks (if no CLI):**
- Đăng nhập vào hệ thống, mở DropdownMenu và bấm vào Settings để chuyển tới `/settings/profile`.
- Thử đổi họ tên, bio và upload ảnh avatar mới.
