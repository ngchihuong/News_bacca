---
title: "PRD: NewsRoom"
status: draft
created: 2026-09-14
updated: 2026-09-14
---

# PRD: NewsRoom

## 0. Document Purpose

Tài liệu PRD này phục vụ product owner (huong_dz), các downstream workflow (architecture, epics & stories, sprint planning, UX design), và bất kỳ collaborator tương lai nào. PRD được cấu trúc theo BMad Method: Glossary-anchored vocabulary, features grouped với FRs nested (numbered globally FR-1 → FR-N), assumptions tagged inline và indexed ở cuối. PRD này build trên [Product Brief](../briefs/brief-News-2026-09-14/brief.md) và [Brief Addendum](../briefs/brief-News-2026-09-14/addendum.md). Existing codebase v1.3.0 inventory nằm trong addendum — PRD không duplicate.

## 1. Vision

NewsRoom là nền tảng mạng xã hội tin tức cộng đồng nơi mọi người dùng đều có thể đăng tải, chia sẻ và tương tác với thông tin. Khác biệt cốt lõi: hai luồng nội dung trong một feed thống nhất — tin tức cộng đồng (ai cũng đăng được) và tin tức nhà báo xác thực (badge ✅). Platform tập trung 100% vào tin tức, không phải mạng xã hội đa mục đích.

Doanh thu từ hệ thống quảng cáo tích hợp (banner, in-feed, sidebar — base đã có, admin quản lý). Thị trường Việt Nam trước, mở rộng quốc tế sau. Sản phẩm mang mục tiêu kép: sân chơi công nghệ để học tập và áp dụng kỹ thuật hiện đại, đồng thời là sản phẩm kiếm tiền thực.

Nền tảng kỹ thuật hiện tại (Spring Boot 3.2 + Next.js 14 + MongoDB, v1.3.0) đã có cấu trúc vững với 47+ API endpoints, Admin Panel, JWT Auth, và Advertisement System. Bước tiếp theo là chuyển từ CMS truyền thống sang social platform đầy đủ.

## 2. Target User

### 2.1 Jobs To Be Done

- **Functional:** Tôi muốn đăng tin tức / thông tin và có người đọc, tương tác — nhanh, không rào cản
- **Functional:** Tôi muốn đọc tin tức từ nhiều nguồn (cộng đồng + nhà báo) trong một nơi duy nhất, tùy chỉnh theo sở thích
- **Functional:** Tôi muốn biết tin nào đáng tin cậy nhờ badge nhà báo xác thực
- **Social:** Tôi muốn xây dựng audience theo dõi nội dung của mình
- **Social:** Tôi muốn tương tác (like, comment, share) với cộng đồng quan tâm cùng chủ đề
- **Emotional:** Tôi muốn cảm giác nội dung của mình được trân trọng, không bị chìm trong noise
- **Contextual (builder):** Tôi muốn build platform này như bài tập công nghệ nghiêm túc, có giá trị portfolio và kiếm tiền

### 2.2 Non-Users (v1)

- Nhà quảng cáo tự phục vụ (self-service advertisers) — V1 ads do admin quản lý
- Nhà báo chuyên nghiệp (journalist verification là V2)
- Người dùng tìm kiếm messaging/chat cá nhân
- Người dùng tìm kiếm video hosting / live streaming

### 2.3 Key User Journeys

**UJ-1. Minh đăng tin sự kiện địa phương vừa chứng kiến.**
- **Persona + context:** Minh, 28 tuổi, nhân viên văn phòng ở Hà Nội, vừa chứng kiến tai nạn giao thông ở ngã tư gần công ty.
- **Entry state:** Đã đăng nhập trên điện thoại, đang ở Home Feed.
- **Path:** Nhấn nút "Đăng bài" → chọn category "Thời sự" → viết tiêu đề + nội dung → đính kèm 2 ảnh chụp hiện trường → chọn visibility "Public" → nhấn "Đăng".
- **Climax:** Bài viết xuất hiện trên feed ngay lập tức. Trong 30 phút có 5 likes và 3 comments từ người dùng cùng khu vực.
- **Resolution:** Minh thấy notification tương tác, mở bài xem comments, trả lời một comment. Quay lại feed đọc tin khác.
- **Edge case:** Nếu auto-scan phát hiện từ khóa nhạy cảm, bài bị flag — Minh nhận thông báo "Bài viết đang chờ duyệt" thay vì publish ngay.

