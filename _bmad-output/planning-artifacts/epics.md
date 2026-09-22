---
stepsCompleted:
  - step-01-validate-prerequisites
  - step-02-design-epics
  - step-03-create-stories
  - step-03-advanced-elicitation-applied
  - step-04-final-validation
inputDocuments:
  - _bmad-output/planning-artifacts/prds/prd-News-2026-09-14/prd.md
  - _bmad-output/planning-artifacts/architecture/architecture-News-2026-09-14/ARCHITECTURE-SPINE.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/DESIGN.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/EXPERIENCE.md
status: final
updated: 2026-09-14
---

# NewsRoom - Danh mục Phân rã Kế hoạch Phát triển Hệ thống

## Overview

Tài liệu này phân rã toàn bộ các yêu cầu từ PRD, Kiến trúc kỹ thuật (Architecture Spine) và Thiết kế trải nghiệm người dùng (UX Design Spines) của hệ thống **NewsRoom** thành danh sách các Epic và User Story chi tiết, có tiêu chí chấp nhận (Acceptance Criteria) kiểm thử được dành cho kỹ sư phát triển. Tài liệu đã trải qua vòng phản biện chuyên sâu **Advanced Elicitation (Pre-mortem, Boundary Sweep, Assumption Audit)** để bịt kín mọi ca lỗi biên và rủi ro vận hành.

## Requirements Inventory

### Functional Requirements

- **FR-1: User Registration** — Người dùng chưa đăng ký có thể tạo tài khoản mới bằng email, mật khẩu và tên hiển thị. Hệ thống hash BCrypt, kiểm tra email duy nhất và tạo user với role `USER`.
- **FR-2: User Login / Logout** — Người dùng đăng nhập bằng email/mật khẩu, nhận JWT access token (24h) và refresh token (7 ngày). Logout hủy token client. Khóa tài khoản 15 phút nếu nhập sai 5 lần liên tiếp.
- **FR-3: User Profile Management** — Người dùng xem và chỉnh sửa thông tin cá nhân (tên hiển thị, avatar qua MinIO port 8887, tiểu sử bio, ngày tham gia). Xem profile công khai của người khác tại `/user/{username}`.
- **FR-4: Personal Feed Generation** — Feed cá nhân kết hợp bài viết từ người đang theo dõi (Followed Users) và bài viết nổi bật (Trending), hiển thị bài mới nhất trước, hỗ trợ phân trang/infinite scroll.
- **FR-5: Public Feed (Home)** — Khách vãng lai (Guest) và người dùng có thể xem dòng tin công khai sắp xếp theo điểm thịnh hành kết hợp thời gian. Khách chỉ xem, bấm tương tác hiện nhắc nhở đăng nhập.
- **FR-6: Create Post** — Người dùng đã đăng nhập có thể đăng bài viết với tiêu đề (tối đa 200 ký tự), nội dung, đính kèm tối đa 5 ảnh qua MinIO, chọn chuyên mục bắt buộc, gắn thẻ tag tự do và chọn phạm vi hiển thị. Giới hạn tần suất 5 bài / 10 phút.
- **FR-7: Edit Post** — Tác giả hoặc Admin có thể chỉnh sửa nội dung bài viết đã đăng. Nếu bài đã bị gắn cờ kiểm duyệt, việc chỉnh sửa sẽ kích hoạt quét lại từ khóa.
- **FR-8: Delete Post** — Tác giả có thể xóa bài viết của mình (áp dụng Soft Delete để có thể khôi phục). Admin có quyền xóa vĩnh viễn (Hard Delete).
- **FR-9: Draft Posts** — Người dùng có thể lưu bài viết dưới dạng bản nháp và tiếp tục chỉnh sửa trước khi xuất bản chính thức.
- **FR-10: Like Post** — Người dùng có thể Thích/Bỏ thích (Toggle Like) bài viết. Số lượt like cập nhật tức thì (Optimistic update với Debounce 300ms) và đảm bảo không âm.
- **FR-11: Comment on Post** — Người dùng có thể bình luận văn bản (tối đa 2000 ký tự) trên bài viết công khai hoặc bài viết của người mình đang theo dõi.
- **FR-12: Share Post** — Người dùng có thể chia sẻ bài viết công khai lên trang cá nhân dưới dạng bản tham chiếu (reference), tăng bộ đếm share của bài gốc.
- **FR-13: Follow/Unfollow User** — Người dùng có thể theo dõi hoặc hủy theo dõi người dùng khác một chiều. Bài viết của người được theo dõi sẽ xuất hiện trên Personal Feed.
- **FR-14: Follower/Following Lists** — Người dùng có thể xem danh sách người theo dõi và người đang theo dõi của bất kỳ ai kèm nút Follow/Unfollow nhanh.
- **FR-15: Set Post Visibility** — Tác giả có thể chọn 3 mức hiển thị: Công khai (Public), Chỉ người theo dõi (Followers-only), hoặc Riêng tư (Private).
- **FR-16: Auto-scan on Publish** — Hệ thống chuẩn hóa chuỗi và tự động quét từ khóa cấm/nhạy cảm khi đăng bài. Nếu phát hiện vi phạm, chuyển trạng thái `FLAGGED_PENDING_REVIEW`, bài không hiện trên Public Feed.
- **FR-17: Community Report** — Người dùng có thể gửi báo cáo vi phạm kèm lý do (Tin giả, Ngôn từ xúc phạm, Spam, Bản quyền). Bài viết người dùng thường đạt ≥ 3 báo cáo tự động tạm ẩn; bài viết của Nhà báo xác thực được gắn cờ ưu tiên cao cho Admin duyệt tay.
- **FR-18: Admin Moderation Actions** — Admin duyệt danh sách bài viết bị báo cáo/gắn cờ với các hành động: Duyệt công khai lại (Approve), Gỡ bỏ bài viết (Remove), Cảnh cáo tài khoản (Warn), hoặc Khóa tài khoản (Ban).
- **FR-19: Admin Dashboard (Enhance)** — Nâng cấp Dashboard quản trị: bổ sung số liệu người dùng mới, chỉ số tương tác trong ngày, hàng đợi kiểm duyệt và hiệu suất quảng cáo.
- **FR-20: Advertisement System (Integrate)** — Tích hợp hệ thống quảng cáo sẵn có vào dòng tin tức mạng xã hội (In-feed ads xuất hiện 1 banner sau mỗi 6 bài viết) và theo dõi lượt hiển thị/click.
- **FR-21: Category & Tag System (Maintain)** — Duy trì hệ thống chuyên mục chuẩn và danh mục thẻ tags, cho phép lọc bài viết theo chuyên mục tại `/category/{slug}`.
- **FR-22: File Upload (Maintain & Enhance)** — Tích hợp upload hình ảnh qua MinIO Object Storage (`news_minio`) hỗ trợ định dạng JPG, PNG, WebP (tối đa 5MB/file), kiểm tra MIME type thực tế.
- **FR-23: Theme Customization (Dark / Light Mode)** — Hỗ trợ chuyển đổi nhanh chế độ Sáng / Tối linh hoạt, lưu cấu hình vào `localStorage`, tích hợp Ant Design `theme.darkAlgorithm` và Tailwind CSS dark variant.
- **FR-24: Video Embed Support** — Tự động nhận diện URL và nhúng khung phát video mượt mà (responsive player với iframe sandbox an toàn) khi chèn link YouTube, TikTok hoặc Facebook Reels.

