---
title: 'Story 1.4: Public Profile View & Author Showcase'
type: 'feature'
created: '2026-09-16'
status: 'done'
baseline_commit: 'a4162427f13318032c2a59265484ca8ece911518'
route: 'dispatch'
review_loop_iteration: 0
context:
  - _bmad-output/planning-artifacts/architecture/architecture-News-2026-09-14/ARCHITECTURE-SPINE.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/DESIGN.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/EXPERIENCE.md
  - _bmad-output/planning-artifacts/epics.md
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** Độc giả và người dùng mạng xã hội NewsRoom chưa thể xem hồ sơ công khai của các tác giả/nhà báo; khi bấm vào tên hoặc avatar của tác giả trên bài viết không có trang chi tiết hiển thị tiểu sử, thống kê người theo dõi, huy hiệu nhà báo xác thực và danh sách bài viết đã xuất bản của tác giả đó.

**Approach:** 
- Backend: Bổ sung các trường `isJournalistVerified`, `journalistOrganization`, `followersCount`, `followingCount` vào model `User`. Cung cấp 2 public REST API an toàn không lộ thông tin nhạy cảm: `GET /api/v1/user/{id}/public` (lấy thông tin hồ sơ công khai) và `GET /api/v1/user/{id}/articles` (lấy danh sách bài viết đã xuất bản của tác giả theo phân trang). Cấu hình mở quyền truy cập công khai trong Spring Security.
- Frontend: Xây dựng component `JournalistBadge` theo đúng Design Tokens (`#0284C7`, `#E0F2FE`, bo góc tròn `full`). Xây dựng trang hồ sơ công khai `/user/[id]` hiển thị avatar, tên, bio, ngày tham gia, số followers/following, huy hiệu nhà báo và danh sách bài viết sắp xếp mới nhất. Tích hợp liên kết từ chi tiết bài viết sang `/user/[id]`.

## Boundaries & Constraints

**Always:**
- Endpoint công khai hồ sơ `GET /api/v1/user/{id}/public` tuyệt đối không được để lộ các trường nhạy cảm PII (mật khẩu, email cá nhân, số điện thoại, failed login attempts, refresh token).
- Cho phép cả khách vãng lai (chưa đăng nhập) và người dùng đã đăng nhập xem trang hồ sơ tác giả `/user/[id]`.
- Nếu tác giả có `isJournalistVerified = true`, hiển thị huy hiệu `JournalistBadge` chuẩn visual token kèm tên đơn vị báo chí (`journalistOrganization`).
- Xử lý trạng thái tài khoản không tồn tại hoặc bị khóa: trả về mã lỗi `USER_NOT_FOUND` và hiển thị trang 404 thân thiện.
- Xử lý trạng thái tác giả chưa có bài viết nào bằng Empty State trực quan, nhẹ nhàng.

**Never:**
- Không yêu cầu JWT token bắt buộc khi đọc thông tin hồ sơ công khai của tác giả hoặc danh sách bài viết công khai.
- Không cho phép chỉnh sửa thông tin từ trang xem hồ sơ công khai (chỉ được sửa qua `/settings/profile` của chính chủ).
- Không hardcode dữ liệu tác giả hay bài viết trong frontend.

## I/O & Edge-Case Matrix

| Scenario | Input / State | Expected Output / Behavior | Error Handling |
|----------|--------------|---------------------------|----------------|
| Xem hồ sơ người dùng thông thường | ID người dùng hợp lệ (`isJournalistVerified = false`) | HTTP 200 OK, trả về avatar, tên, bio, ngày tham gia, followersCount, followingCount | N/A |
| Xem hồ sơ Nhà báo xác thực | ID người dùng có `isJournalistVerified = true` | HTTP 200 OK, hiển thị đầy đủ thông tin kèm `JournalistBadge` và tên cơ quan báo chí | N/A |
| Xem danh sách bài viết tác giả | ID tác giả hợp lệ, `page=0, size=10` | HTTP 200 OK, danh sách bài viết sắp xếp `createdAt` giảm dần kèm phân trang | N/A |
| Tác giả chưa có bài viết | ID tác giả hợp lệ, chưa xuất bản bài nào | HTTP 200 OK, danh sách rỗng, frontend hiển thị Empty State "Tác giả chưa có bài viết nào" | N/A |
| ID người dùng không tồn tại | ID không có trong MongoDB hoặc không hợp lệ | HTTP 404 Not Found, mã lỗi `ErrorCode.USER_NOT_FOUND` | Frontend hiển thị thông báo "Không tìm thấy hồ sơ tác giả" kèm nút quay về Trang chủ |
| Tài khoản bị khóa / không active | User có `active = false` | HTTP 404 Not Found / 403 Forbidden | Frontend thông báo "Hồ sơ không khả dụng hoặc đã bị vô hiệu hóa" |
| Click tác giả trên bài viết | Độc giả click vào tên hoặc avatar tác giả | Điều hướng người dùng tới `/user/{authorId}` | N/A |

