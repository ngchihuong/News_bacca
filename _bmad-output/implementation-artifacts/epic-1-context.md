# Epic 1 Context: User Identity & Profile Foundation

<!-- Compiled from planning artifacts. Edit freely. Regenerate with compile-epic-context if planning docs change. -->

## Goal

Thiết lập hệ thống định danh người dùng an toàn, xác thực phân quyền bằng JWT stateless, quản lý hồ sơ cá nhân đính kèm avatar qua MinIO và tùy biến giao diện Sáng / Tối (Light/Dark theme), đóng vai trò nền tảng gốc cho toàn bộ hoạt động đăng tin và tương tác của mạng xã hội NewsRoom.

## Stories

- Story 1.1: User Registration & Account Creation
- Story 1.2: User Login, JWT Authentication & Rate Limiting
- Story 1.3: User Profile Management & Avatar Upload via MinIO
- Story 1.4: Public Profile View & Author Showcase
- Story 1.5: Dark & Light Theme Switcher

## Requirements & Constraints

- **Đăng ký tài khoản (FR-1)**: Tiếp nhận email, mật khẩu (tối thiểu 8 ký tự, mã hóa BCrypt), và tên hiển thị. Đảm bảo tính duy nhất của email trong cơ sở dữ liệu. Gán role mặc định `ROLE_USER`, trạng thái `ACTIVE`.
- **Đăng nhập & Xác thực (FR-2, NFR-2)**: Cấp JWT access token (thời hạn 24h) và refresh token (7 ngày). Tự động khóa tài khoản 15 phút nếu nhập sai mật khẩu 5 lần liên tiếp.
- **Quản lý Hồ sơ & Upload Avatar (FR-3, FR-22)**: Cho phép chỉnh sửa tiểu sử bio (tối đa 500 ký tự) và tải lên avatar (định dạng JPG, PNG, WebP dung lượng <= 2MB, kiểm tra magic bytes nhị phân thực tế). Lưu trữ qua MinIO bucket `newsroom` (port public 8887).
- **Hồ sơ Công khai (Story 1.4)**: Trang `/user/{id}` hiển thị thông tin tác giả, avatar, tiểu sử, số liệu followers/following và danh sách bài viết đã xuất bản.
- **Giao diện Sáng / Tối (FR-23)**: Hỗ trợ nút toggle Theme trên thanh điều hướng, lưu lựa chọn vào `localStorage`, tích hợp Ant Design `theme.darkAlgorithm` và Tailwind CSS dark mode.

## Technical Decisions

- **Kiến trúc phân tầng**: Controller -> Service -> Repository -> MongoDB. DTO mapping tự động qua MapStruct.
- **Cấu trúc Dữ liệu Users**: Collection `users` trong MongoDB (port local 27000), có unique index trên trường `email`.
- **Bảo mật Spring Security**: Stateless session với `JwtAuthenticationFilter`, cấu hình endpoint công khai `/api/auth/**` và các endpoint cần token qua `SecurityFilterChain`.
- **MinIO Service**: Tái sử dụng `MinioService` đã cấu hình sẵn trong backend để upload avatar lên port 8887.
- **Chuẩn API Envelope**: Mọi API trả về cấu trúc `{ code: 200, message: "...", data: {...} }` và bắt ngoại lệ tập trung qua `GlobalExceptionHandler`.

## UX & Interaction Patterns

- **Form Validation**: Hiển thị lỗi tức thì tại từng trường nhập liệu (Client-side validation trước khi submit API).
- **Feedback & Thông báo**: Sử dụng Ant Design message component cho thông báo thành công / thất bại.
- **Design Tokens**: Primary Orange `#FF6600`, Slate-50 `#F8FAFC`, Dark Slate `#0F172A`, bo góc mềm mại `md: 12px`.
- **Lưu trữ Token**: Lưu JWT vào `localStorage` và tự động gắn header `Authorization: Bearer <token>` trên mọi request bảo vệ qua interceptor của Axios/Fetch.

## Cross-Story Dependencies

- Story 1.1 và 1.2 là tiền đề bắt buộc cho các thao tác yêu cầu định danh ở mọi story khác.
- Story 1.3 kế thừa cơ chế Auth từ 1.2 và tích hợp MinIO.
- Story 1.4 sử dụng dữ liệu user từ Story 1.1 - 1.3 và danh sách bài viết từ Epic 2.
- Story 1.5 cấu hình Theme Provider dùng chung cho toàn bộ giao diện của dự án.