### NonFunctional Requirements

- **NFR-1: Performance & Latency** — API p95 latency < 500ms đối với tất cả các endpoint đọc Feed. Thời gian tải trang Profile < 2s. Tải phân trang Feed < 1s.
- **NFR-2: Security & Authentication** — Mật khẩu mã hóa BCrypt. Token JWT stateless hết hạn sau 24h, refresh token 7 ngày. Giới hạn tần suất đăng nhập sai (5 lần khóa 15 phút). Phân quyền nghiêm ngặt 3 vai trò: `USER`, `JOURNALIST`, `ADMIN`.
- **NFR-3: Data Integrity & Concurrency** — Atomic increment (`$inc`) cho các bộ đếm `likeCount`, `commentCount`, `shareCount` trên Document bài viết kèm điều kiện chặn giá trị âm. Unique compound index `{ userId: 1, postId: 1, type: 1 }` trên `interactions`.
- **NFR-4: Storage Scalability & Ports** — Lưu trữ ảnh tĩnh qua MinIO Object Storage (port public 8887, bucket `newsroom`), MongoDB kết nối chuẩn xác theo môi trường local port `27000`.
- **NFR-5: Usability & Accessibility** — Tuân thủ tiêu chuẩn tiếp cận WCAG 2.1 AA (tỷ lệ tương phản ≥ 4.5:1 ở cả 2 giao diện Sáng & Tối, phím tắt điều hướng nhanh `/`, `c`, `j`/`k`, hỗ trợ screen reader).

### Additional Requirements (Architecture Spine)

- **ARCH-1: Strict Layered Dependency Direction** — Tuân thủ kiến trúc phân tầng một chiều: `Controller -> Service -> Repository -> MongoDB`. Controller tuyệt đối không truy vấn trực tiếp Repository. DTO mapping tự động qua MapStruct 1.6.
- **ARCH-2: Feed Fan-out on Read Strategy** — Cơ chế truy vấn Feed tại thời điểm đọc với Compound Indexes bắt buộc: `{ authorId: 1, status: 1, createdAt: -1 }` và `{ authorRole: 1, status: 1, createdAt: -1 }`.
- **ARCH-3: MinIO S3-compatible Integration [ADOPTED]** — Tái sử dụng `MinioService` và container `news_minio` sẵn có (port 8887 API S3) để phục vụ ảnh bài viết và avatar.
- **ARCH-4: Synchronous Pre-save Keyword Filter with Normalization** — Bộ lọc từ khóa chạy đồng bộ trên văn bản đã được unidecode/chuẩn hóa ngay lúc gọi `POST /api/posts` đảm bảo bài viết vi phạm không xuất hiện trên feed công cộng dù chỉ 1 giây.
- **ARCH-5: Standard API Envelope** — Tất cả API endpoints tuân thủ cấu trúc đồng nhất: `{ code, message, data }` và Global Exception Handler xử lý lỗi tập trung.

### UX Design Requirements (UX Spines)

- **UX-DR1: Design System & Token Foundation** — Hiện thực hóa bộ Design Tokens theo Google Labs spec: Primary Orange `#FF6600`, Secondary Navy `#13357B`, Journalist Sky Blue `#0284C7`, Canvas Slate-50 `#F8FAFC`, Border `#E2E8F0`, bo góc `sm: 6px`, `md: 12px`, `full: 9999px`.
- **UX-DR2: Reusable Post Card Component (`PostCard`)** — Thẻ bài viết tinh giản phong cách Modern Stream: Avatar tác giả, Tên, Badge nhà báo (nếu có), Thời gian, Nội dung, Khung lưới ảnh (1 đến 4 ảnh), Thanh tương tác chân thẻ (Like, Comment, Share, Báo cáo •••).
- **UX-DR3: Journalist Verified Trust Badge (`JournalistBadge`)** — Huy hiệu xanh Sky Blue bo tròn kèm tick xác thực và tên cơ quan báo chí; đường viền nhấn 3px màu xanh Sky Blue ở cạnh trái thẻ bài viết của nhà báo.
- **UX-DR4: Quick Composer Component (`PostComposer`)** — Khung soạn bài nhanh ở đầu Feed: mặc định 1 dòng, mở rộng 3 dòng khi focus kèm nút đính kèm ảnh, chọn chuyên mục và tự động lưu nháp vào `localStorage`.
- **UX-DR5: Sticky Feed Tabs (`FeedTabs`)** — Thanh chuyển tab bám dính đầu trang mượt mà gồm: "Dành cho bạn", "Tin Nhà Báo", "Đang theo dõi".
- **UX-DR6: Responsive Multi-surface Layout** — Desktop 3 cột cân xứng (Sidebar trái, Feed giữa, Trending phải); Mobile 1 cột tràn viền kèm thanh điều hướng đáy (Bottom Navigation Bar cố định).
- **UX-DR7: In-Feed Sponsored Card (`InFeedAdCard`)** — Thẻ bài quảng cáo đối tác hòa nhập tự nhiên vào dòng tin, có nhãn "Được tài trợ" màu hổ phách và nút CTA.
- **UX-DR8: Skeleton Loading & Dual Empty States** — Khung xương Ant Design Skeleton khi tải trang; Phân biệt 2 trạng thái Empty State của tab Đang theo dõi (chưa follow ai vs đã follow nhưng chưa có bài mới).
- **UX-DR9: Community Report Dialog (`ReportModal`)** — Hộp thoại báo cáo vi phạm với 4 nhóm lý do chuẩn (Tin giả, Ngôn từ xúc phạm, Spam, Bản quyền) kèm microcopy trấn an người dùng.
- **UX-DR10: Keyboard Shortcuts & Accessibility Floor** — Hỗ trợ phím tắt `/` tìm kiếm, `c` viết bài nhanh, `j`/`k` cuộn tin; đầy đủ `aria-label` cho các nút chỉ có biểu tượng icon.
- **UX-DR11: Dark & Light Theme Switcher** — Nút toggle giao diện Sáng / Tối trên thanh điều hướng; chuyển đổi mượt mà giữa Slate-50 sang nền tối sâu (Dark slate) mà không chói mắt.
- **UX-DR12: Video Embed Player with Fallback** — Component hiển thị video nhúng tự động co giãn theo tỷ lệ 16:9 với cơ chế sandbox an toàn và UI fallback khi video lỗi.

