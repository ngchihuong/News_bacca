---
name: NewsRoom Experience Spine
status: final
sources:
  - _bmad-output/planning-artifacts/prds/prd-News-2026-09-14/prd.md
  - _bmad-output/planning-artifacts/briefs/brief-News-2026-09-14/brief.md
updated: 2026-09-14
---

# NewsRoom — Experience Spine

> Interaction, information architecture, and behavioral specification for NewsRoom.
> Paired with `DESIGN.md`. Composition reference: `mockups/home-feed.html`. The spine wins on conflict.

## Foundation

Single-surface responsive web application with native mobile ergonomics. Kế thừa hệ thống **Ant Design 5 + Tailwind CSS** trên nền tảng **Next.js 15 & React 19**. Hệ thống component library xử lý nền tảng form, modal, dropdown và notification; tài liệu này định nghĩa hành vi trải nghiệm, dòng thông tin và tương tác riêng của NewsRoom. `DESIGN.md` là tham chiếu định danh thị giác.

Ứng dụng phục vụ cả khách vãng lai (Guest - chỉ đọc), Người dùng cộng đồng (User - đăng bài, tương tác, follow), Nhà báo xác thực (Journalist - cấp quyền, đăng bài có badge), và Quản trị viên (Admin - kiểm duyệt bài báo cáo, quản lý quảng cáo).

## Information Architecture

| Màn hình (Surface) | Truy cập từ (Reached from) | Mục đích & Chức năng cốt lõi (Purpose) |
|---|---|---|
| **Feed Trang chủ (Home Feed)** | Mở ứng dụng / URL `/` | Dòng thời gian tin tức tổng hợp với 3 tab: "Dành cho bạn" (Personalized/Mix), "Tin Nhà Báo" (Chỉ tin từ nhà báo xác thực), và "Đang theo dõi" (Followed users). Chứa khung soạn bài nhanh. |
| **Chi tiết bài viết (Post Detail)** | Click vào thẻ bài viết / URL `/posts/:id` | Xem đầy đủ nội dung bài viết, hình ảnh gốc chất lượng cao, luồng bình luận dạng cây (nested comments) và gợi ý bài liên quan. |
| **Trang tác giả / Cá nhân (Profile)** | Click vào tên/avatar tác giả / URL `/profile/:id` | Hiển thị thông tin tác giả, tiểu sử, huy hiệu nhà báo (nếu có), số người theo dõi, danh sách bài đã đăng và nút "Theo dõi" (Follow). |
| **Khám phá / Xu hướng (Explore / Trending)** | Sidebar / Bottom Nav / URL `/explore` | Tìm kiếm bài viết theo từ khóa, lọc theo chuyên mục (Thời sự, Giao thông, Công nghệ, Đời sống), danh sách hashtag thịnh hành. |
| **Hộp thông báo (Notifications)** | Menu bên / Bell icon / URL `/notifications` | Cập nhật theo thời gian thực về lượt tương tác: Ai đã thích bài viết, ai đã bình luận, ai vừa follow bạn, hoặc cảnh báo bài viết bị báo cáo. |
| **Cửa sổ đăng bài (Post Composer Modal)** | Nút "+ Đăng tin mới" trên Header/Sidebar | Khung soạn thảo đầy đủ: nhập nội dung, chọn chuyên mục, tải lên nhiều ảnh (tối đa 4 ảnh), chọn chế độ xem (Công khai / Chỉ người theo dõi). |
| **Cửa sổ báo cáo (Report Modal)** | Menu ••• trên góc bài viết | Người dùng chọn lý do vi phạm: Tin giả / Sai sự thật, Ngôn từ xúc phạm, Spam / Quảng cáo rác, Vi phạm bản quyền. |
| **Bảng điều khiển Quản trị (Admin Moderation & Ads)** | URL `/admin` (dành riêng cho Admin) | Quản lý danh sách bài viết bị báo cáo (Duyệt giữ lại / Xóa bài / Khóa tài khoản), duyệt cấp quyền Nhà báo, và quản lý các chiến dịch quảng cáo hiển thị. |

→ Tham chiếu bố cục chi tiết: `mockups/home-feed.html`. Cột điều hướng bên trái thu gọn trên tablet và chuyển thành thanh đáy (Bottom Nav Bar) trên điện thoại di động.

## Voice and Tone (Microcopy)

