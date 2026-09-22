---
title: 'Story 1.1: User Registration & Account Creation'
type: 'feature'
created: '2026-09-14'
status: 'done'
baseline_commit: 'b7dc049c979b13cb2386930725d12e2da1ab24b8'
route: 'dispatch'
review_loop_iteration: 0
context:
  - _bmad-output/planning-artifacts/architecture/architecture-News-2026-09-14/ARCHITECTURE-SPINE.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/DESIGN.md
  - _bmad-output/planning-artifacts/epics.md
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** Khách truy cập nền tảng NewsRoom chưa thể tự đăng ký tài khoản cá nhân để xuất bản tin bài và tương tác với cộng đồng.

**Approach:** Cung cấp API đăng ký chuẩn hóa với `RegisterRequest` DTO (validate email, mật khẩu tối thiểu 8 ký tự, tên hiển thị), mã hóa BCrypt, lưu MongoDB với role `ROLE_USER` và trạng thái kích hoạt, kiểm tra trùng email trả về HTTP 400; đồng thời xây dựng giao diện đăng ký `/register` hiện đại (Next.js 15, Ant Design, Tailwind CSS) với validation inline và thông báo thân thiện.

## Boundaries & Constraints

**Always:**
- Mật khẩu phải được mã hóa an toàn bằng `BCryptPasswordEncoder` trước khi lưu vào MongoDB.
- Kiểm tra tính duy nhất của email; nếu email đã tồn tại trong cơ sở dữ liệu, trả về HTTP 400 kèm thông điệp lỗi rõ ràng (`error.user.exist` hoặc "Email đã được sử dụng").
- Gán vai trò mặc định cho người dùng đăng ký mới là `ROLE_USER`, trạng thái `active = true`.
- Tự động sinh `username` từ phần tiền tố email (hoặc cho phép nhập `username` nếu người dùng điền) đảm bảo duy nhất.
- API endpoint `/api/v1/auth/register` phải cho phép truy cập công khai không cần token (`permitAll`).
- Dữ liệu trả về từ API đăng ký tuân thủ cấu trúc envelope chuẩn `BaseOutput<UserDTO>` và tuyệt đối không để lộ mật khẩu đã hash.
- Giao diện đăng ký tại `/register` tuân thủ bộ Design Tokens: Primary Orange `#FF6600`, Slate-50 `#F8FAFC`, phản hồi tức thì với Ant Design message/notification và client-side validation.
- **Quy tắc điều hướng trạng thái phiên (Session Navigation Guard):**
  - **Đã đăng nhập (`isAuthenticated === true`):** Tuyệt đối không cho phép truy cập lại các trang Guest-only gồm `/register` và `/auth/login`. Nếu cố tình vào, hệ thống tự động redirect về trang chủ `/` (hoặc `/admin` nếu là Admin) kèm thông báo Ant Design: "Bạn đã đăng nhập, vui lòng đăng xuất nếu muốn đăng ký tài khoản mới".
  - **Chưa đăng nhập (Khách/Guest):** Cho phép truy cập form đăng ký. Sau khi đăng ký thành công, hiển thị thông báo "Đăng ký thành công!" và tự động chuyển hướng sang `/auth/login?email=...` để người dùng đăng nhập ngay.

**Never:**
- Không lưu mật khẩu dưới dạng plain-text vào database trong bất kỳ tình huống nào.
- Không cho phép đăng ký tài khoản với quyền `ROLE_ADMIN` hoặc `ROLE_JOURNALIST` thông qua API đăng ký công cộng này.
- Không bỏ qua tầng xác thực dữ liệu tại Backend (Bean Validation `@Valid`).
- Không để người dùng đã có phiên đăng nhập hợp lệ ở lại trang `/register` hoặc `/auth/login`.

## I/O & Edge-Case Matrix