### Deferred to Future Releases (V2 / V3)

- **Chat & Messaging (V2)**: Tin nhắn cá nhân 1-1 và Chat nhóm thời gian thực (WebSocket).
- **Groups & Communities (V2)**: Mô hình hội nhóm / cộng đồng theo chủ đề kiểu Facebook Groups.
- **Native Video Hosting & Transcoding (V3)**: Pipeline convert video đa độ phân giải với ffmpeg và HLS streaming.

### FR Coverage Map

- **FR-1**: Epic 1 — Story 1.1: User Registration & Account Creation
- **FR-2**: Epic 1 — Story 1.2: User Login, JWT Authentication & Rate Limiting
- **FR-3**: Epic 1 — Story 1.3: User Profile Management & MinIO Avatar Upload & Story 1.4: Public Profile View
- **FR-4**: Epic 3 — Story 3.2: Personal Feed Generation & Story 3.4: Infinite Scroll & State Patterns
- **FR-5**: Epic 3 — Story 3.1: Public Feed for Guests & Discovery
- **FR-6**: Epic 2 — Story 2.2: Post Creation with Rich Content & MinIO Image Upload
- **FR-7**: Epic 2 — Story 2.6: Edit Existing Post & Re-scan Trigger
- **FR-8**: Epic 2 — Story 2.7: Soft Delete Post with Unique Slug Suffix
- **FR-9**: Epic 2 — Story 2.5: Draft Post Save & Resume
- **FR-10**: Epic 4 — Story 4.1: Post Like & Unlike Toggle with Debounce & Non-negative Atomic Counter
- **FR-11**: Epic 4 — Story 4.2: Text Comments on Posts
- **FR-12**: Epic 4 — Story 4.3: Internal Post Sharing
- **FR-13**: Epic 4 — Story 4.4: Follow & Unfollow Authors
- **FR-14**: Epic 4 — Story 4.5: Followers and Following Lists
- **FR-15**: Epic 2 — Story 2.4: Post Visibility Control
- **FR-16**: Epic 5 — Story 5.1: Normalized Keyword Pre-save Auto-scan
- **FR-17**: Epic 5 — Story 5.2: Community Report Post Modal & Journalist Anti-brigading Protection
- **FR-18**: Epic 5 — Story 5.3: Admin Moderation Queue & Content Actions
- **FR-19**: Epic 6 — Story 6.1: Enhanced Admin Dashboard Statistics
- **FR-20**: Epic 6 — Story 6.2: In-Feed Advertisement Injection & Story 6.3: Impression Tracking
- **FR-21**: Epic 2 — Story 2.1: Category & Tag Selection for Posts
- **FR-22**: Epic 2 — Story 2.2: Post Creation with Rich Content & MinIO Image Upload
- **FR-23**: Epic 1 — Story 1.5: Dark & Light Theme Switcher
- **FR-24**: Epic 2 — Story 2.3: Sandboxed Video Embed Support for YouTube/TikTok

## Tóm tắt 6 Nhóm Tính năng Lớn

- **Epic 1: User Identity & Profile Foundation (Định danh người dùng & Quản lý hồ sơ)**: Cung cấp nền tảng định danh an toàn cho toàn bộ hệ thống: người dùng có thể đăng ký tài khoản, đăng nhập qua JWT với cơ chế khóa tài khoản chống brute-force, quản lý hồ sơ cá nhân (avatar qua MinIO port 8887, bio), xem trang cá nhân của tác giả khác và chuyển đổi giao diện Sáng / Tối (Light/Dark mode). **FRs covered:** FR-1, FR-2, FR-3, FR-23.

- **Epic 2: Content Creation & Publishing (Sáng tạo & Xuất bản tin tức)**: Cung cấp công cụ xuất bản tin tức toàn diện: người dùng và nhà báo có thể soạn bài nhanh (có rate limit 5 bài/10p), đính kèm ảnh (qua MinIO) hoặc nhúng link video (YouTube/TikTok có sandbox an toàn), chọn chuyên mục/tag, lưu bản nháp, cài đặt quyền xem (Công khai / Người theo dõi / Riêng tư), và chỉnh sửa/xóa bài viết (slug có suffix timestamp chống đụng độ). **FRs covered:** FR-6, FR-7, FR-8, FR-9, FR-15, FR-21, FR-22, FR-24.

- **Epic 3: Social Feed & Content Discovery (Dòng tin tức xã hội & Khám phá tin)**: Mang lại trải nghiệm đọc tin tức hiện đại (Modern Stream): dòng tin cá nhân hóa (Personal Feed) cho người đã đăng nhập và dòng tin công cộng (Public Feed) cho khách vãng lai; phân tách các luồng "Dành cho bạn", "Tin Nhà Báo" (có badge xác thực và viền xanh nổi bật), "Đang theo dõi"; xử lý tinh tế 2 trạng thái Empty State; tối ưu hiển thị mượt mà trên Desktop lẫn Mobile. **FRs covered:** FR-4, FR-5.

- **Epic 4: Social Interactions & Network Growth (Tương tác xã hội & Mạng lưới theo dõi)**: Biến người đọc thành cộng đồng tương tác chủ động: người dùng có thể thả tim (Toggle Like) cập nhật tức thì với debounce 300ms và bộ đếm chặn âm, bình luận văn bản, chia sẻ bài viết, và xây dựng mạng lưới theo dõi (Follow/Unfollow) các tác giả và nhà báo yêu thích. **FRs covered:** FR-10, FR-11, FR-12, FR-13, FR-14.

- **Epic 5: Content Trust & Moderation (Kiểm duyệt & Giữ sạch môi trường tin tức)**: Xây dựng niềm tin và sự an toàn cho nền tảng: tự động quét từ khóa cấm đã qua chuẩn hóa chuỗi (chống teencode bypass), cơ chế cho người dùng báo cáo vi phạm kèm chính sách bảo vệ Nhà báo khỏi tấn công report bẩn, và hàng đợi quản trị cho Admin xử lý (Duyệt, Gỡ bài, Cảnh cáo, Khóa tài khoản). **FRs covered:** FR-16, FR-17, FR-18.

