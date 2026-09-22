---
title: 'Story 1.5: Dark & Light Theme Switcher'
type: 'feature'
created: '2026-09-21'
status: 'done'
baseline_commit: '15074e9e7553f55b83bbc0b3351a1cc8093e21c9'
route: 'dispatch'
review_loop_iteration: 1
context:
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/DESIGN.md
  - _bmad-output/planning-artifacts/ux-designs/ux-News-2026-09-14/EXPERIENCE.md
  - _bmad-output/planning-artifacts/epics.md
---

<frozen-after-approval reason="human-owned intent — do not modify unless human renegotiates">

## Intent

**Problem:** Độc giả và người dùng đọc tin tức trên NewsRoom vào ban đêm hoặc môi trường thiếu sáng gặp tình trạng chói mắt, mỏi mắt do giao diện hiện chỉ có nền trắng sáng cố định; chưa có cơ chế cho phép chuyển đổi và ghi nhớ chế độ Sáng / Tối (Light / Dark mode).

**Approach:** 
- Xây dựng `ThemeContext` quản lý trạng thái theme (`light` | `dark`), tự động khởi tạo từ `localStorage` (fallback theo `prefers-color-scheme`), đồng bộ toggle class `dark` trên thẻ `<html>` và gán thuộc tính `data-theme`.
- Cấu hình Tailwind CSS với `darkMode: 'class'` và đồng bộ bộ token màu tối (`#0F172A`, `#1E293B`, `#334155`, `#F8FAFC`).
- Tích hợp Ant Design `ConfigProvider` trong `Providers.tsx` với thuật toán chuyển đổi linh hoạt giữa `antdTheme.defaultAlgorithm` và `antdTheme.darkAlgorithm`, giữ nguyên màu nhận diện thương hiệu cam (`#FF6600`).
- Xây dựng component `ThemeToggle` với icon Mặt trời / Mặt trăng mượt mà, hỗ trợ accessibility (`aria-label`, focus ring), gắn trên cả Header (Desktop) và MobilePanel / DropdownMenu (Mobile & User menu).

## Boundaries & Constraints

**Always:**
- Lưu trữ lựa chọn của người dùng vào `localStorage` với key `newsroom_theme` và đọc lại khi tải trang để không bị mất cấu hình khi người dùng refresh hoặc mở tab mới.
- Tích hợp đồng bộ cả hai hệ thống UI: Tailwind CSS (class `dark` trên `<html>`) và Ant Design (`theme.darkAlgorithm` / `theme.defaultAlgorithm`).
- Đảm bảo tỷ lệ tương phản văn bản WCAG 2.1 AA (≥ 4.5:1) ở cả hai chế độ: nền sáng chữ tối, nền tối chữ sáng (`#F8FAFC`, `#0F172A`).
- Hỗ trợ đầy đủ phím bấm bàn phím và nhãn screen reader `aria-label` cho nút toggle.
- Tránh hiện tượng Flash of Unstyled Content (FOUC) khi tải trang bằng cách kiểm tra theme ngay khi render.

**Never:**
- Không reload toàn bộ trang (page refresh) khi người dùng bấm chuyển theme.
- Không hardcode màu nền trắng (`bg-white`) hoặc màu chữ đen (`text-black`) ở các component cấp cao mà không có đối ứng `dark:bg-...` và `dark:text-...`.
- Không gửi request lưu theme lên backend nếu không có endpoint (lưu hoàn toàn phía client bằng `localStorage`).

## I/O & Edge-Case Matrix

| Scenario | Input / State | Expected Output / Behavior | Error Handling |
|----------|--------------|---------------------------|----------------|
| Lần đầu truy cập (chưa lưu theme) | Chưa có key `newsroom_theme` trong localStorage | Tự động phát hiện qua `window.matchMedia('(prefers-color-scheme: dark)')`, nếu hệ thống là dark thì dùng dark, ngược lại mặc định light | Fallback an toàn về `light` nếu window/matchMedia không khả dụng |
| Chuyển từ Light sang Dark | Người dùng bấm nút ThemeToggle | Class `dark` được thêm vào `<html>`, Ant Design chuyển sang `darkAlgorithm`, icon đổi sang Mặt trời, lưu `localStorage` = `dark` | N/A |
| Chuyển từ Dark sang Light | Người dùng bấm nút ThemeToggle | Class `dark` được xóa khỏi `<html>`, Ant Design chuyển sang `defaultAlgorithm`, icon đổi sang Mặt trăng, lưu `localStorage` = `light` | N/A |
| Reload / Mở lại trang | Trang web tải lại khi `localStorage` = `dark` | Giao diện giữ nguyên theme `dark` ngay từ đầu, không bị chớp sáng | Fallback nếu parse localStorage lỗi |
| Chuyển theme trên Mobile | Người dùng mở MobilePanel và bấm nút ThemeToggle | Giao diện chuyển đổi tức thì, drawer và toàn bộ ứng dụng đổi màu đồng bộ | N/A |

