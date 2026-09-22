---
title: "Product Brief: NewsRoom"
status: draft
created: 2026-09-14
updated: 2026-09-14
---

# Product Brief: NewsRoom

## Executive Summary

NewsRoom là một **nền tảng mạng xã hội tin tức cộng đồng** — nơi mọi người đều có thể đăng tải, chia sẻ và tương tác với thông tin, còn nhà báo được xác thực bằng badge riêng để nâng cao uy tín nội dung. Đây không phải CMS truyền thống kiểu "admin viết — độc giả đọc"; đây là platform nơi cộng đồng tạo ra tin tức.

Sản phẩm nhắm đến thị trường Việt Nam trước, với tầm nhìn mở rộng quốc tế. Doanh thu đến từ hệ thống quảng cáo tích hợp (đã có base sẵn: banner, in-feed, sidebar, hỗ trợ Google AdSense/Facebook Ads). NewsRoom được build nghiêm túc với mục tiêu kép: là sân chơi công nghệ để khám phá và áp dụng các kỹ thuật hiện đại, đồng thời là sản phẩm có khả năng kiếm tiền thực.

Nền tảng kỹ thuật hiện tại (Spring Boot + Next.js + MongoDB, v1.3.0) đã có cấu trúc vững — API 47+ endpoints, Admin Panel, JWT Auth, Advertisement System. Bước tiếp theo là chuyển từ CMS sang social platform: thêm user-generated content, feed cá nhân, hệ thống tương tác, và mô hình nhà báo xác thực.

## The Problem

Người Việt Nam tiêu thụ tin tức qua hai kênh chính đang có vấn đề riêng:

**Mạng xã hội (Facebook, Zalo, TikTok):** Tin tức bị trộn lẫn với nội dung giải trí, quảng cáo, và thông tin cá nhân. Không có cách phân biệt tin đáng tin cậy với tin giả. Thuật toán ưu tiên engagement chứ không ưu tiên chất lượng thông tin.

**Báo truyền thống (VnExpress, Dân trí, Tuổi trẻ):** Một chiều — tòa soạn viết, độc giả đọc. Cộng đồng chỉ có thể comment, không thể đóng góp nội dung. Không có cơ chế cho citizen journalism hay thông tin cộng đồng.

**Khoảng trống:** Chưa có platform nào kết hợp được sức mạnh cộng đồng của mạng xã hội với tính uy tín và cấu trúc của báo chí. Người dùng phải chọn giữa "tự do nhưng hỗn loạn" và "uy tín nhưng thụ động".

## The Solution

NewsRoom kết hợp hai luồng thông tin trong một feed thống nhất:

**1. Tin tức cộng đồng (Community Content — ưu tiên cao):** Mọi người dùng đều có thể đăng bài, chia sẻ thông tin, đóng góp góc nhìn. Giống cách Facebook cho phép ai cũng đăng bài — nhưng trên nền tảng được thiết kế cho tin tức, không phải ảnh selfie.

**2. Tin tức nhà báo (Journalist Content):** Nhà báo xác thực được gắn badge ✅, nội dung của họ xuất hiện trong cùng feed nhưng nổi bật hơn. Người dùng có thể filter "chỉ xem từ nhà báo" khi cần thông tin có kiểm chứng.

**Trải nghiệm cốt lõi:**
- Đăng bài tự do — nhanh, không rào cản
- Feed cá nhân — nội dung theo sở thích, từ người đang follow
- Tương tác xã hội — like, comment, share
- Kiểm soát quyền riêng — public, private, hoặc chỉ followers
- Kiểm duyệt cộng đồng — report vi phạm + quét tự động
- Follow/unfollow — xây dựng mạng lưới thông tin cá nhân

## What Makes This Different

**Honest assessment — lợi thế cạnh tranh ở đây là tập trung và tốc độ thực thi, không phải "moat" công nghệ:**

- **Focus vào tin tức:** Không phải "mạng xã hội làm mọi thứ" — mọi tính năng, thuật toán, và UX đều phục vụ một mục đích: tiêu thụ và tạo ra tin tức tốt hơn
- **Mô hình hai tầng (Community + Journalist):** Cộng đồng tạo volume và đa dạng góc nhìn; nhà báo xác thực tạo lớp tin cậy. Không platform nào ở VN đang làm chính xác combo này
- **Codebase sẵn sàng:** Không bắt đầu từ zero — base v1.3.0 đã có auth, admin, ads, API. Đây là lợi thế thời gian thực

**Điều chưa phải lợi thế:** Platform mới đối mặt bài toán cold-start (chưa có người dùng → chưa có nội dung → chưa hấp dẫn). Cần chiến lược nội dung ban đầu — seed data hoặc đối tác nội dung.

## Who This Serves

### Người dùng chính (Primary)

**Người dùng cộng đồng (Community Users):** Bất kỳ ai muốn đăng tải và đọc thông tin — từ tin địa phương, sự kiện, đến góc nhìn cá nhân. Họ muốn một nơi đăng tin mà không bị chìm trong biển selfie và video hài. Thành công = bài viết được đọc, được tương tác, được follow.