- **Epic 6: Monetization & Platform Administration (Khai thác Quảng cáo & Quản trị nền tảng)**: Hoàn thiện khả năng vận hành và sinh doanh thu: bảng điều khiển Admin Dashboard trực quan theo dõi số liệu người dùng, bài viết, tương tác; tích hợp hệ thống quảng cáo hiển thị tự nhiên vào dòng tin tức (In-feed Ads sau mỗi 6 bài) và thống kê lượt hiển thị/click. **FRs covered:** FR-19, FR-20.

---

## Epic 1: User Identity & Profile Foundation

Cung cấp nền tảng tài khoản người dùng, xác thực bảo mật JWT, quản lý thông tin cá nhân và tùy biến giao diện Sáng / Tối.

### Story 1.1: User Registration & Account Creation

As a khách truy cập chưa có tài khoản,  
I want đăng ký tài khoản NewsRoom bằng email, mật khẩu và tên hiển thị,  
So that tôi có thể tham gia đăng bài và tương tác với cộng đồng tin tức.

**Acceptance Criteria:**

**Given** người dùng ở trang Đăng ký `/register`  
**When** người dùng nhập email hợp lệ, mật khẩu ≥ 8 ký tự và tên hiển thị, sau đó bấm "Đăng ký"  
**Then** hệ thống mã hóa mật khẩu bằng BCrypt, lưu User mới vào MongoDB với `role = ROLE_USER`, `status = ACTIVE`, và trả về thông báo thành công  
**And** nếu email đã tồn tại trong database, hệ thống trả về lỗi 400 kèm thông báo "Email đã được sử dụng"  
**And** nếu mật khẩu < 8 ký tự hoặc để trống trường bắt buộc, hiển thị lỗi validation ngay tại form frontend.

### Story 1.2: User Login, JWT Authentication & Rate Limiting

As a người dùng đã có tài khoản,  
I want đăng nhập bằng email và mật khẩu để nhận token xác thực an toàn,  
So that tôi có thể truy cập các tính năng dành riêng cho thành viên đã đăng nhập.

**Acceptance Criteria:**

**Given** người dùng ở trang Đăng nhập `/login`  
**When** người dùng nhập đúng email và mật khẩu  
**Then** hệ thống trả về JWT access token (thời hạn 24h), refresh token (7 ngày), và thông tin cơ bản của User (`id`, `email`, `displayName`, `role`)  
**And** frontend lưu token vào `localStorage` và tự động đính kèm `Authorization: Bearer <token>` trong các request tiếp theo  
**And** nếu người dùng nhập sai mật khẩu 5 lần liên tiếp, tài khoản bị tạm khóa 15 phút và hiển thị thông báo yêu cầu thử lại sau  
**And** khi người dùng bấm "Đăng xuất", frontend xóa token khỏi `localStorage` và chuyển hướng về trang chủ.

### Story 1.3: User Profile Management & Avatar Upload via MinIO

As a người dùng đã đăng nhập,  
I want cập nhật tên hiển thị, tiểu sử bio và tải ảnh đại diện cá nhân,  
So that hồ sơ của tôi thể hiện rõ danh tính khi tham gia đưa tin.

**Acceptance Criteria:**

**Given** người dùng truy cập trang chỉnh sửa hồ sơ `/settings/profile`  
**When** người dùng tải lên file ảnh avatar (JPG, PNG, WebP ≤ 2MB, kiểm tra MIME type nhị phân thực tế)  
**Then** backend gọi `MinioService` upload ảnh vào bucket `newsroom`, sinh URL truy cập công khai qua port 8887 (`http://localhost:8887/newsroom/...`) và lưu `avatarUrl` vào document User  
**And** khi người dùng cập nhật bio (tối đa 500 ký tự) và bấm "Lưu thay đổi", thông tin được cập nhật thành công và hiển thị phản hồi Ant Design message  
**And** nếu upload file không đúng định dạng ảnh hoặc dung lượng > 2MB, hệ thống từ chối và thông báo lỗi rõ ràng.

### Story 1.4: Public Profile View & Author Showcase

As a độc giả trên NewsRoom,  
I want xem trang hồ sơ công khai của bất kỳ tác giả nào tại `/user/{id}`,  
So that tôi có thể biết thông tin, số người theo dõi và đọc các bài viết do tác giả đó xuất bản.

**Acceptance Criteria:**

**Given** người dùng click vào tên hoặc avatar của một tác giả trên bài viết  
**When** trang `/user/{id}` được mở  
**Then** hiển thị đầy đủ avatar, tên tác giả, bio, ngày tham gia, số lượng người theo dõi (Followers) và số người đang theo dõi (Following)  
**And** nếu tác giả là nhà báo đã xác thực (`isJournalistVerified = true`), hiển thị huy hiệu `JournalistBadge` kèm tên cơ quan báo chí bên cạnh tên  
**And** bên dưới hiển thị danh sách các bài viết công khai của tác giả này sắp xếp theo thời gian mới nhất.

### Story 1.5: Dark & Light Theme Switcher

As a người dùng đọc tin tức vào ban đêm hoặc môi trường thiếu sáng,  
I want chuyển đổi giữa giao diện Sáng (Light mode) và Tối (Dark mode),  
So that mắt tôi không bị mỏi khi đọc tin trong thời gian dài.

**Acceptance Criteria:**

**Given** người dùng ở bất kỳ trang nào trên NewsRoom  
**When** người dùng click vào nút icon Mặt trời / Mặt trăng trên Header hoặc Menu di động  
**Then** toàn bộ giao diện chuyển đổi tức thì giữa nền sáng (`#F8FAFC`) sang nền tối sâu (Dark slate) thông qua Ant Design `theme.darkAlgorithm` và class `dark` của Tailwind  
**And** tùy chọn giao diện được lưu vào `localStorage` để giữ nguyên trạng thái khi người dùng mở lại trang ở các phiên tiếp theo  
**And** tất cả màu văn bản, đường viền và huy hiệu đảm bảo độ tương phản WCAG 2.1 AA ở cả hai chế độ.

---

## Epic 2: Content Creation & Publishing

Cung cấp toàn bộ công cụ và luồng xuất bản tin tức đa phương tiện (ảnh MinIO, video nhúng có sandbox an toàn), gắn nhãn chuyên mục, bản nháp, rate limit và phân quyền hiển thị.

### Story 2.1: Category & Tag Selection for Posts