**UJ-2. Lan cuộn feed buổi sáng tìm tin đáng đọc.**
- **Persona + context:** Lan, 35 tuổi, mẹ hai con, đọc tin mỗi sáng trên xe bus đi làm.
- **Entry state:** Đã đăng nhập, mở app thấy Personal Feed.
- **Path:** Cuộn feed → thấy mix bài từ người đang follow + trending → tap vào bài "Giá xăng tăng hôm nay" của một user có badge nhà báo ✅ → đọc xong → like bài → scroll xuống đọc comments → share bài lên feed cá nhân.
- **Climax:** Lan tìm được 3 bài đáng đọc trong 10 phút. Feed cá nhân khác biệt rõ so với lần trước nhờ follow mới.
- **Resolution:** Lan follow thêm 1 tác giả viết hay, đóng app. Ngày mai feed cập nhật với bài mới từ tác giả đó.

**UJ-3. Admin Hương duyệt bài bị report và quản lý ads.**
- **Persona + context:** Hương, admin và founder của NewsRoom, kiểm tra platform mỗi tối.
- **Entry state:** Đăng nhập admin panel, vào Dashboard.
- **Path:** Thấy notification "3 bài viết bị report" → vào Content Moderation → đọc bài bị report + lý do report → quyết định: xóa 1 bài spam, cảnh cáo 1 user, giữ lại 1 bài (false report) → chuyển sang Ad Management → kiểm tra CTR campaigns → tạo ad mới cho vị trí sidebar.
- **Climax:** Platform sạch sẽ, ads campaign mới live.
- **Resolution:** Dashboard cập nhật stats. Hương logout.

## 3. Glossary

- **Post** — Một bài viết do User tạo. Chứa title, content (text + images), category, tags, visibility setting. Thuộc về đúng một Author.
- **Author** — User đã tạo ít nhất một Post. Có thể là User thường hoặc Journalist.
- **Feed** — Danh sách Posts được sắp xếp và cá nhân hóa. **Personal Feed** hiển thị Posts từ Followed Users + Trending. **Public Feed** (Home) hiển thị tất cả Public Posts.
- **User** — Người dùng đã đăng ký tài khoản. Có profile, có thể Post, Follow, tương tác.
- **Guest** — Người truy cập chưa đăng ký/đăng nhập. Chỉ xem Public Posts, không tương tác.
- **Journalist** — User được admin cấp badge xác thực nhà báo ✅. Có tất cả quyền User + badge hiển thị trên Posts. (V2)
- **Admin** — Quản trị viên platform. Quản lý Users, duyệt Journalist, xử lý vi phạm, quản lý Ads.
- **Interaction** — Hành động tương tác trên Post: Like, Comment, hoặc Share. Mỗi Interaction thuộc về đúng một User và một Post.
- **Like** — Interaction thể hiện đồng ý/thích. Mỗi User chỉ like một Post một lần (toggle).
- **Comment** — Interaction chứa text response trên Post. Thuộc về một User, gắn với một Post.
- **Share** — Interaction chia sẻ Post lên Personal Feed của User, tạo bản tham chiếu (không duplicate content).
- **Follow** — Quan hệ một chiều: User A follow User B. Posts từ B xuất hiện trong Personal Feed của A.
- **Visibility** — Quyền xem Post: **Public** (mọi người kể cả Guest), **Followers-only** (chỉ Followers của Author), **Private** (chỉ Author).
- **Report** — Hành động User gửi báo cáo vi phạm về một Post. Admin review và xử lý.
- **Auto-scan** — Hệ thống tự động quét Post khi đăng, phát hiện nội dung vi phạm bằng keyword/pattern matching.
- **Flag** — Trạng thái Post bị Auto-scan hoặc Report đánh dấu nghi ngờ vi phạm, chờ Admin review.
- **Category** — Phân loại nội dung (Thời sự, Công nghệ, Thể thao, Giải trí, v.v.). Mỗi Post thuộc một Category.
- **Tag** — Nhãn tự do gắn vào Post để phân loại chi tiết hơn Category. Một Post có nhiều Tags.
- **Advertisement (Ad)** — Nội dung quảng cáo hiển thị trên platform. Có position (sidebar, banner, in-feed), format (image, HTML, script), tracking (impressions, clicks). Admin quản lý.