| Scenario | Input / State | Expected Output / Behavior | Error Handling |
|----------|--------------|---------------------------|----------------|
| Đăng ký thành công (Chưa đăng nhập) | Khách chưa đăng nhập, email hợp lệ chưa tồn tại, mật khẩu ≥ 8 ký tự, tên hiển thị đầy đủ | HTTP 200 OK, trả về `UserDTO` (id, username, email, fullName, role = ROLE_USER), frontend thông báo thành công và chuyển hướng đến `/auth/login` | N/A |
| Đã đăng nhập cố truy cập `/register` hoặc `/auth/login` | Người dùng đã có session (`isAuthenticated === true`) truy cập trực tiếp URL `/register` | Tự động chuyển hướng (Redirect) về trang chủ `/` (hoặc `/admin`), hiển thị message: "Bạn đã đăng nhập hệ thống" | Chặn render form, redirect ngay lập tức |
| Trùng lặp email | Email đã tồn tại trong MongoDB | HTTP 400 Bad Request, `BaseOutput` có `status = FAILED`, lỗi "Email đã được sử dụng" | Frontend hiển thị thông báo lỗi nổi bật |
| Mật khẩu quá ngắn (< 8 ký tự) | Mật khẩu chỉ có 6 hoặc 7 ký tự | Frontend báo lỗi inline tại ô mật khẩu; nếu gửi trực tiếp đến API, backend trả về HTTP 400 validation error | Không gửi request / Backend từ chối |
| Bỏ trống trường bắt buộc | Để trống email, tên hiển thị hoặc mật khẩu | Frontend chặn submit, viền đỏ ô input và hiển thị lỗi validation | Báo lỗi từng trường cụ thể |
| Email sai định dạng | Nhập chuỗi không đúng định dạng email (vd: "abc", "user@") | Frontend và Backend đều từ chối với lỗi "Email không hợp lệ" | Báo lỗi định dạng email |

</frozen-after-approval>

## Code Map

- `backend/src/main/java/com/newsroom/dto/RegisterRequest.java` -- DTO đăng ký: kiểm tra validation `@Email`, `@Size(min = 8)` cho mật khẩu, `@NotBlank` cho họ tên và username tùy chọn.
- `backend/src/main/java/com/newsroom/controller/auth/AuthController.java` -- Tiếp nhận request đăng ký `POST /api/v1/auth/register` với `@Valid @RequestBody RegisterRequest`.
- `backend/src/main/java/com/newsroom/service/auth/IAuthService.java` -- Khai báo phương thức `UserDTO register(RegisterRequest request)`.
- `backend/src/main/java/com/newsroom/service/auth/AuthServiceImpl.java` -- Nghiệp vụ: kiểm tra email trùng lặp, sinh username, mã hóa mật khẩu qua BCrypt, lưu `User` với role `ROLE_USER`, trả về `UserDTO`.
- `backend/src/main/java/com/newsroom/config/exceptions/GlobalExceptionHandler.java` -- Đảm bảo ngoại lệ `NewsCommonException` được map sang HTTP status 400 Bad Request.
- `frontend/src/lib/authApi.ts` -- Bổ sung hàm API client `register(data: RegisterData)` gọi endpoint backend.
- `frontend/src/app/register/page.tsx` -- Trang Đăng ký chính phong cách Modern Stream (Primary Orange `#FF6600`, Slate-50, React Hook Form, Ant Design).
- `frontend/src/app/auth/register/page.tsx` -- Route alias chuyển tiếp hoặc hiển thị trang đăng ký.
- `frontend/src/app/auth/login/page.tsx` -- Thêm liên kết "Chưa có tài khoản? Đăng ký ngay" dẫn đến `/register`.
- `frontend/src/components/DropdownMenu.tsx` -- Kích hoạt nút "Đăng ký" dẫn đến `/register`.

## Tasks & Acceptance

**Execution:**
- [x] `backend/src/main/java/com/newsroom/dto/RegisterRequest.java` -- Cập nhật validation cho `RegisterRequest` (mật khẩu tối thiểu 8 ký tự, email hợp lệ, họ tên bắt buộc).
- [x] `backend/src/main/java/com/newsroom/service/auth/IAuthService.java` & `backend/src/main/java/com/newsroom/service/auth/AuthServiceImpl.java` -- Cập nhật hàm `register` nhận `RegisterRequest`, mã hóa BCrypt, gán role `ROLE_USER`, `active = true`, kiểm tra trùng email và chuyển đổi sang `UserDTO`.
- [x] `backend/src/main/java/com/newsroom/controller/auth/AuthController.java` -- Cập nhật controller nhận `@Valid @RequestBody RegisterRequest request`.
- [x] `backend/src/main/java/com/newsroom/config/exceptions/GlobalExceptionHandler.java` -- Đảm bảo ngoại lệ nghiệp vụ trả về HTTP 400 Bad Request.
- [x] `frontend/src/lib/authApi.ts` -- Thêm hàm gọi API `register` cho frontend.
- [x] `frontend/src/app/register/page.tsx` -- Xây dựng giao diện đăng ký với React Hook Form, Ant Design App notification, inline validation, và GuestGuard (tự động redirect về `/` nếu đã đăng nhập).
- [x] `frontend/src/app/auth/register/page.tsx` -- Cung cấp route alias dẫn đến trang đăng ký.
- [x] `frontend/src/app/auth/login/page.tsx` -- Bổ sung liên kết dẫn sang trang `/register` và GuestGuard (tự động redirect về `/` nếu đã đăng nhập).
- [x] `frontend/src/components/DropdownMenu.tsx` -- Mở khóa liên kết đăng ký tài khoản trên thanh điều hướng.