Ngôn ngữ giao tiếp của NewsRoom trung tính, lịch thiệp, hướng đến sự tôn trọng thông tin và khuyến khích thảo luận văn minh:

| Tình huống giao tiếp | Nên dùng (Do) | Không nên dùng (Don't) |
|---|---|---|
| **Placeholder đăng tin** | "Bạn có thông tin gì muốn chia sẻ hôm nay?" | "Bạn đang nghĩ gì thế? Viết gì đi nào! 🚀" |
| **Nút gửi bài** | "Đăng tin" | "Bắn tin lên ngay" |
| **Xác nhận báo cáo vi phạm** | "Cảm ơn bạn. Báo cáo của bạn đã được gửi tới ban quản trị kiểm duyệt." | "Báo cáo thành công! Chúng tôi sẽ trừng phạt bài này." |
| **Bài viết bị quét từ khóa vi phạm** | "Bài viết chứa từ khóa nhạy cảm và đang chờ ban biên tập xem xét." | "Bài viết của bạn bị chặn vì vi phạm chính sách!" |
| **Trạng thái Feed trống (chưa follow ai)** | "Chưa có bài viết mới từ người bạn theo dõi. Khám phá các nhà báo nổi bật bên dưới để cập nhật tin tức." | "Trống trơn rồi! Mau đi follow ai đó đi." |
| **Huy hiệu nhà báo** | "✓ Nhà báo xác thực" kèm tên đơn vị báo chí | "V.I.P Member" hoặc "Người nổi tiếng" |

## Component Patterns (Behavioral)

Quy chuẩn hành vi của các thành phần giao tiếp (Visual tokens quy định tại `DESIGN.md.Components`):

- **Thẻ bài viết (Post Card):**
  - Click vào phần thân bài hoặc tiêu đề để mở `Post Detail`.
  - Click vào Avatar hoặc Tên tác giả để mở trang `Profile` (không kích hoạt mở bài).
  - Tương tác nút Thích (Like): Optimistic update ngay lập tức (+1 hoặc -1 số đếm và chuyển màu icon đỏ), tự động rollback nếu server timeout.
  - Nút Tùy chọn •••: Mở menu nhỏ chứa hành động: "Báo cáo vi phạm", "Sao chép liên kết", và "Xóa bài" (chỉ hiển thị nếu tác giả là chính người dùng đang đăng nhập).
- **Thanh soạn tin nhanh (Composer):**
  - Mặc định hiển thị dạng 1 dòng thu gọn để tiết kiệm không gian.
  - Khi người dùng click (focus) vào ô nhập, khung mở rộng thành 3 dòng kèm thanh công cụ đính kèm ảnh và nút chọn chuyên mục.
  - Tự động lưu bản nháp tạm thời vào `localStorage` phòng trường hợp rớt mạng hoặc người dùng vô tình reload trang.
- **Hệ thống Tag & Chuyên mục:**
  - Mỗi bài viết gắn tối đa 1 chuyên mục chính và tối đa 3 hashtag tự do.
  - Click vào hashtag sẽ mở trang tìm kiếm kết quả tương ứng.
- **Thẻ quảng cáo trong Feed (In-Feed Ad):**
  - Hiển thị theo tỷ lệ 1 thẻ quảng cáo sau mỗi 5-7 bài viết tin tức.
  - Luôn có nhãn minh bạch `{colors.ad-badge}` "Được tài trợ".
  - Có nút "Ẩn quảng cáo này" trong menu ••• để tôn trọng trải nghiệm người dùng.

## State Patterns

| Trạng thái (State) | Bề mặt hiển thị | Cách xử lý trải nghiệm (Treatment) |
|---|---|---|
| **Tải dữ liệu ban đầu (Cold Load)** | Feed Trang chủ | Hiển thị 3-4 khung xương `Skeleton` (Ant Design Skeleton) mô phỏng chính xác cấu trúc Avatar + Dòng text + Khung ảnh. Không dùng spinner xoay tròn toàn trang. |
| **Đang tải thêm tin (Infinite Scroll)** | Đáy Feed | Hiển thị thanh tiến trình nhỏ nhẹ nhàng "Đang tải thêm tin mới..." khi người dùng cuộn đến 80% chiều dài trang. |
| **Feed trống (Empty State)** | Tab "Đang theo dõi" | Hiển thị thông báo nhẹ nhàng kèm danh sách 3 thẻ tác giả nhà báo đề xuất có nút "+ Theo dõi" ngay tại chỗ. |
| **Mất kết nối mạng (Offline)** | Toàn ứng dụng | Hiển thị banner cảnh báo cố định ở mép trên: "Mất kết nối internet. Đang hiển thị tin tức đã lưu trong bộ nhớ đệm." Các thao tác Like/Comment sẽ tạm hoãn và tự đồng bộ khi có mạng lại. |
| **Lỗi hệ thống (Error State)** | Chi tiết bài viết không tồn tại / bị xóa | Trang báo lỗi 404 thân thiện: "Bài viết này không còn khả dụng hoặc đã bị gỡ bỏ theo quy chuẩn cộng đồng. [Quay lại Trang chủ]". |
| **Kiểm duyệt tự động (Flagged by Auto-scan)** | Sau khi bấm Đăng tin | Nếu nội dung chứa từ khóa vi phạm trong từ điển cấm, bài viết lập tức chuyển sang trạng thái "Đang kiểm duyệt". Người viết thấy thông báo giải thích rõ ràng và bài không xuất hiện trên Public Feed cho đến khi Admin duyệt. |

## Interaction Primitives

- **Cuộn trang & Tải tin tức:** Hỗ trợ Infinite Scroll mượt mà kết hợp nút "Cuộn lên đầu trang" nổi lên khi cuộn quá 2 màn hình.
- **Đóng/Mở Modal:**
  - Nhấn phím `Esc` luôn đóng modal hoặc popover đang mở.
  - Bấm vào lớp nền mờ (backdrop) bên ngoài để đóng cửa sổ.
  - Khóa cuộn trang nền (`overflow: hidden`) khi modal đang hiển thị.
- **Thao tác nhanh trên phím (Power shortcuts trên Desktop):**
  - Phím `/` để focus ngay vào thanh Tìm kiếm toàn trang.
  - Phím `c` để mở nhanh cửa sổ Soạn bài mới.
  - Phím `j` / `k` để di chuyển tiêu điểm bài viết tiếp theo / trước đó trên Feed (giống Twitter/Reddit).

## Accessibility Floor

- **Chuẩn tương thích:** Đạt tiêu chuẩn WCAG 2.1 AA trên toàn bộ giao diện Web Responsive.
- **Tương phản thị giác:** Toàn bộ chữ chính (`{colors.text-main}`) và chữ phụ (`{colors.text-muted}`) đạt tỷ lệ tương phản tối thiểu 4.5:1 so với nền `{colors.surface}` và `{colors.canvas}`.
- **Hỗ trợ Screen Reader:**
  - Mọi nút bấm chỉ có icon (như nút Like, Share, Options) đều có thuộc tính `aria-label` tương ứng ("Thích bài viết", "Chia sẻ bài viết", "Báo cáo nội dung").
  - Huy hiệu nhà báo có nhãn đọc: `aria-label="Tài khoản nhà báo đã được xác thực bởi NewsRoom"`.
- **Điều hướng bàn phím (Keyboard Trapping & Focus):** Khung viền focus ring `{colors.border-focus}` hiển thị rõ nét khi dùng phím `Tab` để di chuyển qua các nút bấm và link bài viết.

## Responsive & Platform

| Thiết bị & Kích thước | Hành vi giao diện (Layout Behavior) |
|---|---|
| **Màn hình lớn (Desktop ≥ 1024px)** | Bố cục chuẩn 3 cột đầy đủ. Sidebar trái cố định menu điều hướng; cột giữa hiển thị Feed; cột phải hiển thị Xu hướng và Gợi ý Follow. |
| **Máy tính bảng (Tablet 768px – 1023px)** | Sidebar trái co lại thành icon nhỏ gọn. Cột phải chuyển xuống dạng thanh cuộn ngang (Horizontal scroll) phía trên Feed. |
| **Điện thoại di động (Mobile < 768px)** | Chuyển sang bố cục cột đơn 100% chiều ngang. Menu điều hướng chuyển thành Bottom Navigation Bar cố định ở đáy (Trang chủ, Nhà báo, Đăng bài, Thông báo, Cá nhân). Nút đăng bài nhanh dạng Floating Action Button hoặc icon nổi bật chính giữa thanh đáy. |

## Inspiration & Anti-patterns

- **Học hỏi từ Threads & X:** Trải nghiệm cuộn tin tức nhanh gọn, đường phân cách thanh mảnh, tương tác vi mô tức thì không gây giật lag.
- **Học hỏi từ Substack & Báo chí hiện đại:** Sự tôn trọng dành cho tác giả chuyên nghiệp qua badge xác thực rõ ràng và hiển thị đơn vị công tác của nhà báo.
- **Loại bỏ (Anti-patterns tránh tuyệt đối):**
  - *Không dùng bảng xếp hạng giật gân, điểm thưởng (gamification/karma score) gây áp lực câu view độc hại.*
  - *Không nhét quảng cáo pop-up che toàn màn hình.*
  - *Không hiển thị số lượng view ảo làm nhiễu độ tin cậy của thông tin.*

## Key Flows (Named-Protagonist Journeys)

### Luồng 1 — Minh chia sẻ cảnh báo ngập lụt cục bộ (Minh, 28 tuổi, nhân viên văn phòng)
1. Trên đường đi làm về gặp đoạn đường ngập nặng, Minh mở NewsRoom trên điện thoại.
2. Bottom bar sáng mục "Feed". Minh chạm vào ô soạn bài trên đầu Feed hoặc bấm icon Đăng bài.
3. Cửa sổ soạn bài trượt lên: Minh nhập nội dung "Đoạn ngã tư Thái Hà ngập khoảng 30cm, xe số và xe ga né gấp nhé anh em", đính kèm 1 ảnh hiện trường vừa chụp.
4. Minh chọn chuyên mục "Giao thông" và bấm "Đăng tin".
5. **Đỉnh điểm tương tác (Climax):** Hệ thống quét tự động kiểm tra từ khóa độc hại (Passed trong 0.2s), bài viết hiện ngay trên đầu Feed với avatar của Minh. Trong vòng 15 phút, 30 người dân quanh khu vực thả tim và bình luận cảm ơn vì tránh được điểm ngập.
6. Minh nhận thông báo đẩy nhẹ nhàng, mỉm cười và cất điện thoại an tâm tiếp tục hành trình.

### Luồng 2 — Lan tìm đọc tin tức thời sự chính thống (Lan, 35 tuổi, mẹ 2 con đọc tin trên xe bus)
1. Lan mở NewsRoom trên tablet khi đang ngồi trên xe bus sáng.
2. Lan muốn đọc tin tức có kiểm chứng, nên bấm vào tab "Tin Nhà Báo" trên thanh Feed.
3. Feed tức thì lọc ra các bài viết độc quyền từ các tác giả có huy hiệu `{components.badge-journalist}` viền xanh Sky Blue.
4. Lan dừng lại ở bài viết của Nhà báo Lê Bình (Ban Thời sự) phân tích phương án phân luồng giao thông mới.
5. **Đỉnh điểm tương tác (Climax):** Nhờ viền card xanh và badge xác thực, Lan hoàn toàn an tâm vào tính chính xác của bài viết mà không phải lo sợ tin đồn giả mạo. Lan bấm nút Like, chia sẻ bài viết về trang cá nhân kèm bình luận của mình, và bấm "+ Theo dõi" nhà báo Lê Bình.
6. Ngày hôm sau khi mở ứng dụng ở tab "Đang theo dõi", Lan thấy ngay các bài cập nhật tiếp theo của nhà báo Lê Bình.

### Luồng 3 — Admin Hương xử lý báo cáo vi phạm và tối ưu quảng cáo (Hương, Founder & Admin)
1. Buổi tối, Hương đăng nhập vào trang `/admin` trên máy tính xách tay.
2. Bảng điều khiển Moderation hiển thị thông báo "Có 2 bài viết bị người dùng báo cáo vi phạm".
3. Hương bấm vào xem: 1 bài viết đăng bán hàng giả mạo (được 3 người dùng gắn cờ Spam) và 1 bài tranh luận thời sự (bị đối thủ báo cáo sai sự thật).
4. **Đỉnh điểm tương tác (Climax):** Hương bấm "Xóa bài viết & Cảnh cáo tài khoản" đối với bài spam giả mạo, và bấm "Bác bỏ báo cáo (Giữ nguyên bài viết)" cho bài tranh luận hợp lệ. Cả hai hành động được ghi vết tức thì vào nhật ký kiểm duyệt.
5. Hương chuyển sang tab "Quản lý Quảng cáo", kích hoạt banner đối tác FPT Telecom vị trí sidebar với lịch hiển thị 7 ngày.
6. Nền tảng sạch sẽ, doanh thu quảng cáo được bảo đảm, Hương an tâm kết thúc phiên làm việc.