## 4. Features

### 4.1 User Authentication & Profile Management

**Description:** Mở rộng hệ thống auth hiện có (chỉ admin JWT) thành hệ thống đa vai trò cho User thường. User đăng ký bằng email/password, đăng nhập, quản lý profile cá nhân (avatar, bio, display name). Đây là foundation cho mọi social feature — không có Auth thì không có Feed, UGC, hay Interactions. Realizes UJ-1, UJ-2, UJ-3.

`[ASSUMPTION: Đăng ký bằng email/password trước. Social login (Google, Facebook) là V2+]`

`[ASSUMPTION: Email verification required khi đăng ký để giảm spam accounts]`

**Functional Requirements:**

#### FR-1: User Registration

User chưa đăng ký can tạo tài khoản mới bằng email, password, display name. Realizes UJ-1.

**Consequences (testable):**
- System tạo User mới trong database với role USER, trạng thái active
- System gửi email verification link `[ASSUMPTION: sử dụng SMTP service]`
- User không thể đăng nhập cho đến khi verify email `[ASSUMPTION: có thể cho phép login ngay nhưng hạn chế posting cho đến khi verify]`
- Email phải unique — system trả lỗi nếu email đã tồn tại
- Password phải ≥ 8 ký tự, hash bằng BCrypt

#### FR-2: User Login / Logout

User đã đăng ký can đăng nhập bằng email/password và nhận JWT token.

**Consequences (testable):**
- System trả JWT access token (expire 24h) và refresh token (expire 7 ngày) `[ASSUMPTION: token duration]`
- JWT payload chứa userId, role, displayName
- Logout invalidate token phía client (clear localStorage) `[ASSUMPTION: không blacklist token phía server cho V1]`
- Failed login 5 lần liên tiếp → lock account 15 phút `[ASSUMPTION: rate limiting strategy]`

#### FR-3: User Profile Management

User đã đăng nhập can xem và chỉnh sửa profile cá nhân. Realizes UJ-1, UJ-2.

**Consequences (testable):**
- Profile gồm: display name, avatar (upload image), bio (max 500 chars), join date
- User có thể xem profile của User khác (public view: display name, avatar, bio, post count, follower count)
- Profile URL: `/user/{username}` `[ASSUMPTION: username unique, generated từ display name hoặc user chọn]`

**Feature-specific NFRs:**
- Avatar upload ≤ 2MB, format: jpg/png/webp
- Profile page load < 2 giây

---

### 4.2 Personal Feed

**Description:** Trải nghiệm trung tâm của platform. Khi User đăng nhập, Feed hiển thị Posts từ Followed Users + Trending Posts. Guest thấy Public Feed (trending + latest). Feed là thứ đầu tiên user thấy — nó phải hấp dẫn ngay cả khi user chưa follow ai (cold-start: seed trending/latest). Realizes UJ-2.

**Functional Requirements:**

#### FR-4: Personal Feed Generation

User đã đăng nhập can xem Personal Feed là mix của Posts từ Followed Users và Trending Posts. Realizes UJ-2.