As a tác giả soạn tin tức,  
I want chọn chuyên mục phù hợp (Thời sự, Giao thông, Công nghệ, Đời sống) và gắn các thẻ hashtag chi tiết,  
So that bài viết của tôi được phân loại đúng và độc giả dễ dàng tìm thấy.

**Acceptance Criteria:**

**Given** người dùng đang mở khung soạn thảo bài viết  
**When** người dùng mở danh sách chuyên mục  
**Then** hiển thị danh sách các Category hoạt động lấy từ `CategoryRepository` để người dùng bắt buộc chọn 1 chuyên mục  
**And** người dùng có thể nhập tối đa 10 thẻ tags (tự do hoặc gợi ý có sẵn bắt đầu bằng `#`)  
**And** nếu người dùng không chọn chuyên mục, nút "Đăng tin" sẽ bị vô hiệu hóa kèm cảnh báo validation.

### Story 2.2: Post Creation with Rich Content & MinIO Image Upload

As a người dùng đã đăng nhập,  
I want viết bài tin tức với tiêu đề, nội dung và đính kèm tối đa 5 ảnh chụp hiện trường,  
So that tôi có thể chia sẻ thông tin sự kiện một cách trực quan và đầy đủ.

**Acceptance Criteria:**

**Given** người dùng nhập tiêu đề (tối đa 200 ký tự) và nội dung bài viết trong ô Composer  
**When** người dùng chọn đính kèm từ 1 đến 5 ảnh (JPG, PNG, WebP ≤ 5MB mỗi ảnh, kiểm tra magic bytes MIME thực tế) và bấm "Đăng tin"  
**Then** backend áp dụng rate limit: nếu người dùng đăng vượt quá 5 bài / 10 phút, hệ thống từ chối kèm thông báo "Bạn đang đăng tin quá nhanh, vui lòng chờ ít phút"  
**And** backend gọi `MinioService` upload từng ảnh lên MinIO, lưu mảng `imageUrls` (trỏ cổng 8887) vào document Post với trạng thái `PUBLISHED`  
**And** bài viết mới xuất hiện ngay trên đầu Feed của người dùng mà không cần reload trang (React Query cache update)  
**And** nếu tổng số ảnh > 5 ảnh hoặc có file dung lượng > 5MB, hệ thống hiển thị thông báo lỗi và không gửi request.

### Story 2.3: Video Embed Support for YouTube/TikTok with Sandbox & Fallback

As a người dùng đăng bài,  
I want dán liên kết video từ YouTube, TikTok hoặc Facebook Reels vào bài viết,  
So that người đọc có thể bấm xem video trực tiếp trên dòng tin một cách an toàn và mượt mà.

**Acceptance Criteria:**

**Given** người dùng dán link video (YouTube, TikTok, Facebook Reels) vào nội dung bài  
**When** bài viết được lưu và hiển thị trên giao diện  
**Then** frontend tự động nhận diện regex URL và nhúng component `VideoEmbedPlayer` dạng iframe tỷ lệ 16:9 với thuộc tính `sandbox="allow-scripts allow-same-origin allow-presentation"` để ngăn chặn script độc hại  
**And** video phát mượt mà trực tiếp trong card bài viết với đầy đủ nút điều khiển  
**And** nếu video là link riêng tư (Private) hoặc bị xóa trên nền tảng gốc, hiển thị fallback UI trang nhã: *"Video không khả dụng hoặc đã bị gỡ bỏ [Mở link gốc]"* mà không để lại khung đen xì xô lệch layout.

### Story 2.4: Post Visibility Control (Public, Followers-Only, Private)

As a tác giả bài viết,  
I want cài đặt phạm vi người xem bài viết (Công khai, Chỉ người theo dõi, Riêng tư),  
So that tôi kiểm soát được đối tượng độc giả tiếp cận thông tin của mình.

**Acceptance Criteria:**

**Given** người dùng chuẩn bị đăng bài hoặc chỉnh sửa bài viết đã có  
**When** người dùng chọn 1 trong 3 mức Visibility: `PUBLIC`, `FOLLOWERS_ONLY`, `PRIVATE`  
**Then** bài viết `PUBLIC` hiển thị trên Public Feed cho tất cả mọi người (kể cả Guest)  
**And** bài viết `FOLLOWERS_ONLY` chỉ hiển thị trên feed của những người đang follow tác giả; khách vãng lai vào URL trực tiếp sẽ nhận thông báo "Nội dung chỉ dành cho người theo dõi"  
**And** bài viết `PRIVATE` chỉ hiển thị trong danh sách bài viết riêng của chính tác giả đó.

### Story 2.5: Draft Post Save & Resume

As a người dùng đang viết bài nhưng chưa hoàn thành,  
I want lưu bài viết dưới dạng bản nháp và quay lại chỉnh sửa sau,  
So that tôi không bị mất nội dung dở dang khi có việc bận đột xuất.

**Acceptance Criteria:**

**Given** người dùng đang nhập bài viết trong khung soạn thảo  
**When** người dùng bấm nút "Lưu nháp" (Save Draft)  
**Then** bài viết được lưu vào MongoDB với `status = DRAFT`, không xuất hiện trên bất kỳ dòng tin công cộng nào  
**And** trong trang quản lý bài viết của tôi `/my-posts?tab=drafts`, hiển thị danh sách các bài nháp kèm thời gian cập nhật  
**And** khi người dùng bấm vào bài nháp, toàn bộ tiêu đề, nội dung, ảnh đã đính kèm được tải lại vào Composer để tiếp tục viết và bấm "Xuất bản".

### Story 2.6: Edit Existing Post & Re-scan Trigger

As a tác giả bài viết,  
I want chỉnh sửa lại nội dung hoặc thay đổi ảnh bài viết đã đăng,  
So that tôi có thể cập nhật thông tin diễn biến mới nhất của sự kiện.

**Acceptance Criteria:**

**Given** người dùng là tác giả bài viết hoặc Admin  
**When** chọn "Chỉnh sửa bài viết" từ menu ••• trên góc card, sửa nội dung và bấm "Lưu cập nhật"  
**Then** backend kiểm tra quyền sở hữu (chỉ tác giả hoặc role `ADMIN` mới được sửa), cập nhật trường `content`, `updatedAt` và trả về mã 200  
**And** card bài viết trên Feed tự động cập nhật nội dung mới kèm nhãn nhỏ "(Đã chỉnh sửa)" bên cạnh timestamp  
**And** nếu bài viết bị chỉnh sửa bởi user khác không có quyền, backend trả về lỗi 403 Forbidden.

### Story 2.7: Soft Delete Post with Unique Slug Suffix