</frozen-after-approval>

## Code Map

- `frontend/tailwind.config.js` -- Bổ sung `darkMode: 'class'` và kiểm tra cấu hình theme colors.
- `frontend/src/context/ThemeContext.tsx` -- Context quản lý state theme, đọc/ghi localStorage, toggle class `dark` trên `document.documentElement`.
- `frontend/src/components/ThemeToggle.tsx` -- Component nút bấm chuyển theme có icon Mặt trời / Mặt trăng, micro-animation xoay nhẹ, accessibility labels.
- `frontend/src/app/providers.tsx` -- Bọc `ThemeProvider`, cấu hình `ConfigProvider` của Ant Design với `darkAlgorithm` / `defaultAlgorithm` và theme tokens.
- `frontend/src/app/layout.tsx` -- Thêm `suppressHydrationWarning` trên `<html>`, tinh chỉnh `body` và `main` hỗ trợ dark classes (`dark:bg-slate-950 dark:text-slate-100`).
- `frontend/src/app/globals.css` -- Cập nhật styles nền tảng `:root` và `.dark` với màu Dark Slate chuẩn DESIGN.md.
- `frontend/src/components/Header.tsx` -- Bổ sung component `ThemeToggle` trên thanh công cụ Header (Desktop).
- `frontend/src/components/mobile/MobilePanel.tsx` -- Bổ sung mục chuyển đổi Theme trong danh mục tiện ích của menu di động.
- `frontend/src/components/Sidebar.tsx` & `Footer.tsx` -- Bổ sung class màu dark mode tương ứng.

## Tasks & Acceptance

**Execution:**
- [x] `frontend/tailwind.config.js` -- Thêm `darkMode: 'class'` -- Kích hoạt cơ chế dark variant dựa trên class cho Tailwind.
- [x] `frontend/src/app/globals.css` -- Định nghĩa biến màu dark theme và styles chuẩn cho body/card -- Đồng bộ màu sắc Dark Slate (`#0F172A`, `#1E293B`, `#F8FAFC`).
- [x] `frontend/src/context/ThemeContext.tsx` -- Tạo `ThemeContext`, `ThemeProvider` và custom hook `useTheme` -- Quản lý state theme, đồng bộ `localStorage` và DOM class `dark`.
- [x] `frontend/src/components/ThemeToggle.tsx` -- Tạo component `ThemeToggle` trực quan với icon Mặt trời / Mặt trăng -- Nút tương tác chuyển đổi theme cho người dùng.
- [x] `frontend/src/app/providers.tsx` -- Tích hợp `ThemeProvider` và đồng bộ `ConfigProvider` của Ant Design với `darkAlgorithm` -- Đồng bộ hóa giao diện giữa các component Ant Design và Tailwind.
- [x] `frontend/src/app/layout.tsx` -- Cập nhật thẻ `html` và `body` với `dark:bg-slate-950 dark:text-slate-100` và `suppressHydrationWarning` -- Đảm bảo không bị lỗi hydration mismatch và nền hiển thị mượt mà.
- [x] `frontend/src/components/Header.tsx` -- Nhúng `ThemeToggle` và cập nhật các class dark cho header, search bar, icon -- Cho phép chuyển theme trên desktop và giữ header tương thích dark mode.
- [x] `frontend/src/components/mobile/MobilePanel.tsx` -- Nhúng `ThemeToggle` và bổ sung dark styles cho panel di động -- Hỗ trợ chuyển đổi theme trên giao diện di động.
- [x] `frontend/src/components/Sidebar.tsx` & `Footer.tsx` -- Thêm dark styles thích hợp -- Giữ giao diện hài hòa không bị lệch tông màu khi bật dark mode.

**Acceptance Criteria:**
- Given người dùng truy cập bất kỳ trang nào trên NewsRoom, when bấm nút ThemeToggle trên Header hoặc Mobile Menu, then giao diện chuyển đổi tức thì giữa sáng và tối (Ant Design + Tailwind dark mode).
- Given người dùng đã chọn Dark mode, when reload trang hoặc mở tab mới, then giao diện vẫn duy trì Dark mode mà không bị chớp sáng (FOUC).
- Given người dùng duyệt web trong Dark mode, when quan sát các thành phần chính (Header, Main Feed, Sidebar, Post card, Profile), then độ tương phản và màu sắc hiển thị hài hòa, đúng chuẩn thiết kế Dark Slate (`#0F172A` / `#1E293B`).

### Review Findings (Adversarial Code Review)

