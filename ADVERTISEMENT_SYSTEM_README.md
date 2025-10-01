# 📢 Advertisement System Documentation

## 🎯 Tổng quan

Hệ thống quảng cáo của NewsRoom cho phép:
- ✅ Admin quản lý quảng cáo (CRUD)
- ✅ Hiển thị quảng cáo ở nhiều vị trí khác nhau
- ✅ Tracking (impressions, clicks, CTR)
- ✅ Quảng cáo giữa các bài viết (mỗi 10 bài)
- ✅ Hỗ trợ nhiều format (Image, HTML, Script)
- ✅ Tích hợp Google Ads & Facebook Ads

---

## 🏗️ Kiến trúc

### Backend Components

```
backend/
├── model/Advertisement.java          # Advertisement entity
├── repository/AdvertisementRepository.java
├── service/AdvertisementService.java
├── controller/
│   ├── AdvertisementController.java      # Public API
│   └── admin/AdminAdvertisementController.java  # Admin API
└── dto/AdvertisementDTO.java
```

### Frontend Components

```
frontend/
├── types/advertisement.ts
├── lib/adApi.ts
├── components/ads/
│   ├── AdUnit.tsx              # Renders single ad
│   ├── AdContainer.tsx         # Loads ads by position
│   ├── InFeedAd.tsx           # In-feed ads
│   └── LatestNewsWithAds.tsx  # News list with ads
└── app/admin/advertisements/
    └── page.tsx               # Admin management UI
```

---

## 📊 Advertisement Model

### Fields

| Field | Type | Description |
|-------|------|-------------|
| id | String | Unique identifier |
| title | String | Ad title |
| description | String | Ad description |
| adType | String | BANNER, SIDEBAR, IN_FEED, POPUP |
| position | String | Vị trí hiển thị |
| format | String | IMAGE, HTML, SCRIPT |
| imageUrl | String | For IMAGE format |
| htmlContent | String | For HTML format |
| scriptCode | String | For SCRIPT format (Google Ads, FB) |
| targetUrl | String | Click destination |
| width/height | String | Dimensions |
| startDate/endDate | DateTime | Scheduling |
| active | Boolean | Active status |
| priority | Integer | Higher = more priority |
| impressions | Long | View count |
| clicks | Long | Click count |

### Ad Positions

```typescript
- SIDEBAR_TOP       // Top của sidebar
- SIDEBAR_MIDDLE    // Giữa sidebar
- SIDEBAR_BOTTOM    // Cuối sidebar
- TOP_BANNER        // Banner trên đầu trang
- BOTTOM_BANNER     // Banner cuối trang
- IN_FEED           // Giữa các bài viết
- FOOTER            // Footer area
```

### Ad Formats

```typescript
- IMAGE   // Image banner với link
- HTML    // Custom HTML content
- SCRIPT  // Script tags (Google Ads, Facebook Ads)
```

---

## 🔌 API Endpoints

### Public API (Frontend)

```http
GET /api/advertisements/position/{position}
# Lấy ads theo vị trí
Response: Advertisement[]

POST /api/advertisements/{id}/impression
# Track khi ad được hiển thị

POST /api/advertisements/{id}/click
# Track khi ad được click
```

### Admin API (Protected)

```http
GET /api/admin/advertisements
# Lấy tất cả ads

GET /api/admin/advertisements/{id}
# Lấy ad theo ID

POST /api/admin/advertisements
# Tạo ad mới
Body: AdvertisementDTO

PUT /api/admin/advertisements/{id}
# Cập nhật ad
Body: AdvertisementDTO

DELETE /api/admin/advertisements/{id}
# Xóa ad

GET /api/admin/advertisements/active
# Lấy ads đang active
```

---

## 💻 Sử dụng trong Code

### 1. Hiển thị Ad theo Position

```typescript
import AdContainer from '@/components/ads/AdContainer';

// Trong component
<AdContainer position="SIDEBAR_TOP" />
<AdContainer position="SIDEBAR_MIDDLE" />
<AdContainer position="SIDEBAR_BOTTOM" />
```

### 2. In-Feed Ads (giữa bài viết)

```typescript
import LatestNewsWithAds from '@/components/LatestNewsWithAds';

<LatestNewsWithAds 
  news={newsArray} 
  adFrequency={10}  // Show ad every 10 posts
/>
```

### 3. Tạo Ad qua Admin

```typescript
const adData = {
  title: "Summer Sale",
  description: "50% off all items",
  adType: "BANNER",
  position: "SIDEBAR_TOP",
  format: "IMAGE",
  imageUrl: "https://example.com/ad.jpg",
  targetUrl: "https://example.com/sale",
  width: "300px",
  height: "250px",
  active: true,
  priority: 5
};

await adminAdApi.create(adData);
```

