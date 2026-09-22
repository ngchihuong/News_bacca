---
name: NewsRoom Design Spine
status: final
description: Visual design tokens and identity for NewsRoom platform — Social-First Minimalist with Ant Design and Tailwind CSS
colors:
  primary: '#FF6600'
  primary-hover: '#E05A00'
  primary-light: '#FFF7ED'
  secondary: '#13357B'
  secondary-hover: '#0C2354'
  secondary-light: '#EEF2FF'
  journalist-badge: '#0284C7'
  journalist-bg: '#E0F2FE'
  ad-badge: '#B45309'
  ad-badge-bg: '#FEF3C7'
  surface: '#FFFFFF'
  canvas: '#F8FAFC'
  border: '#E2E8F0'
  border-focus: '#CBD5E1'
  text-main: '#0F172A'
  text-muted: '#64748B'
  text-sub: '#94A3B8'
  destructive: '#EF4444'
  destructive-light: '#FEE2E2'
  success: '#10B981'
typography:
  display:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 24px
    fontWeight: '800'
    lineHeight: '1.25'
    letterSpacing: '-0.02em'
  headline:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 18px
    fontWeight: '700'
    lineHeight: '1.4'
    letterSpacing: '-0.01em'
  title:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 16px
    fontWeight: '700'
    lineHeight: '1.45'
  body:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 15px
    fontWeight: '400'
    lineHeight: '1.6'
  body-bold:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 15px
    fontWeight: '600'
    lineHeight: '1.6'
  meta:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 13px
    fontWeight: '400'
    lineHeight: '1.5'
  badge:
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif'
    fontSize: 11px
    fontWeight: '700'
    lineHeight: '1.2'
rounded:
  sm: 6px
  md: 12px
  lg: 16px
  full: 9999px
spacing:
  '1': 4px
  '2': 8px
  '3': 12px
  '4': 16px
  '5': 20px
  '6': 24px
  '8': 32px
  gutter: 24px
  margin-mobile: 16px
components:
  button-primary:
    background: '{colors.primary}'
    foreground: '#FFFFFF'
    hover-background: '{colors.primary-hover}'
    radius: '{rounded.full}'
    padding-x: '{spacing.5}'
    padding-y: '{spacing.2}'
    fontSize: '{typography.meta.fontSize}'
    fontWeight: '700'
  button-secondary:
    background: '{colors.secondary}'
    foreground: '#FFFFFF'
    hover-background: '{colors.secondary-hover}'
    radius: '{rounded.full}'
    padding-x: '{spacing.4}'
    padding-y: '{spacing.1}'
  badge-journalist:
    background: '{colors.journalist-bg}'
    foreground: '{colors.journalist-badge}'
    radius: '{rounded.full}'
    padding-x: '{spacing.2}'
    padding-y: '2px'
    fontSize: '{typography.badge.fontSize}'
    fontWeight: '{typography.badge.fontWeight}'
  post-card:
    background: '{colors.surface}'
    border: '1px solid {colors.border}'
    radius: '{rounded.md}'
    padding: '{spacing.4}'
  post-card-journalist:
    background: '{colors.surface}'
    border: '1px solid {colors.border}'
    border-left: '3px solid {colors.journalist-badge}'
    radius: '{rounded.md}'
    padding: '{spacing.4}'
  composer-box:
    background: '{colors.surface}'
    border: '1px solid {colors.border}'
    radius: '{rounded.md}'
    padding: '{spacing.4}'
---

# NewsRoom Design Spine

> Visual identity reference per the Google Labs DESIGN.md specification. Paired with `EXPERIENCE.md`.
> Visual reference composition: `mockups/home-feed.html`. The spine wins on conflict.

## Brand & Style

NewsRoom là mạng xã hội tin tức cộng đồng kết hợp giữa tính năng động, tự do của dòng chảy mạng xã hội (Threads, X) với tính chuẩn xác, uy tín của báo chí chính thống. Khác với các mạng xã hội giải trí ngập tràn hình ảnh selfie hay meme giật gân, NewsRoom đặt **Thông tin & Trải nghiệm đọc** làm trung tâm.