**Độc giả (Readers):** Những người tiêu thụ tin tức hàng ngày, muốn feed cá nhân hóa với mix giữa tin cộng đồng và tin từ nhà báo uy tín. Thành công = tìm được thông tin cần thiết nhanh, tin tưởng được nguồn.

### Người dùng phụ (Secondary)

**Nhà báo xác thực (Verified Journalists):** Cần platform để mở rộng reach ngoài tòa soạn. Đăng ký → admin duyệt → nhận badge. Thành công = có audience trung thành, nội dung được ưu tiên hiển thị.

**Admin/Quản trị viên:** Quản lý platform, duyệt nhà báo, xử lý vi phạm, quản lý quảng cáo. Thành công = platform sạch, doanh thu ổn, cộng đồng phát triển.

## Success Criteria

### Tín hiệu người dùng
- Người dùng đăng ký và tạo bài viết đầu tiên trong phiên đầu
- Tỷ lệ quay lại hàng ngày (DAU/MAU) > 20%
- Trung bình mỗi bài viết có ≥ 2 tương tác (like/comment/share)
- Người dùng follow ≥ 3 tài khoản trong tuần đầu

### Tín hiệu kinh doanh
- Hệ thống quảng cáo hoạt động ổn, có impression/click tracking chính xác
- CTR quảng cáo ≥ 0.5% (industry average cho news platforms)
- Chi phí vận hành server nằm trong ngân sách cá nhân giai đoạn đầu

### Tín hiệu kỹ thuật
- Codebase áp dụng được các công nghệ mới, có giá trị học tập
- Response time API < 500ms cho 95th percentile
- Zero critical security vulnerabilities

## Scope

### V1 — Foundation (In Scope)

**Đã có (từ base code v1.3.0):**
- ✅ Backend API cơ bản (News, Category CRUD)
- ✅ JWT Authentication (admin)
- ✅ Admin Panel + Dashboard
- ✅ Advertisement System (banner, in-feed, sidebar, tracking)
- ✅ File Upload
- ✅ Docker support

**Cần build — theo thứ tự ưu tiên:**

1. **User Authentication mở rộng** — đăng ký/đăng nhập cho người dùng thường (không chỉ admin), profile cá nhân, quản lý tài khoản
2. **Personal Feed** — feed cá nhân hóa hiển thị bài viết từ người đang follow + trending
3. **User-Generated Content (UGC)** — người dùng tự đăng bài, chọn category, gắn tag, quản lý bài viết của mình
4. **Social Interactions** — like, comment, share trên mỗi bài viết
5. **Follow/Unfollow** — theo dõi người dùng khác, feed cập nhật theo
6. **Content Visibility Control** — người dùng chọn public/private/followers-only cho bài viết
7. **Content Moderation** — hệ thống report + quét tự động vi phạm

### V2 — Credibility Layer (Out of Scope for V1)

- Journalist badge system (đăng ký → admin duyệt → badge ✅)
- Filter "chỉ xem từ nhà báo"
- Journalist analytics (reach, engagement)
- Nâng cấp hệ thống quảng cáo (self-service cho advertisers)

### V3+ — Growth & Monetization (Future)

- Đa ngôn ngữ (i18n) cho expansion quốc tế
- Thuật toán đề xuất nội dung nâng cao
- Real-time notifications (WebSocket)
- Mobile app (React Native hoặc Flutter)
- Content scheduling
- Advanced analytics dashboard
- SEO tools cho content creators

### Explicitly Out of Scope

- Payment/subscription system (không paywall)
- Video hosting (chỉ embed, không host)
- Live streaming
- Marketplace/e-commerce
- Messaging/chat 1-1

## Vision

Nếu NewsRoom thành công, trong 2-3 năm nó trở thành **nền tảng tin tức cộng đồng hàng đầu Việt Nam** — nơi mà:

- **100K+ người dùng** đóng góp và tiêu thụ tin tức hàng ngày
- **Nhà báo tự do** coi đây là kênh phân phối chính bên cạnh tòa soạn
- **Doanh thu quảng cáo** đủ cover chi phí vận hành và tạo lợi nhuận
- **Mở rộng ra Đông Nam Á** — bắt đầu từ các thị trường có nhu cầu tin tức cộng đồng tương tự
- **Công nghệ** — platform trở thành showcase thực tế cho các kỹ thuật hiện đại (AI content moderation, recommendation engine, real-time systems)

Tầm nhìn lớn nhất: trở thành **"Reddit cho tin tức" phiên bản Việt Nam** — nhưng với lớp uy tín từ nhà báo xác thực mà Reddit không có.

## Risks & Unknowns

| Risk | Mức độ | Mitigation |
|------|--------|------------|
| Cold-start (không có nội dung ban đầu) | Cao | Seed data, mời nhà báo quen, tự tạo nội dung giai đoạn đầu |
| Content moderation khó scale | Trung bình | Kết hợp auto-scan + community report + admin review |
| Cạnh tranh với Facebook/Zalo | Cao | Không cạnh tranh trực tiếp — focus vào niche tin tức |
| Chi phí server khi scale | Trung bình | Bắt đầu nhỏ, optimize trước khi scale |
| Một founder (bus factor = 1) | Trung bình | Codebase sạch, documentation tốt (đã có), BMad process |