**Acceptance Criteria:**
- Given người dùng ở trang Đăng ký `/register`, when nhập email hợp lệ, mật khẩu ≥ 8 ký tự và tên hiển thị rồi bấm "Đăng ký", then hệ thống lưu User mới vào MongoDB với `role = ROLE_USER`, `active = true`, mật khẩu mã hóa BCrypt và trả về thông báo thành công.
- Given email đã tồn tại trong database, when người dùng gửi form đăng ký, then hệ thống trả về HTTP 400 và hiển thị thông báo lỗi "Email đã được sử dụng".
- Given người dùng nhập mật khẩu < 8 ký tự hoặc để trống trường bắt buộc, when tương tác trên form, then frontend hiển thị cảnh báo lỗi tức thì tại trường tương ứng và chặn gửi request.
- Given người dùng đã đăng nhập (`isAuthenticated === true`), when truy cập vào `/register` hoặc `/auth/login`, then hệ thống lập tức chuyển hướng (redirect) về trang chủ `/` (hoặc `/admin`), ngăn không cho truy cập lại màn hình đăng ký/đăng nhập.

## Implementation Notes

- **Backend:**
  - `RegisterRequest.java`: Cập nhật annotation `@Size(min = 8)` cho trường `password`, giữ `@Email` và `@NotBlank`. Cho phép `username` tùy chọn.
  - `AuthServiceImpl.java`: Tiếp nhận `RegisterRequest`, chuẩn hóa email sang chữ thường, kiểm tra tồn tại qua `userRepository.existsByEmail`. Tự động sinh `username` duy nhất với suffix số nếu bị trùng. Mã hóa mật khẩu qua `BCryptPasswordEncoder`, đặt role `ROLE_USER`, `active = true`. Trả về `UserDTO` có đầy đủ `username`.
  - `AuthController.java`: Endpoint `POST /api/v1/auth/register` nhận `@Valid @RequestBody RegisterRequest`.
  - `GlobalExceptionHandler.java`: Cập nhật `NewsCommonException` trả về mã trạng thái `HttpStatus.BAD_REQUEST` (HTTP 400).
  - Đã bổ sung bộ kiểm thử đơn vị `AuthServiceRegisterTest.java` bao phủ 4 ca kiểm thử: thành công, trùng email, tự động sinh username, và giải quyết đụng độ username.
- **Frontend:**
  - `authApi.ts` & `backend.ts`: Thêm hàm gọi `register(data: RegisterRequest)` và bổ sung các interface `RegisterRequest`, `UserDTO`.
  - `app/register/page.tsx`: Tạo mới trang đăng ký Modern Stream với React Hook Form, validate mật khẩu >= 8 ký tự, xác nhận mật khẩu, thông báo Ant Design và cài đặt GuestGuard.
  - `app/auth/register/page.tsx`: Route alias tương thích ngược trỏ đến `RegisterPage`.
  - `app/auth/login/page.tsx`: Cập nhật GuestGuard (redirect về `/` hoặc `/admin` nếu đã login), nhận email điền sẵn từ search param `?email=...`, và gắn link đăng ký.
  - `components/DropdownMenu.tsx`: Kích hoạt nút "Đăng ký tài khoản" dẫn sang `/register`.
  - `context/AuthContext.tsx`: Cung cấp hàm `login` và `logout` trực tiếp, tương thích với cả Admin và WebApp. Đã build production Next.js thành công (11/11 static routes).

## Spec Change Log

## Review Triage Log

- `blind-hunter`: low | Email lowercase normalization applied to ensure consistent lookups. Verified, handled via `trim().toLowerCase()`.
- `edge-case-hunter`: low | Duplicate submit prevention handled via React Query `mutation.isPending` disabling button. Verified.
- `verification-gap`: low | 4 unit tests in `AuthServiceRegisterTest` executed and passed on Java 21; production Next.js build validated with exit code 0. Verified.

## Verification

**Commands:**
- `mvn -f backend/pom.xml test-compile` -- expected: BUILD SUCCESS, không có lỗi cú pháp hoặc typing trong backend.
- `npm --prefix frontend run build` -- expected: Build frontend thành công, route `/register` được tạo.

**Manual checks (if no CLI):**
- Truy cập `http://localhost:3000/register`, nhập thông tin đăng ký hợp lệ, kiểm tra tài khoản được lưu trong MongoDB với password mã hóa BCrypt và role `ROLE_USER`.
- Thử đăng ký lại với cùng email, xác nhận thông báo lỗi email trùng xuất hiện.
- Thử nhập mật khẩu dưới 8 ký tự, xác nhận client hiển thị cảnh báo lỗi inline.