As a tác giả bài viết,  
I want xóa bài viết của mình khỏi hệ thống mà không lo đụng độ dữ liệu nếu đăng lại sau này,  
So that bài viết không còn hiển thị trên dòng tin khi thông tin không còn chính xác.

**Acceptance Criteria:**

**Given** người dùng click "Xóa bài viết" từ menu ••• trên card bài của mình  
**When** popup xác nhận xuất hiện và người dùng bấm "Đồng ý xóa"  
**Then** backend thực hiện Soft Delete bằng cách cập nhật `status = DELETED` trên document Post  
**And** slug của bài viết được thiết kế có hậu tố định danh (ví dụ: `slug = base-title + '-' + timestamp`) để nếu người dùng đăng lại bài viết cùng tên sau khi xóa thì không bao giờ bị trùng khóa Unique Index trên MongoDB  
**And** bài viết lập tức biến mất khỏi Feed và trang cá nhân của tác giả; các tương tác like/comment được ẩn đồng bộ.

---

## Epic 3: Social Feed & Content Discovery

Xây dựng trải nghiệm dòng tin tức xã hội Modern Stream, phân tách các luồng tin thông minh và tối ưu tốc độ đọc tin trên đa nền tảng.

### Story 3.1: Public Feed for Guests & Discovery

As a khách vãng lai hoặc người dùng mới chưa follow ai,  
I want xem dòng tin tức công cộng tổng hợp các tin nóng mới nhất trên trang chủ,  
So that tôi có thể nắm bắt ngay tình hình sự kiện thời sự đang diễn ra.

**Acceptance Criteria:**

**Given** người dùng mở trang chủ NewsRoom `/` khi chưa đăng nhập  
**When** trang chủ tải dữ liệu  
**Then** backend truy vấn các bài viết có `visibility = PUBLIC` và `status = PUBLISHED`, sắp xếp theo thời gian mới nhất và trả về danh sách 20 bài đầu tiên  
**And** mỗi thẻ bài hiển thị đúng chuẩn `PostCard`: Avatar, Tên tác giả, Badge nhà báo (nếu có), Chuyên mục, Thời gian đăng, Nội dung tóm tắt, Khung lưới ảnh và các bộ đếm like/comment  
**And** khi khách vãng lai bấm nút Like hoặc Comment, hiển thị modal nhắc nhở "Vui lòng đăng nhập để tương tác với bài viết".

### Story 3.2: Personal Feed with Followed Authors & Fan-out on Read

As a người dùng đã đăng nhập,  
I want xem dòng tin cá nhân hóa hiển thị bài viết từ những người tôi đang theo dõi tại tab "Đang theo dõi",  
So that tôi luôn cập nhật được thông tin từ các tác giả và nhà báo mà tôi tin tưởng.

**Acceptance Criteria:**

**Given** người dùng đã đăng nhập và chuyển sang tab "Đang theo dõi"  
**When** request `GET /api/feed/following` được gửi lên backend  
**Then** backend lấy danh sách `followingIds` của người dùng từ collection `follows`, thực hiện truy vấn MongoDB:  
`db.posts.find({ authorId: { $in: followingIds }, status: "PUBLISHED" }).sort({ createdAt: -1 })` sử dụng compound index tối ưu  
**And** thời gian phản hồi API p95 đạt < 500ms đối với trang đầu 20 bài viết  
**And** bài viết từ các tác giả vừa được bấm Follow sẽ xuất hiện ngay trong feed ở lần tải tiếp theo.

### Story 3.3: Verified Journalist Stream & Trust Badge Display

As a độc giả muốn tìm nguồn tin chính thống có kiểm chứng,  
I want bấm vào tab "Tin Nhà Báo" để chỉ đọc các bài viết từ những nhà báo đã xác thực,  
So that tôi tiếp cận được thông tin uy tín mà không sợ tin đồn thất thiệt.

**Acceptance Criteria:**

**Given** người dùng ở trang chủ Feed  
**When** người dùng click vào tab "Tin Nhà Báo" trên thanh `FeedTabs`  
**Then** feed lọc ra danh sách bài viết từ các tác giả có `authorRole = JOURNALIST` hoặc `isJournalistVerified = true`  
**And** tất cả thẻ bài trong luồng này đều hiển thị đường viền nhấn 3px màu xanh Sky Blue `{colors.journalist-badge}` ở cạnh trái thẻ và huy hiệu `JournalistBadge` có dấu tick xác thực bên cạnh tên tác giả  
**And** khi rê chuột vào huy hiệu, hiển thị popover thông tin: "Nhà báo đã được xác thực bởi NewsRoom • Đơn vị: [Tên cơ quan]".

### Story 3.4: Infinite Scroll, Skeleton Loading & Dual Empty States

As a người dùng lướt tin trên điện thoại hoặc máy tính,  
I want trang tự động tải thêm tin khi cuộn và phân biệt rõ ràng các trường hợp không có bài viết,  
So that tôi không bị bối rối khi dòng tin trống.

**Acceptance Criteria:**

**Given** người dùng đang cuộn xem danh sách tin trên Feed  
**When** vị trí cuộn đạt 80% chiều dài trang  
**Then** frontend kích hoạt gọi trang dữ liệu tiếp theo (`useInfiniteQuery` của TanStack React Query) và nối tiếp mượt mà vào danh sách  
**And** trong lần mở app đầu tiên (Cold load), hiển thị 4 hàng Skeleton Ant Design mô phỏng chính xác cấu trúc bài viết thay vì spinner xoay  
**And** **Trường hợp Empty 1 (Chưa follow ai)**: hiển thị Empty State gợi ý 3 nhà báo nổi bật kèm nút "+ Theo dõi" ngay tại chỗ  
**And** **Trường hợp Empty 2 (Đã follow nhưng hôm nay họ chưa đăng bài mới)**: hiển thị thông báo: *"Bạn đã đọc hết tin mới từ những người đang theo dõi! Hãy khám phá thêm bài viết nóng tại tab Dành cho bạn"* kèm nút bấm chuyển tab nhanh.

---

## Epic 4: Social Interactions & Network Growth

Cung cấp toàn bộ các tính năng tương tác xã hội (Like, Comment, Share) và cơ chế xây dựng mạng lưới độc giả một chiều (Follow/Unfollow) với bảo vệ toàn vẹn dữ liệu.

### Story 4.1: Post Like & Unlike Toggle with Debounce & Non-negative Atomic Counter

As a người dùng đọc tin,  
I want bấm thích hoặc bỏ thích một bài viết một cách mượt mà không bị lỗi số đếm âm khi mạng lag,  
So that tôi bày tỏ sự đồng tình mà dữ liệu luôn chính xác tuyệt đối.