**Consequences (testable):**
- Feed sắp xếp theo thời gian (newest first) `[ASSUMPTION: không có recommendation algorithm cho V1, chỉ chronological + trending mix]`
- Feed hiển thị: Post title, excerpt (100 chars), author avatar + name, category, timestamp, like/comment/share counts, thumbnail image (nếu có)
- Infinite scroll hoặc pagination (20 posts/page) `[ASSUMPTION: infinite scroll cho UX tốt hơn]`
- Nếu user chưa follow ai → Feed hiển thị Trending + Latest Public Posts

#### FR-5: Public Feed (Home)

Guest hoặc User can xem Public Feed trên trang chủ.

**Consequences (testable):**
- Public Feed hiển thị tất cả Public Posts, sắp xếp theo combination trending score + recency
- Trending score = f(likes, comments, shares, views, recency) `[ASSUMPTION: formula cụ thể quyết định ở architecture]`
- Guest có thể đọc full Post nhưng không thể like/comment/share — hiển thị CTA "Đăng ký để tương tác"

---

### 4.3 User-Generated Content (UGC)

**Description:** Tính năng cốt lõi biến NewsRoom từ CMS thành social platform. Mọi User đều có thể tạo Post — viết bài, đính kèm ảnh, chọn Category, gắn Tags, chọn Visibility. Post được publish ngay (trừ khi bị Auto-scan flag). User quản lý Posts của mình (edit, delete, thay đổi visibility). Realizes UJ-1.

**Functional Requirements:**

#### FR-6: Create Post

User đã đăng nhập can tạo Post mới với title, content, images, category, tags, và visibility. Realizes UJ-1.

**Consequences (testable):**
- Post gồm: title (required, max 200 chars), content (required, rich text `[ASSUMPTION: markdown hoặc rich text editor — quyết định ở architecture]`), images (optional, max 5 ảnh `[ASSUMPTION]`, mỗi ảnh ≤ 5MB), category (required, chọn từ danh sách), tags (optional, max 10), visibility (required, default Public)
- System tự generate slug từ title
- Post được publish ngay trừ khi Auto-scan flag → status "pending_review"
- Post có created_at, updated_at timestamps
- Author có thể xem danh sách tất cả Posts của mình (published, pending, draft)

#### FR-7: Edit Post

Author can chỉnh sửa Post đã tạo (title, content, images, category, tags, visibility).

**Consequences (testable):**
- Chỉ Author hoặc Admin có thể edit Post
- updated_at cập nhật khi edit
- Nếu Post đã bị flag, edit trigger lại Auto-scan

#### FR-8: Delete Post

Author can xóa Post đã tạo.

**Consequences (testable):**
- Soft delete — Post bị ẩn khỏi Feed nhưng vẫn trong database `[ASSUMPTION: soft delete cho content recovery]`
- Tất cả Interactions (likes, comments, shares) liên quan vẫn giữ nhưng ẩn theo
- Admin có thể hard delete

#### FR-9: Draft Posts

User can lưu Post ở trạng thái draft trước khi publish.

**Consequences (testable):**
- Draft Posts chỉ Author xem được
- User có thể resume editing draft và publish khi sẵn sàng

---

### 4.4 Social Interactions

**Description:** Hệ thống tương tác xã hội: Like, Comment, Share. Đây là lớp engagement biến platform từ "đọc" thành "tham gia". Mỗi Interaction tạo notification cho Author. Interaction counts hiển thị trên Post card trong Feed. Realizes UJ-1, UJ-2.

**Functional Requirements:**

#### FR-10: Like Post

User đã đăng nhập can like/unlike một Post. Realizes UJ-2.

**Consequences (testable):**
- Like là toggle — nhấn lần 1 = like, nhấn lần 2 = unlike
- Like count hiển thị real-time trên Post
- Author nhận notification khi có like mới `[ASSUMPTION: notification in-app, không push notification V1]`
- User không thể like Post của chính mình `[ASSUMPTION]`