---

## 🎨 Ad Formats Chi tiết

### 1. IMAGE Format

```typescript
{
  format: "IMAGE",
  imageUrl: "https://example.com/banner.jpg",
  targetUrl: "https://example.com/product",
  width: "300px",
  height: "250px"
}
```

### 2. HTML Format

```typescript
{
  format: "HTML",
  htmlContent: `
    <div style="padding: 20px; background: #f0f0f0;">
      <h3>Special Offer!</h3>
      <p>Get 50% off today</p>
      <a href="https://example.com">Shop Now</a>
    </div>
  `
}
```

### 3. SCRIPT Format (Google Ads)

```typescript
{
  format: "SCRIPT",
  scriptCode: `
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <ins class="adsbygoogle"
         style="display:block"
         data-ad-client="ca-pub-XXXXXXXXXXXXXXXX"
         data-ad-slot="XXXXXXXXXX"
         data-ad-format="auto"></ins>
    <script>
      (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
  `
}
```

---

## 🎯 Google Ads Integration

### Bước 1: Tạo Google AdSense Account

1. Đăng ký tại [Google AdSense](https://www.google.com/adsense/)
2. Verify website
3. Lấy Publisher ID (ca-pub-XXXXXXXXXXXXXXXX)

### Bước 2: Tạo Ad Unit

1. Login AdSense
2. Ads → Ad units → Create new ad unit
3. Chọn Display ads
4. Copy ad code

### Bước 3: Thêm vào NewsRoom

```javascript
// Via Admin Panel
{
  title: "Google Display Ad - Sidebar",
  position: "SIDEBAR_TOP",
  format: "SCRIPT",
  scriptCode: `
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <ins class="adsbygoogle"
         style="display:block"
         data-ad-client="ca-pub-YOUR_PUBLISHER_ID"
         data-ad-slot="YOUR_AD_SLOT_ID"
         data-ad-format="auto"
         data-full-width-responsive="true"></ins>
    <script>
      (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
  `,
  width: "300px",
  height: "250px",
  active: true
}
```

---

## 📱 Facebook Ads Integration

### Option 1: Facebook Audience Network

```javascript
{
  title: "Facebook Audience Network",
  position: "SIDEBAR_MIDDLE",
  format: "SCRIPT",
  scriptCode: `
    <div id="fb-root"></div>
    <script async defer crossorigin="anonymous" 
      src="https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v12.0">
    </script>
    <div class="fb-ad" 
         data-placementid="YOUR_PLACEMENT_ID" 
         data-format="300x250"
         data-testmode="false">
    </div>
  `
}
```

### Option 2: Facebook Pixel + Remarketing

```html
<!-- Add to frontend/src/app/layout.tsx -->
<Script id="facebook-pixel">
  {`
    !function(f,b,e,v,n,t,s)
    {if(f.fbq)return;n=f.fbq=function(){n.callMethod?
    n.callMethod.apply(n,arguments):n.queue.push(arguments)};
    if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
    n.queue=[];t=b.createElement(e);t.async=!0;
    t.src=v;s=b.getElementsByTagName(e)[0];
    s.parentNode.insertBefore(t,s)}(window, document,'script',
    'https://connect.facebook.net/en_US/fbevents.js');
    fbq('init', 'YOUR_PIXEL_ID');
    fbq('track', 'PageView');
  `}
</Script>
```

---

## 📈 Tracking & Analytics

### Automatic Tracking

Hệ thống tự động track:
- **Impressions**: Khi ad hiển thị (50% viewport)
- **Clicks**: Khi user click vào ad

### View Stats

```
Admin Panel → Advertisements
```

Metrics hiển thị:
- Total Impressions
- Total Clicks
- CTR (Click-Through Rate)
- Per-ad statistics

### Export Data (Future)

```typescript
// API endpoint cho export
GET /api/admin/advertisements/stats/export
```

---

## 🎨 Best Practices

### 1. Ad Placement

✅ **Tốt:**
- Sidebar (không che nội dung chính)
- Giữa bài viết (mỗi 10-15 posts)
- Footer
- Top banner (nhỏ gọn)

❌ **Tránh:**
- Popup che toàn màn hình
- Quá nhiều ads cùng lúc
- Ads che menu navigation

### 2. Ad Sizes (Chuẩn IAB)

```
- 300x250 (Medium Rectangle) - Phổ biến nhất
- 728x90 (Leaderboard) - Top banner
- 300x600 (Half Page) - Sidebar
- 320x50 (Mobile Banner)
- 336x280 (Large Rectangle)
```

### 3. Performance

```typescript
// Lazy load ads
<AdContainer 
  position="SIDEBAR_BOTTOM"
  className="lazy-load"
/>

// Limit ads per page
adFrequency={10}  // 1 ad per 10 posts
```

### 4. User Experience

- ✅ Label ads clearly ("Advertisement")
- ✅ Make ads distinguishable from content
- ✅ Reasonable ad frequency
- ✅ Fast loading ads
- ❌ Don't use deceptive ads
- ❌ Don't auto-play sound

---

## 🔧 Configuration

### Ad Rotation

```typescript
// In AdContainer.tsx
const rotationInterval = 30000; // 30 seconds

useEffect(() => {
  const timer = setInterval(() => {
    setCurrentAdIndex(prev => prev + 1);
  }, rotationInterval);
  
  return () => clearInterval(timer);
}, []);
```

### Ad Scheduling

```typescript
{
  startDate: "2025-10-01T00:00:00",
  endDate: "2025-10-31T23:59:59",
  active: true
}
```

### Priority System

```
priority: 10  // Highest priority
priority: 5   // Medium
priority: 0   // Default/Lowest
```

---

## 📱 Responsive Ads

### Auto-resize

```javascript
{
  width: "100%",
  height: "auto",
  // Or use responsive HTML
  htmlContent: `
    <div style="max-width: 100%; height: auto;">
      <img src="ad.jpg" style="width: 100%; height: auto;" />
    </div>
  `
}
```

### Mobile-specific Ads

```typescript
// Create separate ads for mobile
{
  position: "MOBILE_BANNER",
  width: "320px",
  height: "50px"
}
```

---

## 🚀 Deployment Checklist

- [ ] Create ad placements in Admin
- [ ] Set up Google AdSense (if using)
- [ ] Configure Facebook Ads (if using)
- [ ] Test ad display on all pages
- [ ] Verify tracking (impressions/clicks)
- [ ] Check mobile responsiveness
- [ ] Monitor ad performance
- [ ] Optimize ad positions based on CTR

---

## 🔒 Security & Privacy

### Content Security Policy

```typescript
// Add to next.config.js
const cspHeader = `
  script-src 'self' 'unsafe-inline' 'unsafe-eval' 
    https://pagead2.googlesyndication.com
    https://connect.facebook.net;
  frame-src 'self' 
    https://googleads.g.doubleclick.net
    https://www.facebook.com;
`;
```

### Privacy Policy

Cần thêm vào Privacy Policy:
- Sử dụng cookies cho ads
- Third-party ad providers
- User tracking
- Opt-out options

---

## 📊 Sample Ad Data

### MongoDB Sample

```javascript
db.advertisements.insertMany([
  {
    title: "Sidebar Banner - Tech Products",
    description: "Promote tech products",
    adType: "BANNER",
    position: "SIDEBAR_TOP",
    format: "IMAGE",
    imageUrl: "https://example.com/tech-banner.jpg",
    targetUrl: "https://example.com/tech",
    openInNewTab: true,
    width: "300px",
    height: "250px",
    active: true,
    priority: 5,
    impressions: 0,
    clicks: 0,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    title: "In-Feed Promotion",
    description: "Special offer between posts",
    adType: "IN_FEED",
    position: "IN_FEED",
    format: "HTML",
    htmlContent: `
      <div style="padding:20px;background:#f8f9fa;border-radius:8px;text-align:center;">
        <h3 style="color:#FF6600;margin-bottom:10px;">Special Offer!</h3>
        <p>Get 50% off on all premium subscriptions</p>
        <a href="https://example.com/subscribe" 
           style="display:inline-block;padding:10px 20px;background:#FF6600;color:white;text-decoration:none;border-radius:5px;">
          Subscribe Now
        </a>
      </div>
    `,
    active: true,
    priority: 3,
    createdAt: new Date(),
    updatedAt: new Date()
  }
])
```

---

## 💡 Tips & Tricks

### 1. A/B Testing

Create multiple ads for same position with different priorities, rotate them to test performance.

### 2. Seasonal Ads

Use startDate/endDate for:
- Holiday promotions
- Limited time offers
- Event-based campaigns

### 3. Target by Category

Create category-specific ads by checking page context in AdContainer.

### 4. Performance Optimization

```typescript
// Lazy load ad scripts
const loadAdScript = async () => {
  if (typeof window !== 'undefined') {
    const script = document.createElement('script');
    script.src = 'https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js';
    script.async = true;
    document.body.appendChild(script);
  }
};
```

---

## 📞 Support

Xem thêm:
- [Google AdSense Help](https://support.google.com/adsense)
- [Facebook Audience Network](https://www.facebook.com/audiencenetwork)
- [IAB Ad Standards](https://www.iab.com/guidelines/iab-standard-ad-unit-portfolio/)

---

**Version**: 1.0.0  
**Last Updated**: October 2025  
**Status**: ✅ Production Ready