**Acceptance Criteria:**

**Given** người dùng đã đăng nhập đang xem bài viết  
**When** người dùng click vào nút icon Thích trên chân thẻ bài viết  
**Then** frontend thực hiện Debounce 300ms (nếu người dùng bấm like/unlike liên tục thì chỉ gửi trạng thái cuối cùng)  
**And** backend ghi nhận bản ghi vào collection `interactions`: `{ userId, postId, type: "LIKE" }` và chạy câu lệnh atomic MongoDB:  
Khi like: `$inc: { likeCount: 1 }`  
Khi unlike: `$inc: { likeCount: -1 }` kết hợp điều kiện query `{ likeCount: { $gt: 0 } }` đảm bảo `likeCount` tuyệt đối không bao giờ bị âm (< 0)  
**And** một người dùng tuyệt đối không thể like trùng 2 lần trên cùng một bài viết nhờ Unique Compound Index `{ userId: 1, postId: 1, type: 1 }`.

### Story 4.2: Text Comments on Posts

As a thành viên cộng đồng,  
I want viết bình luận văn bản dưới bài viết,  
So that tôi đóng góp góc nhìn hoặc bổ sung tình tiết mới cho sự kiện tin tức.

**Acceptance Criteria:**

**Given** người dùng mở xem chi tiết bài viết tại `/posts/{id}` hoặc bấm nút Bình luận trên card  
**When** người dùng nhập nội dung bình luận (tối đa 2000 ký tự) và bấm "Gửi bình luận"  
**Then** backend lưu bản ghi vào collection `comments`, tăng nguyên tử `commentCount` trên Post (+1)  
**And** bình luận mới xuất hiện ngay lập tức ở đầu danh sách bình luận với đầy đủ avatar, tên người viết và thời gian gửi  
**And** tác giả của bình luận có quyền xóa bình luận của mình (giảm `commentCount` đi -1); Admin có quyền xóa bất kỳ bình luận nào vi phạm quy chuẩn.

### Story 4.3: Internal Post Sharing

As a người dùng thấy một bài viết hay,  
I want chia sẻ bài viết đó lên trang cá nhân của tôi,  
So that những người đang theo dõi tôi cũng có thể tiếp cận được thông tin giá trị này.

**Acceptance Criteria:**

**Given** bài viết có chế độ hiển thị `PUBLIC`  
**When** người dùng click nút "Chia sẻ" và xác nhận "Chia sẻ lên trang cá nhân"  
**Then** hệ thống tạo một bản ghi chia sẻ tham chiếu (reference post) trong feed của người dùng đó với dòng tiêu đề: "[Tên người chia sẻ] đã chia sẻ bài viết"  
**And** bộ đếm `shareCount` trên bài viết gốc tăng nguyên tử +1  
**And** không cho phép chia sẻ các bài viết có phạm vi `PRIVATE` hoặc `FOLLOWERS_ONLY`.

### Story 4.4: Follow & Unfollow Authors

As a độc giả,  
I want bấm theo dõi một tác giả hoặc nhà báo viết bài hay,  
So that tôi không bỏ lỡ các tin tức mới của họ trong tương lai.

**Acceptance Criteria:**

**Given** người dùng xem thẻ bài viết hoặc trang cá nhân của một tác giả khác  
**When** người dùng click nút "+ Theo dõi" (Follow)  
**Then** backend tạo bản ghi trong collection `follows`: `{ followerId, followingId }`, tăng `followingCount` của người bấm (+1) và tăng `followersCount` của tác giả (+1)  
**And** nút bấm lập tức đổi trạng thái thành "Đang theo dõi" (Following) với màu nền nhạt  
**And** nếu người dùng click lại để hủy theo dõi (Unfollow), bản ghi trong `follows` bị xóa và các bộ đếm giảm tương ứng (-1)  
**And** người dùng không thể tự follow tài khoản của chính mình.

### Story 4.5: Followers and Following Lists

As a người dùng,  
I want xem danh sách những người đang theo dõi tôi và những người tôi đang theo dõi,  
So that tôi quản lý được mạng lưới quan hệ xã hội của mình trên nền tảng.

**Acceptance Criteria:**

**Given** người dùng click vào con số "Người theo dõi" hoặc "Đang theo dõi" trên trang Profile  
**When** modal danh sách người dùng mở ra  
**Then** hiển thị danh sách gồm: Avatar, Tên hiển thị, Huy hiệu nhà báo (nếu có), Bio ngắn và nút hành động Follow/Unfollow nhanh  
**And** danh sách hỗ trợ phân trang mượt mà (20 người mỗi trang)  
**And** thao tác bấm Follow/Unfollow trực tiếp trong danh sách có hiệu lực ngay lập tức.

---

## Epic 5: Content Trust & Moderation

Xây dựng cơ chế lọc từ khóa tự động chống teencode, báo cáo vi phạm cộng đồng có bảo vệ Nhà báo và công cụ xử lý dành cho Quản trị viên.

### Story 5.1: Normalized Keyword Pre-save Auto-scan

As a quản trị viên hệ thống,  
I want nền tảng chuẩn hóa văn bản và tự động kiểm tra từ khóa cấm/nhạy cảm ngay khi người dùng đăng bài,  
So that tin tức có nội dung kích động, bạo lực hoặc cố tình lách luật bằng teencode không bao giờ lọt lên Public Feed.

**Acceptance Criteria:**

**Given** người dùng bấm "Đăng tin" hoặc "Lưu chỉnh sửa" một bài viết  
**When** request đến `PostService` tại backend  
**Then** hệ thống thực hiện tiền xử lý văn bản: chuyển về chữ thường, loại bỏ các ký tự phân cách cố ý (dấu chấm, gạch dưới, khoảng trắng thừa như `c.ờ b.ạ.c`, `d_á_n_h b_à_i`) và unidecode  
**And** `KeywordFilterService` đối chiếu chuỗi đã chuẩn hóa với danh sách từ khóa cấm trong thời gian < 10ms  
**And** nếu phát hiện chứa từ khóa cấm: bài viết được lưu với trạng thái `status = FLAGGED_PENDING_REVIEW` và phản hồi mã 201 kèm thông báo: "Bài viết chứa từ khóa cần duyệt và đang chờ ban biên tập xem xét"  
**And** bài viết bị gắn cờ chỉ hiển thị riêng cho tác giả trong danh sách bài của tôi, hoàn toàn bị ẩn khỏi Public Feed và Personal Feed của người khác.

### Story 5.2: Community Report Post Modal & Journalist Anti-brigading Protection