#### FR-11: Comment on Post

User đã đăng nhập can viết Comment trên một Public hoặc Followers-only Post (nếu đang follow Author).

**Consequences (testable):**
- Comment chứa text (max 2000 chars `[ASSUMPTION]`), created_at, author
- Comments hiển thị chronological dưới Post
- `[ASSUMPTION: V1 không có nested/reply comments — flat list. Nested replies là V2+]`
- Author nhận notification khi có comment mới
- Comment author có thể delete comment của mình
- Admin có thể delete bất kỳ comment nào

#### FR-12: Share Post

User đã đăng nhập can share một Public Post. Realizes UJ-2.

**Consequences (testable):**
- Share tạo một reference entry trên Personal Feed của User (hiển thị "User X shared Post Y")
- Share count tăng trên original Post
- `[ASSUMPTION: V1 chỉ share nội bộ platform. Share ra Facebook/Twitter/Zalo là V2+]`
- Không thể share Post có visibility Private hoặc Followers-only

---

### 4.5 Follow System

**Description:** Quan hệ một chiều giữa Users. Follow User B → Posts của B xuất hiện trong Personal Feed của A. Follow là nền tảng của cá nhân hóa feed. Realizes UJ-2.

**Functional Requirements:**

#### FR-13: Follow/Unfollow User

User đã đăng nhập can follow hoặc unfollow User khác. Realizes UJ-2.

**Consequences (testable):**
- Follow là toggle — nhấn Follow/Unfollow
- Sau follow, Posts mới từ Followed User xuất hiện trong Personal Feed
- Follower count và Following count hiển thị trên User Profile
- User không thể follow chính mình
- `[ASSUMPTION: không có approval required — follow ngay lập tức, không kiểu "follow request"]`

#### FR-14: Follower/Following Lists

User can xem danh sách Followers và Following của bất kỳ User nào.

**Consequences (testable):**
- Danh sách hiển thị avatar, display name, bio excerpt, follow/unfollow button
- Paginated (20 users/page)

---

### 4.6 Content Visibility Control

**Description:** User kiểm soát ai xem được Post của mình. Ba mức: Public, Followers-only, Private. Đây là tính năng tạo trust — user tin tưởng platform khi họ có quyền kiểm soát nội dung. Realizes UJ-1.

**Functional Requirements:**

#### FR-15: Set Post Visibility

Author can chọn Visibility khi tạo Post và thay đổi sau khi publish. Realizes UJ-1.

**Consequences (testable):**
- Ba options: Public (default), Followers-only, Private
- Thay đổi visibility có hiệu lực ngay lập tức
- Public → Followers-only: Post biến mất khỏi Public Feed, chỉ còn trong Followers' Feeds
- Followers-only → Private: Post biến mất khỏi tất cả Feeds trừ Author's profile
- Interactions trên Post vẫn giữ khi thay đổi visibility
- Guest chỉ thấy Public Posts — khi truy cập URL của Followers-only/Private Post → hiển thị "Nội dung không khả dụng"

---

### 4.7 Content Moderation

**Description:** Hệ thống giữ platform sạch với hai lớp: Auto-scan (tự động quét keywords/patterns khi đăng bài) và Community Report (user báo cáo vi phạm). Admin review và xử lý. Đây là tính năng critical cho trust và chất lượng platform. Realizes UJ-3.

**Functional Requirements:**

#### FR-16: Auto-scan on Publish

System tự động quét Post khi user nhấn publish, phát hiện nội dung vi phạm.

**Consequences (testable):**
- Quét title và content against danh sách keywords/patterns vi phạm `[ASSUMPTION: keyword blacklist do admin configure. V1 chưa dùng AI/ML — chỉ keyword matching]`
- Nếu match → Post status = "pending_review", không publish lên Feed
- Author nhận thông báo "Bài viết đang chờ duyệt"
- Admin thấy flagged Posts trong Moderation Dashboard
- False positive: Admin approve → Post publish bình thường