- [x] [Review][Patch] Bổ sung dark mode token cho JournalistBadge đảm bảo WCAG 2.1 AA [frontend/src/components/JournalistBadge.tsx:18]
- [x] [Review][Patch] Thiết lập document.documentElement.style.colorScheme cho native scrollbars/inputs [frontend/src/context/ThemeContext.tsx:34]
- [x] [Review][Patch] Đồng bộ colorScheme trong inline script chống FOUC [frontend/src/app/layout.tsx:37]
- [x] [Review][Patch] Bổ sung dark classes cho bài viết và khu vực bình luận trang chi tiết tin [frontend/src/app/news/[slug]/page.tsx:80]
- [x] [Review][Patch] Bổ sung dark classes cho hồ sơ tác giả và danh sách bài viết tác giả [frontend/src/app/user/[id]/page.tsx:142]
- [x] [Review][Patch] Bổ sung dark classes cho LatestNews component [frontend/src/components/LatestNews.tsx:18]

#### Rejected Findings Appendix:
- Rejected (low): Lắng nghe `window.matchMedia` change event khi người dùng đổi OS theme lúc đang mở tab — Không cần thiết vì người dùng thường chủ động toggle trực tiếp trên web và lưu cố định vào `localStorage`.

## Implementation Notes

- **Tailwind & Ant Design Theme Unification:**
  - Bật `darkMode: 'class'` trong `frontend/tailwind.config.js`.
  - Tạo `frontend/src/context/ThemeContext.tsx` với hook `useTheme()`, tự động đồng bộ key `newsroom_theme` trong `localStorage` và class `dark` + `data-theme` trên `document.documentElement`.
  - Cấu hình `AntdConfigProvider` bên trong `Providers.tsx` để đồng bộ `theme.darkAlgorithm` / `theme.defaultAlgorithm` cho toàn bộ các component Ant Design (App, Modal, Message, Notification, Dropdown).
  - Ngăn ngừa hiện tượng nhấp nháy FOUC (Flash of Unstyled Content) bằng inline script trong `<head>` của `layout.tsx` và `suppressHydrationWarning` trên thẻ `<html>`.
  - Cập nhật toàn bộ các component cốt lõi (`Header.tsx`, `MobilePanel.tsx`, `Sidebar.tsx`, `Footer.tsx`, `DropdownMenu.tsx`, `globals.css`) sang các lớp `dark:...` theo đúng bảng màu Dark Slate (`#0F172A`, `#1E293B`, `#334155`, `#F8FAFC`).
  - Kiểm tra biên dịch `npm run build`: Thành công 100%, tạo thành công toàn bộ static và dynamic routes không có lỗi TypeScript.

## Spec Change Log

## Review Triage Log

| Finding | Verdict | Evidence / Disposition |
|---|---|---|
| FOUC prevention in SSR | low (resolved) | Đã nhúng inline script đồng bộ trong `<head>` của `layout.tsx` trước khi render body để kích hoạt class `dark` ngay lập tức nếu user đã chọn dark mode trước đó. |
| Hydration mismatch on `<html>` class | low (resolved) | Đã gắn `suppressHydrationWarning` trên thẻ `<html>` trong `layout.tsx`. |
| Ant Design modal/notification theme sync | low (resolved) | Đã bọc `ConfigProvider` quanh `App` component trong `providers.tsx` đảm bảo cả modal lẫn notification Ant Design đều thừa hưởng `darkAlgorithm`. |
| Screen reader accessibility for ThemeToggle | low (resolved) | Đã bổ sung đầy đủ `aria-label`, `title`, và `type="button"` trên `ThemeToggle.tsx`. |

## Design Notes

- **Dark Mode Visual Tokens:**
  - Canvas Nền: `#0F172A` (`dark:bg-slate-900` / `dark:bg-slate-950`)
  - Bề mặt Card/Box: `#1E293B` (`dark:bg-slate-800`)
  - Đường viền: `#334155` (`dark:border-slate-700`)
  - Chữ chính: `#F8FAFC` (`dark:text-slate-100`)
  - Chữ phụ: `#94A3B8` (`dark:text-slate-400`)
  - Màu thương hiệu Cam: `#FF6600` (giữ nguyên nhận diện)
- **Ant Design Integration:**
  - `theme={{ algorithm: theme === 'dark' ? antdTheme.darkAlgorithm : antdTheme.defaultAlgorithm, token: { colorPrimary: '#FF6600', borderRadius: 8 } }}`

## Verification

**Commands:**
- `npm run build` -- expected: Build frontend thành công không phát sinh lỗi TypeScript.

**Manual checks (if no CLI):**
- Bấm nút ThemeToggle trên Header: toàn trang chuyển đổi theme mượt mà.
- Kiểm tra `localStorage.getItem('newsroom_theme')` lưu đúng giá trị `dark` hoặc `light`.
- Reload trang xác nhận theme không bị reset hoặc nhấp nháy FOUC.
- Mở menu mobile kiểm tra toggle hoạt động đồng bộ.