Ngôn ngữ thị giác của NewsRoom theo định hướng **Modern Stream (Social-First Minimalist)**:
- **Tối giản & Thoáng đãng:** Không dùng các khung hộp nặng nề hay màu sắc chói lọi gây nhiễu. Bề mặt màu trắng ngà `{colors.canvas}` và thẻ `{colors.surface}` với đường viền mỏng `{colors.border}` giúp các mẩu tin nổi bật tự nhiên.
- **Năng động nhưng Đáng tin cậy:** Màu cam thương hiệu `{colors.primary}` mang lại năng lượng tương tác nhanh của cộng đồng, đối trọng hoàn hảo với màu xanh navy `{colors.secondary}` và xanh Sky Blue `{colors.journalist-badge}` đại diện cho sự tin cậy, chính danh của nhà báo xác thực.
- **Nội dung là trung tâm:** Mọi font chữ, khoảng cách đệm và phân cách dòng đều được căn chỉnh để người dùng có thể lướt nhanh trên xe bus, trong giờ nghỉ trưa mà không mỏi mắt.

Hệ thống kế thừa nền tảng **Ant Design 5 + Tailwind CSS** sẵn có của frontend: các component cơ sở (Modal, Dropdown, Tooltip, Skeleton) tuân thủ cấu trúc của Ant Design nhưng được áp dụng bộ token màu sắc và bo góc riêng này.

## Colors

Bảng màu của NewsRoom được tinh chỉnh theo mục đích truyền tải thông điệp rõ ràng:

- **Cam Năng Động (`{colors.primary}` — `#FF6600`):** Màu nhận diện cốt lõi của nền tảng, xuất hiện ở nút Đăng tin, trạng thái Tab đang chọn, icon tương tác khi active và các điểm kích hoạt hành động chính.
- **Cam Nhạt (`{colors.primary-light}` — `#FFF7ED`):** Dùng làm nền hover cho menu điều hướng, nút công cụ, tạo phản hồi êm ái khi di chuột.
- **Xanh Navy Uy Tín (`{colors.secondary}` — `#13357B`):** Đại diện cho thẩm quyền và tổ chức; dùng cho Logo, nút Theo dõi (Follow), tiêu đề widget và các thành phần mang tính định chế.
- **Xanh Xác Thực Nhà Báo (`{colors.journalist-badge}` — `#0284C7` & `{colors.journalist-bg}` — `#E0F2FE`):** Dành riêng cho badge xác thực nhà báo và đường viền nhấn cạnh trái của bài viết nhà báo. Tuyệt đối không dùng màu này cho các nút hành động thông thường để giữ nguyên giá trị nhận diện uy tín.
- **Vàng Hổ Phách Tài Trợ (`{colors.ad-badge}` — `#B45309` & `{colors.ad-badge-bg}` — `#FEF3C7`):** Nhãn minh bạch cho các bài đăng quảng cáo và tin tài trợ từ đối tác.
- **Canvas Nền (`{colors.canvas}` — `#F8FAFC`):** Tông màu Slate-50 thanh thoát làm nền ứng dụng, chống chói mắt khi đọc lâu so với màu trắng tuyệt đối.
- **Bề Mặt Card (`{colors.surface}` — `#FFFFFF`):** Dành cho thẻ bài viết, ô soạn thảo, sidebar widget.
- **Đường Viền Hairline (`{colors.border}` — `#E2E8F0`):** Phân cách bài viết và các module nhẹ nhàng.

## Typography

Kế thừa font hệ thống chuẩn (`-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif`) giúp ứng dụng tải tức thì, tương thích hoàn hảo trên cả iOS, Android, Windows và macOS mà không phụ thuộc font web nặng.

- **Display (`{typography.display.fontSize}` — 24px, Bold 800):** Dùng cho Logo NewsRoom, tiêu đề trang lớn.
- **Headline (`{typography.headline.fontSize}` — 18px, Bold 700):** Tiêu đề bài viết nổi bật, tiêu đề modal.
- **Title (`{typography.title.fontSize}` — 16px, Semi-Bold 700):** Tên người dùng, tiêu đề widget sidebar, tiêu đề tab.
- **Body (`{typography.body.fontSize}` — 15px, Regular 400, Line-height 1.6):** Phông chữ vàng cho nội dung bài viết và bình luận; dễ đọc lướt, ngắt dòng tự nhiên.
- **Body-Bold (`{typography.body-bold.fontSize}` — 15px, Semi-Bold 600):** Nhấn mạnh các đoạn thông tin sự kiện quan trọng trong bài.
- **Meta (`{typography.meta.fontSize}` — 13px, Regular 400):** Thời gian đăng (timestamp), lượt thích, số lượng bình luận, hashtag phụ.
- **Badge (`{typography.badge.fontSize}` — 11px, Bold 700):** Nhãn "Nhà báo xác thực", nhãn "Được tài trợ".

## Layout & Spacing

Hệ thống lưới và khoảng cách tuân thủ bước nhảy cơ bản 4px / 8px:

- **Bố cục Desktop (≥ 1024px):** Cấu trúc 3 cột cân xứng:
  - Cột trái (240px - 260px, sticky): Logo, Menu điều hướng chính, Nút đăng bài nổi bật.
  - Cột giữa (560px - 640px): Dòng chảy tin tức trung tâm (Composer + Feed Posts). Chiều rộng tối ưu cho mắt đọc từ 60-75 ký tự mỗi dòng.
  - Cột phải (280px - 300px, sticky): Chủ đề nóng (#Trending), Gợi ý nhà báo theo dõi, Widget quảng cáo.
- **Bố cục Tablet (768px - 1023px):** Thu gọn sidebar trái về dạng icon đơn lẻ, ẩn sidebar phải hoặc đẩy xuống cuối.
- **Bố cục Mobile (< 768px):** Cột đơn 100% full-bleed với lề an toàn `{spacing.margin-mobile}` (16px). Điều hướng chuyển về thanh Bottom Navigation Bar cố định ở đáy màn hình.

## Elevation & Depth

Hướng thiết kế Modern Stream đề cao sự phẳng tinh tế (Flat Minimalist), hạn chế bóng đổ dày:
- Thẻ bài viết `{components.post-card}` nằm phẳng trên nền canvas, phân tách bằng đường viền `{colors.border}` và đổ bóng siêu mỏng `{box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05)}`.
- Dropdown menu và Popover (Ant Design) sử dụng bóng đổ trung bình: `{box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08)}`.
- Modal cửa sổ nổi (Đăng bài, Báo cáo vi phạm): `{box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1)}` với backdrop mờ tối `{background: rgba(15, 23, 42, 0.4)}`.

## Shapes

- Bo góc thẻ và khung soạn tin: `{rounded.md}` (12px) tạo cảm giác thân thiện, hiện đại nhưng không quá tròn trịa.
- Bo góc các nút bấm (Button), Badge, Avatar: `{rounded.full}` (9999px — dạng viên thuốc pill) giúp hành động nổi bật rõ ràng so với các khối hình chữ nhật của bài viết.
- Bo góc hình ảnh đính kèm trong bài: `{rounded.sm}` (8px) hoặc 6px theo lề chứa.

## Components

- **Thẻ bài viết thông thường (`{components.post-card}`):** Avatar người dùng (40px) bên trái, tên và thời gian đăng bài, nội dung văn bản (15px), cụm ảnh (1 đến 4 ảnh lưới), thanh tương tác đáy (Thích, Bình luận, Chia sẻ, Báo cáo vi phạm).
- **Thẻ bài viết Nhà Báo (`{components.post-card-journalist}`):** Nhận diện đặc thù bằng viền cạnh trái 3px màu xanh Sky Blue `{colors.journalist-badge}`, huy hiệu `{components.badge-journalist}` kế bên tên tác giả.
- **Thanh soạn bài nhanh (`{components.composer-box}`):** Ô nhập văn bản đa dòng với placeholder gợi mở "Bạn có tin tức gì nóng muốn chia sẻ hôm nay?", icon đính kèm ảnh, nút bấm Đăng tin `{components.button-primary}`.
- **Thẻ tin quảng cáo (`{components.ad-card}`):** Hiển thị nhãn `{colors.ad-badge}` "Được tài trợ", nút kêu gọi hành động CTA ("Tìm hiểu thêm" / "Xem chi tiết") màu `{colors.secondary}`.

## Do's and Don'ts

### Do's
- Luôn hiển thị huy hiệu `{components.badge-journalist}` cạnh tên nhà báo đã được kiểm duyệt.
- Giữ nút Báo cáo vi phạm (Report) dễ tiếp cận ở menu tùy chọn góc thẻ bài viết để phục vụ kiểm duyệt cộng đồng.
- Duy trì khoảng thở tối thiểu 16px giữa các bài viết trên Feed để người dùng không bị ngợp thông tin.
- Hiển thị rõ số lượng tương tác (Like, Comment) ngay dưới chân bài viết.

### Don'ts
- Không dùng màu đỏ tươi cho các hành động thông thường (chỉ dành riêng cho nút Xóa/Hủy và icon Thích khi đã active).
- Không tự ý dùng badge xanh của nhà báo cho bất kỳ vai trò nào khác ngoài nhà báo được Admin cấp quyền.
- Không che phủ toàn màn hình bằng quảng cáo popup gây gián đoạn trải nghiệm đọc tin; quảng cáo in-feed phải tuân thủ tỷ lệ của thẻ bài thông thường.