#### FR-17: Community Report

User đã đăng nhập can report một Post vi phạm.

**Consequences (testable):**
- Report form: chọn lý do (spam, nội dung sai sự thật, ngôn từ xúc phạm, khác) + optional comment
- Mỗi User chỉ report mỗi Post một lần
- Post bị ≥ 3 reports `[ASSUMPTION: threshold configurable bởi admin]` → tự động flag cho admin review
- Admin thấy report count + lý do trong Moderation Dashboard

#### FR-18: Admin Moderation Actions

Admin can review và xử lý flagged/reported Posts. Realizes UJ-3.

**Consequences (testable):**
- Actions: Approve (unflag, publish), Remove (hide post), Warn User (gửi warning), Ban User (disable account `[ASSUMPTION: temporary ban X ngày, permanent ban riêng]`)
- Action được log với lý do
- Author nhận notification về kết quả moderation

---

### 4.8 Existing Features (Maintain & Integrate)

**Description:** Các features đã có trong codebase v1.3.0 cần được duy trì và tích hợp vào kiến trúc mới. Không build lại từ đầu — refactor để fit social platform model.

**Functional Requirements:**

#### FR-19: Admin Dashboard (Existing — Enhance)

Admin can xem tổng quan platform stats trên Dashboard. Realizes UJ-3.

**Consequences (testable):**
- Dashboard hiện có: Total/Published/Draft/Featured news stats
- **Enhance:** thêm User stats (total users, new today, active today), Interaction stats (likes, comments, shares today), Moderation queue count, Ad performance summary

#### FR-20: Advertisement System (Existing — Maintain)

Admin can quản lý Advertisements. Ads hiển thị trên public pages.

**Consequences (testable):**
- Giữ nguyên: CRUD ads, 6 positions (sidebar top/mid/bottom, top/bottom banner, in-feed), 3 formats (image, HTML, script), impression/click tracking, scheduling, priority
- **Integrate:** Ads hiển thị trong Social Feed (in-feed position: mỗi 10 Posts `[ASSUMPTION: frequency giữ nguyên]`), Sidebar trên Post detail page
- Google AdSense / Facebook Ads integration giữ nguyên

#### FR-21: Category & Tag System (Existing — Maintain)

Admin can quản lý Categories. Users can chọn Category và Tags khi tạo Post. Realizes UJ-1.

**Consequences (testable):**
- Giữ nguyên Category CRUD API
- Users thấy Category list khi tạo Post
- Users có thể tạo Tags mới hoặc chọn existing Tags
- Category page (`/category/{slug}`) hiển thị Posts filtered by category

#### FR-22: File Upload (Existing — Maintain)

Users can upload images khi tạo Post hoặc chỉnh sửa Profile. Realizes UJ-1.

**Consequences (testable):**
- Giữ nguyên upload API
- Supported: jpg, png, webp, gif
- Max size per file: 5MB `[ASSUMPTION]`
- Files stored locally `[ASSUMPTION: V1 local storage. Cloud storage (S3/GCS) là V2+]`

## 5. Non-Goals (Explicit)

- NewsRoom **không phải** mạng xã hội đa mục đích — không có marketplace, gaming, dating, hay video hosting
- NewsRoom **không cạnh tranh trực tiếp** với Facebook/Zalo — focus vào niche tin tức
- V1 **không có** messaging/chat cá nhân giữa users
- V1 **không có** live streaming hoặc video upload (chỉ embed YouTube/TikTok)
- V1 **không có** payment/subscription/paywall — tất cả nội dung miễn phí
- V1 **không có** recommendation algorithm nâng cao — feed chronological + trending đơn giản
- V1 **không có** mobile app native — web responsive only
- V1 **không có** journalist verification system (V2)
- V1 **không có** self-service advertiser portal (admin quản lý ads)

## 6. MVP Scope

### 6.1 In Scope