</frozen-after-approval>

## Code Map

- `backend/src/main/java/com/newsroom/model/User.java` -- Bổ sung các trường `isJournalistVerified`, `journalistOrganization`, `followersCount`, `followingCount`.
- `backend/src/main/java/com/newsroom/dto/user/PublicUserProfileResponse.java` -- DTO an toàn chỉ chứa dữ liệu công khai của tác giả (không chứa email, phone, mật khẩu).
- `backend/src/main/java/com/newsroom/repository/ArticleRepository.java` -- Bổ sung truy vấn phân trang `findByAuthorIdOrderByCreatedAtDesc(String authorId, Pageable pageable)`.
- `backend/src/main/java/com/newsroom/service/IUserService.java` & `UserServiceImplement.java` -- Bổ sung phương thức `getPublicUserProfile(String userId)`.
- `backend/src/main/java/com/newsroom/service/IArticlesService.java` & `ArticleServiceImpl.java` -- Bổ sung phương thức `getArticlesByAuthor(String authorId, Pageable pageable)`.
- `backend/src/main/java/com/newsroom/controller/UserController.java` -- Bổ sung endpoints `GET /api/v1/user/{id}/public` và `GET /api/v1/user/{id}/articles`.
- `backend/src/main/java/com/newsroom/config/SecurityConfiguration.java` -- Mở permitAll cho `/api/v1/user/*/public` và `/api/v1/user/*/articles`.
- `backend/src/test/java/com/newsroom/service/user/UserServicePublicProfileTest.java` -- Unit test cho chức năng xem hồ sơ công khai và các ca biên.
- `frontend/src/components/JournalistBadge.tsx` -- Component huy hiệu nhà báo xác thực chuẩn thiết kế DESIGN.md.
- `frontend/src/lib/userApi.ts` -- Bổ sung các hàm gọi API public profile và author articles.
- `frontend/src/types/backend.ts` -- Khai báo interface `PublicUserProfile`.
- `frontend/src/app/user/[id]/page.tsx` -- Giao diện trang hồ sơ công khai của tác giả (Author Showcase) và danh sách bài viết.
- `frontend/src/app/news/[slug]/page.tsx` -- Tích hợp hiển thị tác giả kèm liên kết sang `/user/{authorId}`.

## Tasks & Acceptance

**Execution:**
- [x] `backend/src/main/java/com/newsroom/model/User.java` -- Thêm `isJournalistVerified`, `journalistOrganization`, `followersCount`, `followingCount`.
- [x] `backend/src/main/java/com/newsroom/dto/user/PublicUserProfileResponse.java` -- Tạo DTO hồ sơ công khai PII-safe.
- [x] `backend/src/main/java/com/newsroom/repository/ArticleRepository.java` -- Thêm phương thức tìm bài viết theo tác giả sắp xếp thời gian giảm dần.
- [x] `backend/src/main/java/com/newsroom/service/IArticlesService.java` & `ArticleServiceImpl.java` -- Hiện thực hóa `getArticlesByAuthor`.
- [x] `backend/src/main/java/com/newsroom/service/IUserService.java` & `UserServiceImplement.java` -- Hiện thực hóa `getPublicUserProfile`.
- [x] `backend/src/main/java/com/newsroom/controller/UserController.java` -- Thêm endpoint `GET /{id}/public` và `GET /{id}/articles`.
- [x] `backend/src/main/java/com/newsroom/config/SecurityConfiguration.java` -- Cấu hình permitAll cho các endpoint xem hồ sơ và bài viết tác giả.
- [x] `backend/src/test/java/com/newsroom/service/user/UserServicePublicProfileTest.java` -- Viết bộ unit test kiểm thử hồ sơ thường, hồ sơ nhà báo và ca không tìm thấy người dùng.
- [x] `frontend/src/components/JournalistBadge.tsx` -- Tạo component huy hiệu nhà báo với màu sắc `#0284C7` và background `#E0F2FE`.
- [x] `frontend/src/lib/userApi.ts` & `frontend/src/types/backend.ts` -- Khai báo kiểu dữ liệu và client API gọi public profile & articles.
- [x] `frontend/src/app/user/[id]/page.tsx` -- Xây dựng trang hồ sơ công khai: banner thông tin tác giả, thống kê, huy hiệu nhà báo, nút Theo dõi (visual preview), danh sách bài viết đã đăng kèm phân trang và empty state.
- [x] `frontend/src/app/news/[slug]/page.tsx` -- Gắn link chuyển hướng sang `/user/{authorId}` khi click vào tên/avatar tác giả trong trang chi tiết bài viết.

