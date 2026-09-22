---
title: "Product Brief Addendum: NewsRoom"
created: 2026-09-14
updated: 2026-09-14
---

# Product Brief Addendum: NewsRoom

Tài liệu này chứa chi tiết bổ sung từ quá trình discovery — thông tin hữu ích cho PRD, Architecture, và UX design nhưng không thuộc về brief chính.

## Existing Codebase Inventory (v1.3.0)

### Backend — Đã có

| Layer | Components | Notes |
|-------|-----------|-------|
| Models/Entities | News, Category, Tag, User, Comment, Advertisement | 6 entities, đủ base |
| Repositories | 5 repositories | MongoDB Spring Data |
| Services | INewsService, ICategoryService, IAuthService, IAdvertisementService, IFileStorageService | Interface + Impl pattern (SOLID) |
| Controllers | News, Category, Auth, Advertisement, FileUpload | 47+ endpoints |
| DTOs | 4+ DTOs | Tách biệt internal models |
| Config | SecurityConfig, CorsConfig | JWT ready |
| Architecture | Interface + Implementation pattern | Đã refactor theo best practice |

### Frontend — Đã có

| Area | Components | Notes |
|------|-----------|-------|
| Pages | Home, News Detail, Category, Admin (Dashboard, News, Ads, Login) | 6+ pages |
| Public Components | Header, Footer, MainSlider, FeaturedNews, CategorySection, LatestNews, Sidebar | 7 components |
| Admin Components | Dashboard, News Management, Ad Management | 3+ components |
| Ad Components | AdDisplay, AdBanner, AdInFeed, AdSidebar | 4 components |
| API Client | Axios-based with full type definitions | TypeScript |

### Infrastructure — Đã có

- Docker Compose (backend + frontend + MongoDB)
- Multi-stage Dockerfiles
- Git repository với .gitignore
- 15+ documentation files

## User Role Matrix — Chi tiết

| Role | Đăng bài | Comment/Like/Share | Follow | Quản lý nội dung | Duyệt nhà báo | Quản lý Ads | Badge |
|------|----------|-------------------|--------|-------------------|---------------|-------------|-------|
| Guest (chưa đăng ký) | ❌ | ❌ | ❌ | ❌ | ❌ | ❌ | — |
| User (đã đăng ký) | ✅ | ✅ | ✅ | Bài của mình | ❌ | ❌ | — |
| Journalist (xác thực) | ✅ | ✅ | ✅ | Bài của mình | ❌ | ❌ | ✅ Badge nhà báo |
| Admin | ✅ | ✅ | ✅ | Tất cả | ✅ | ✅ | Admin badge |

## Journalist Verification Flow (V2)

```
User đăng ký tài khoản thường
    → Nộp đơn xin xác thực nhà báo (cung cấp thông tin tòa soạn, thẻ nhà báo)
    → Admin/Moderator review đơn
    → Approve → User nhận badge nhà báo ✅
    → Reject → Thông báo lý do, có thể nộp lại
```

## Content Moderation Architecture (V1)

```
User đăng bài
    → Auto-scan (keywords, patterns) → Flag nếu nghi ngờ
    → Publish (hoặc giữ lại nếu flagged)
    → Community report mechanism
    → Admin review flagged/reported content
    → Action: approve / remove / warn user / ban user
```

## Content Visibility Model

| Setting | Ai xem được |
|---------|------------|
| Public | Mọi người (kể cả guest) |
| Followers-only | Chỉ người đang follow tác giả |
| Private | Chỉ tác giả |

## Competitive Landscape Notes

| Platform | Strengths | Weaknesses vs NewsRoom |
|----------|-----------|----------------------|
| Facebook | Massive user base, social graph | Tin tức bị chìm trong noise, không focus |
| VnExpress/Dân trí | Uy tín, nội dung chất lượng | Một chiều, không UGC |
| Reddit (r/Vietnam) | Community-driven, voting | Không có journalist verification, English-centric |
| Threads/X | Real-time, follow model | Short-form, không cấu trúc cho tin tức dài |
| Lotus (VN) | Vietnamese social network | Đã đóng cửa — bài học về cold-start |

## Technology Stack (Current)

| Component | Technology | Version |
|-----------|-----------|---------|
| Backend Runtime | Java | 21 |
| Backend Framework | Spring Boot | 3.2.0 |
| Database | MongoDB | 5.0+ |
| Frontend Runtime | Node.js | 20.14.0 |
| Frontend Framework | Next.js | 14 |
| UI Library | React | 18 |
| Language (FE) | TypeScript | 5.x |
| Styling | Tailwind CSS | 3.3 |
| Auth | JWT | — |
| Build (BE) | Maven | 3.9 |
| Containerization | Docker + Compose | — |

## Feature Priority Rationale

**Tại sao Auth trước Feed:** Không thể có feed cá nhân nếu không biết user là ai. Auth là foundation, không phải feature.

**Tại sao Feed trước UGC:** Feed là trải nghiệm đầu tiên user thấy. Ngay cả khi chưa đăng bài, user cần thấy nội dung hấp dẫn để ở lại. Feed có thể chạy với seed data/admin content trong khi build UGC.

**Tại sao Interactions trước Follow:** Like/comment/share tạo engagement ngay trên nội dung hiện có. Follow cần có đủ users để meaningful.

**Tại sao Journalist tags ở V2:** Cần có community hoạt động trước — nhà báo sẽ không join platform trống.

## Rejected Alternatives

| Idea | Lý do reject |
|------|-------------|
| Tab riêng cho nhà báo | Phân cách nhân tạo, pha loãng trải nghiệm, biến thành "2 sản phẩm trong 1 vỏ" |
| Duyệt bài trước khi đăng | Tạo bottleneck, giết tính tức thì của social platform |
| Paywall / subscription | Không phù hợp giai đoạn đầu, cần volume user trước |
| Self-service ads | Quá phức tạp cho V1, admin quản lý ads là đủ |