- User registration (email/password), login/logout, profile management (FR-1, FR-2, FR-3)
- Personal Feed (chronological + trending mix) và Public Feed (FR-4, FR-5)
- User-generated posts: create, edit, delete, drafts (FR-6, FR-7, FR-8, FR-9)
- Social interactions: like, comment, share (FR-10, FR-11, FR-12)
- Follow/unfollow system (FR-13, FR-14)
- Post visibility control: public/followers-only/private (FR-15)
- Content moderation: auto-scan + community report + admin actions (FR-16, FR-17, FR-18)
- Admin dashboard enhancement (FR-19)
- Existing ad system integration into social feed (FR-20)
- Existing category/tag system reuse (FR-21)
- Existing file upload for posts and profiles (FR-22)

### 6.2 Out of Scope for MVP

- Social login (Google, Facebook, Zalo) — deferred to V2. `[NOTE FOR PM: Nếu user acquisition chậm, đây là feature nên bump lên sớm]`
- Journalist badge/verification system — deferred to V2
- Nested/reply comments — V2+
- Push notifications — V1 in-app only
- Share to external platforms (Facebook, Zalo, X) — V2+
- Search (full-text) — V2+ `[NOTE FOR PM: Search có thể cần sớm hơn dự kiến nếu content volume cao]`
- Real-time notifications (WebSocket) — V2+
- i18n / multi-language — V3+
- Mobile native app — V3+
- Cloud storage migration (S3) — V2+
- AI-powered content moderation — V2+
- Content scheduling — V3+

## 7. Success Metrics

**Primary**

- **SM-1:** First-post conversion rate — % users tạo Post đầu tiên trong 7 ngày sau đăng ký. Target: ≥ 30%. Validates FR-1, FR-6.
- **SM-2:** DAU/MAU ratio — % monthly active users quay lại hàng ngày. Target: ≥ 20%. Validates FR-4, FR-5.
- **SM-3:** Interactions per Post — trung bình (likes + comments + shares) per Post. Target: ≥ 2. Validates FR-10, FR-11, FR-12.

**Secondary**

- **SM-4:** Follow adoption — trung bình Following count per active User after 30 days. Target: ≥ 3. Validates FR-13.
- **SM-5:** Ad CTR — Click-through rate quảng cáo. Target: ≥ 0.5%. Validates FR-20.
- **SM-6:** Moderation response time — trung bình thời gian từ report đến admin action. Target: < 24 giờ. Validates FR-17, FR-18.
- **SM-7:** API p95 latency — 95th percentile response time. Target: < 500ms. Validates cross-cutting performance.

**Counter-metrics (do not optimize)**

- **SM-C1:** Post volume raw count — tổng số Posts không phải metric cần tối ưu. Tăng volume bằng cách hạ chất lượng (cho phép spam) sẽ phá hủy trust. Counterbalances SM-1.
- **SM-C2:** Time-on-platform — thời gian user ở trên platform. Không tối ưu "dính" — tối ưu "giá trị mỗi phiên". Counterbalances SM-2.

## 8. Open Questions

1. **Email service:** Dùng SMTP service nào cho email verification? (Gmail SMTP, SendGrid, Mailgun?) — Ảnh hưởng FR-1.
2. **Rich text editor:** Post content dùng Markdown hay WYSIWYG editor? — Ảnh hưởng FR-6 UX.
3. **Trending algorithm:** Formula cụ thể cho trending score? Weighting giữa likes, comments, shares, views, recency? — Ảnh hưởng FR-4, FR-5.
4. **Auto-scan keyword list:** Nguồn danh sách keywords vi phạm? Ai maintain? — Ảnh hưởng FR-16.
5. **File storage limit:** Tổng dung lượng storage trên server? Kế hoạch migrate cloud khi nào? — Ảnh hưởng FR-22.
6. **Seed content strategy:** Ai tạo nội dung ban đầu? Mời nhà báo quen? Tự viết? Import? — Ảnh hưởng cold-start risk.
7. **Username policy:** User chọn username khi đăng ký hay auto-generate từ display name? — Ảnh hưởng FR-3.