**Acceptance Criteria:**
- Given người dùng hoặc khách vãng lai truy cập `/user/{id}`, when ID tồn tại và tài khoản đang kích hoạt, then hệ thống hiển thị đầy đủ avatar, tên tác giả, bio, ngày tham gia, số lượng Followers và Following.
- Given tác giả là nhà báo đã xác thực (`isJournalistVerified = true`), when xem trang hồ sơ, then huy hiệu `JournalistBadge` màu xanh dương kèm tên cơ quan báo chí hiển thị rõ ràng cạnh tên tác giả.
- Given tác giả có bài viết đã đăng, when xem trang hồ sơ, then danh sách các bài viết được hiển thị theo thứ tự thời gian mới nhất (tiêu đề, ảnh, ngày đăng, chuyên mục, tóm tắt) và có thể click để đọc chi tiết bài viết.
- Given tác giả chưa có bài viết nào, when xem trang hồ sơ, then hiển thị thông báo trạng thái trống (Empty State) thân thiện.
- Given ID người dùng không tồn tại hoặc tài khoản bị vô hiệu hóa, when truy cập `/user/{id}`, then backend trả về 404 và frontend hiển thị thông báo không tìm thấy hồ sơ.
- Given độc giả đang đọc bài viết tại `/news/{slug}`, when click vào tên hoặc avatar tác giả, then trình duyệt điều hướng chính xác tới trang `/user/{id}` của tác giả đó.

## Implementation Notes

- Đã bổ sung các trường xác thực nhà báo (`isJournalistVerified`, `journalistOrganization`) và chỉ số mạng xã hội (`followersCount`, `followingCount`) vào `User.java`.
- Đã tạo `PublicUserProfileResponse.java` bảo vệ dữ liệu PII, không để lộ email, số điện thoại, mật khẩu, failed logins.
- Đã mở rộng `ArticleRepository` và `ArticleServiceImpl` để lấy danh sách bài viết theo tác giả phân trang, sắp xếp giảm dần theo thời gian tạo.
- Đã bổ sung 2 public REST API tại `UserController`: `GET /api/v1/user/{id}/public` và `GET /api/v1/user/{id}/articles`.
- Đã cấu hình permitAll trong `SecurityConfiguration.java` cho cả 2 public endpoints.
- Đã viết bộ unit test `UserServicePublicProfileTest` (5/5 passed) và chạy lại `UserServiceProfileTest` (7/7 passed).
- Đã xây dựng component `JournalistBadge` theo đúng visual design tokens của `DESIGN.md`.
- Đã xây dựng trang `/user/[id]` hiển thị Author Showcase đầy đủ với cover gradient, avatar, thông tin, tiểu sử, số liệu thống kê, danh sách bài viết, phân trang và empty state.
- Đã gắn liên kết chuyển hướng `/user/{authorId}` tại trang chi tiết tin tức `/news/[slug]`.
- Đã build Next.js thành công 100% (`npm run build`).

## Spec Change Log

## Review Triage Log

- [x] [Review][Verified] Ensure PII data (email, phone, passwordHash, failedLoginAttempts) is never exposed via PublicUserProfileResponse [backend/src/main/java/com/newsroom/dto/user/PublicUserProfileResponse.java:1]
- [x] [Review][Verified] Inactive/banned users blocked from public profile viewing with 404 [backend/src/main/java/com/newsroom/service/implement/UserServiceImplement.java:134]
- [x] [Review][Verified] Open endpoints permitAll configured correctly in Spring Security [backend/src/main/java/com/newsroom/config/SecurityConfiguration.java:68]
- [x] [Review][Verified] Verified badge adheres to DESIGN.md tokens with fallback for regular authors [frontend/src/components/JournalistBadge.tsx:1]
- [x] [Review][Verified] Frontend handles empty articles gracefully with empty state component [frontend/src/app/user/[id]/page.tsx:325]