As a độc giả phát hiện bài viết có dấu hiệu sai sự thật hoặc vi phạm bản quyền,  
I want báo cáo bài viết đó lên ban quản trị, đồng thời hệ thống có cơ chế bảo vệ nhà báo khỏi bị tấn công report bẩn,  
So that tin tức chính thống không bị phá hoại bởi các chiến dịch ác ý.

**Acceptance Criteria:**

**Given** người dùng click vào mục "Báo cáo vi phạm" trên menu ••• của một bài viết  
**When** hộp thoại `ReportModal` mở ra, người dùng chọn 1 trong 4 lý do (Tin giả/Sai sự thật, Ngôn từ xúc phạm, Spam/Quảng cáo rác, Vi phạm bản quyền), nhập ghi chú thêm và bấm "Gửi báo cáo"  
**Then** backend ghi nhận bản ghi vào collection `reports` và tăng `reportsCount` trên Post (+1); mỗi tài khoản chỉ được báo cáo 1 bài viết duy nhất 1 lần  
**And** **Quy tắc Người dùng thông thường**: Khi bài viết của User thường đạt `reportsCount >= 3` từ 3 tài khoản khác nhau, bài viết tự động chuyển `status = FLAGGED` và tạm ẩn khỏi feed công cộng  
**And** **Quy tắc Nhà báo xác thực (Anti-brigading)**: Nếu tác giả bài viết có `isJournalistVerified = true`, bài viết **tuyệt đối KHÔNG bị tự động ẩn**, mà hệ thống tạo một thông báo khẩn cấp (High Priority Alert) đẩy thẳng lên đầu hàng đợi Admin để duyệt thủ công.

### Story 5.3: Admin Moderation Queue & Content Actions

As an quản trị viên NewsRoom,  
I want xem danh sách các bài viết bị gắn cờ và thực hiện hành động kiểm duyệt (Duyệt, Xóa, Cảnh cáo),  
So that tôi duy trì được tính chuẩn mực và kỷ cương cho toàn bộ nền tảng.

**Acceptance Criteria:**

**Given** Admin đăng nhập vào trang quản trị `/admin/moderation`  
**When** mở tab "Hàng đợi kiểm duyệt" (Moderation Queue)  
**Then** hiển thị danh sách tất cả các bài viết có trạng thái `FLAGGED_PENDING_REVIEW` hoặc `FLAGGED`, kèm lý do bị flag (dính từ khóa nào hoặc số lượng report và lý do report)  
**And** với mỗi bài viết, Admin có thể bấm:  
  - **Duyệt bài (Approve)**: Cập nhật `status = PUBLISHED`, bài xuất hiện bình thường trên Feed.  
  - **Gỡ bài viết (Remove)**: Cập nhật `status = DELETED`, gửi thông báo giải thích cho tác giả.  
  - **Khóa tài khoản (Ban)**: Khóa tài khoản người đăng nếu vi phạm nghiêm trọng.  
**And** mọi hành động của Admin được ghi lại vào log kiểm duyệt (`audit_logs`) phục vụ tra cứu.

---

## Epic 6: Monetization & Platform Administration

Khai thác doanh thu quảng cáo tự nhiên trong dòng tin và cung cấp bảng điều khiển quản trị số liệu nền tảng.

### Story 6.1: Enhanced Admin Dashboard Statistics

As an quản trị viên NewsRoom,  
I want xem bảng số liệu thống kê tổng quan về người dùng, bài viết và tương tác trong ngày,  
So that tôi nắm bắt được tốc độ tăng trưởng và mức độ gắn kết của cộng đồng tin tức.

**Acceptance Criteria:**

**Given** Admin truy cập trang chủ quản trị `/admin/dashboard`  
**When** dữ liệu thống kê được nạp  
**Then** hiển thị các thẻ chỉ số KPI chính: Tổng số người dùng (và số đăng ký mới hôm nay), Tổng số bài viết xuất bản, Tổng số lượt tương tác (Like, Comment, Share trong 24h qua)  
**And** hiển thị số lượng bài viết đang tồn đọng trong Hàng đợi kiểm duyệt (Moderation Queue Badge) để Admin kịp thời xử lý  
**And** biểu đồ đường thể hiện xu hướng lượng bài đăng và lượng truy cập theo 7 ngày gần nhất.

### Story 6.2: In-Feed Advertisement Injection & Display Card

As a ban quản trị muốn tạo doanh thu,  
I want các banner quảng cáo từ đối tác được hiển thị xen kẽ tự nhiên vào dòng tin tức theo tỷ lệ quy định,  
So that người đọc tiếp cận được thông tin sản phẩm mà không cảm thấy khó chịu.

**Acceptance Criteria:**

**Given** người dùng đang cuộn xem dòng tin tức trên Feed  
**When** danh sách bài viết được render  
**Then** frontend tự động chèn 1 thẻ quảng cáo `InFeedAdCard` sau mỗi 6 bài viết tin tức thông thường  
**And** thẻ quảng cáo hiển thị chuẩn mực: nhãn màu hổ phách "Được tài trợ", tên nhà quảng cáo, nội dung ngắn gọn, banner ảnh tỷ lệ chuẩn và nút bấm kêu gọi hành động CTA ("Tìm hiểu thêm")  
**And** quảng cáo không gây xô lệch khung cuộn trang và có tùy chọn menu ••• để người dùng bấm "Ẩn quảng cáo này" nếu không quan tâm.

### Story 6.3: Advertisement Impression & Click Tracking

As a bộ phận kinh doanh quảng cáo,  
I want hệ thống tự động ghi nhận số lượt hiển thị (Impressions) và số lượt click vào từng banner quảng cáo,  
So that chúng tôi có số liệu minh bạch để đối soát và báo cáo hiệu quả cho các đối tác tài trợ.

**Acceptance Criteria:**

**Given** thẻ quảng cáo xuất hiện trong vùng nhìn thấy của người dùng trên màn hình (Viewport)  
**When** quảng cáo hiển thị trọn vẹn tối thiểu 1 giây  
**Then** frontend tự động gửi request ngầm `POST /api/ads/{id}/impression` để backend tăng trường `impressionsCount` (+1)  
**And** khi người dùng click vào banner hoặc nút CTA, frontend gửi request `POST /api/ads/{id}/click` (tăng `clicksCount` +1) trước khi mở đường link đích của nhà tài trợ trong tab mới  
**And** Admin có thể theo dõi tỷ lệ click (CTR = clicks / impressions) của từng chiến dịch trong trang quản lý quảng cáo `/admin/advertisements`.