## 9. Assumptions Index

- **§4.1 FR-1:** Đăng ký bằng email/password trước, Social login V2+
- **§4.1 FR-1:** Email verification required khi đăng ký
- **§4.1 FR-1:** Sử dụng SMTP service cho email
- **§4.1 FR-1:** Có thể cho phép login ngay nhưng hạn chế posting cho đến khi verify
- **§4.1 FR-2:** Token duration: access 24h, refresh 7 ngày
- **§4.1 FR-2:** Không blacklist token phía server cho V1
- **§4.1 FR-2:** Rate limiting: lock after 5 failed attempts for 15 min
- **§4.1 FR-3:** Username unique, generated hoặc user chọn
- **§4.2 FR-4:** Không recommendation algorithm V1 — chronological + trending mix
- **§4.2 FR-4:** Infinite scroll thay vì pagination
- **§4.3 FR-6:** Rich text editor — quyết định ở architecture
- **§4.3 FR-6:** Max 5 images per post, mỗi ảnh ≤ 5MB
- **§4.3 FR-8:** Soft delete cho content recovery
- **§4.4 FR-10:** Notification in-app, không push notification V1
- **§4.4 FR-10:** User không thể like Post của chính mình
- **§4.4 FR-11:** Max 2000 chars per comment
- **§4.4 FR-11:** V1 flat comments, không nested/reply
- **§4.4 FR-12:** V1 share nội bộ platform only
- **§4.5 FR-13:** Follow ngay lập tức, không cần approval
- **§4.7 FR-16:** Keyword blacklist, không AI/ML cho V1
- **§4.7 FR-17:** Auto-flag threshold: ≥ 3 reports, configurable
- **§4.7 FR-18:** Temporary ban X ngày, permanent ban riêng
- **§4.8 FR-20:** In-feed ad frequency: mỗi 10 Posts
- **§4.8 FR-22:** Max file size 5MB
- **§4.8 FR-22:** V1 local storage, cloud V2+

---

## Adapt-In Sections

### Monetization

NewsRoom kiếm doanh thu qua hệ thống quảng cáo tích hợp:

- **V1 (hiện tại):** Admin quản lý ads. 6 positions, 3 formats, impression/click tracking. Google AdSense + Facebook Ads ready.
- **V2:** Self-service advertiser portal — advertisers tự tạo campaigns, chọn target audience, set budget.
- **Pricing model V1:** Không. Doanh thu = impressions + clicks từ ad networks (AdSense, FB Ads). Direct ads do admin negotiate offline.

Không có paywall, subscription, hay premium tier cho V1.

### Platform

- **V1:** Web responsive (desktop + mobile browser). Next.js 14 SSR.
- **V2+:** PWA capabilities (offline reading, install prompt)
- **V3+:** Mobile native app (React Native hoặc Flutter)

### Why Now

- Codebase v1.3.0 đã sẵn sàng — auth, admin, ads, API đều có. Momentum để extend, không phải build from scratch.
- Thị trường VN chưa có platform news social community sau khi Lotus đóng cửa — window of opportunity cho focused niche product.
- AI tools + BMad workflow accelerate development — solo developer có thể ship quality faster.

### Aesthetic and Tone

- **Visual:** Clean, news-focused. Màu chủ đạo Orange (#FF6600) + Blue, Tailwind CSS. Không flashy — tin tức cần readability.
- **Tone of voice:** Platform-generated text (notifications, empty states, error messages) dùng tiếng Việt thân thiện, ngắn gọn, hơi informal. Không formal kiểu "hệ thống". Giống cách một người bạn thông báo tin tức.
- **Anti-reference:** Không giống VnExpress (quá formal, quá dense). Không giống Facebook (quá cluttered, quá nhiều non-news).