### Review Findings (Adversarial Code Review)

- [x] [Review][Decision] Kiểm tra tồn tại tác giả trong endpoint /api/v1/user/{id}/articles — Đã thêm kiểm tra tồn tại và active của tác giả qua userService.getPublicUserProfile(id), ném USER_NOT_FOUND (404) đồng nhất [backend/src/main/java/com/newsroom/controller/UserController.java:83]
- [x] [Review][Patch] Bổ sung ràng buộc tham số phân trang page/size tránh lỗi 500 IllegalArgumentException [backend/src/main/java/com/newsroom/controller/UserController.java:83]
- [x] [Review][Patch] Sửa fallback thumbnail bài viết không tồn tại /placeholder.jpg [frontend/src/app/user/[id]/page.tsx:55]
- [x] [Review][Patch] Thêm sizes attribute cho Next.js Image fill trong news detail [frontend/src/app/news/[slug]/page.tsx:111]
- [x] [Review][Patch] Bổ sung index MongoDB cho trường author_id và created_at [backend/src/main/java/com/newsroom/model/Article.java:16]
- [x] [Review][Patch] Bổ sung unit test cho ArticleServiceImpl.getArticlesByAuthor [backend/src/test/java/com/newsroom/service/article/ArticleServiceAuthorArticlesTest.java:1]

#### Rejected Findings Appendix:
- Rejected (false): `frontend/src/app/user/[id]/page.tsx:909` — `followersCount` không thể là undefined gây lỗi NaN vì backend dùng primitive `int`, Jackson luôn serialize là 0.
- Rejected (false): `backend/src/main/java/com/newsroom/config/SecurityConfiguration.java:68` — Ant matcher không có dấu gạch chéo đầu là convention chuẩn của toàn dự án trong `ApiPrefixConstants`.

## Design Notes

- **JournalistBadge Visual Token:**
  - Background: `bg-sky-100` (`#E0F2FE`)
  - Text & Icon: `text-sky-700` (`#0284C7`)
  - Border: `border-sky-200`
  - Rounded: `rounded-full`
  - Icon: Checkmark huy hiệu xác thực
- **Author Profile Layout:**
  - Header Card: Nền trắng sạch, avatar kích thước lớn (96x96px hoặc 112x112px), tên tác giả font đậm `text-2xl font-bold`, bên cạnh là huy hiệu nếu có.
  - Tiểu sử (Bio): Hiển thị dưới tên, màu `text-gray-600`, hỗ trợ xuống dòng.
  - Thông tin & Thống kê: Grid hiển thị Ngày tham gia (Calendar icon), Số người theo dõi (Followers), Số người đang theo dõi (Following), Tổng số bài viết đã đăng.
  - Nút Tương tác: Nút "Theo dõi" (Follow) phong cách Pill Button `#FF6600`.
  - Danh sách bài viết: Thẻ bài viết (Post Card) bóng nhẹ, responsive hiển thị ảnh thumbnail và thông tin tóm tắt.

## Verification

**Commands:**
- `$env:JAVA_HOME="C:\Users\nguye\.jdks\corretto-21.0.6"; & "C:\Users\nguye\.m2\wrapper\dists\apache-maven-3.9.11-bin\6mqf5t809d9geo83kj4ttckcbc\apache-maven-3.9.11\bin\mvn.cmd" -f backend/pom.xml test -Dtest=UserServicePublicProfileTest` -- expected: BUILD SUCCESS, tất cả các test cases pass 100%.
- `npm --prefix frontend run build` -- expected: Build Next.js thành công sạch sẽ, không có lỗi type hay lint.

**Manual checks (if no CLI):**
- Mở trang `/user/{id}` với ID của một user thường và một user có role nhà báo xác thực để kiểm tra hiển thị.
- Kiểm tra danh sách bài viết hiển thị đúng thứ tự mới nhất và có thể click vào đọc chi tiết.
- Thử mở ID user không tồn tại để xác nhận trang 404 hiển thị mượt mà.
