# 📢 Advertisement System - Quick Guide

## ⚡ Quick Start (5 phút)

### Bước 1: Tạo Quảng cáo qua MongoDB

```javascript
use newsroom

db.advertisements.insertOne({
  title: "Sidebar Banner Ad",
  description: "Sample advertisement",
  adType: "BANNER",
  position: "SIDEBAR_TOP",
  format: "IMAGE",
  imageUrl: "https://picsum.photos/300/250",
  targetUrl: "https://example.com",
  openInNewTab: true,
  width: "300px",
  height: "250px",
  active: true,
  priority: 5,
  impressions: 0,
  clicks: 0,
  displayFrequency: 1,
  maxDailyImpressions: 0,
  createdAt: new Date(),
  updatedAt: new Date()
})
```

### Bước 2: Xem Quảng cáo

Mở: `http://localhost:3000` → Quảng cáo sẽ hiển thị ở sidebar!

### Bước 3: Quản lý qua Admin

```
URL: http://localhost:3000/admin/advertisements
```

---

## 📍 Vị trí Quảng cáo

### Đã tích hợp sẵn:

✅ **SIDEBAR_TOP** - Đầu sidebar  
✅ **SIDEBAR_MIDDLE** - Giữa sidebar  
✅ **SIDEBAR_BOTTOM** - Cuối sidebar  
✅ **TOP_BANNER** - Banner đầu trang  
✅ **BOTTOM_BANNER** - Banner cuối trang  
✅ **IN_FEED** - Giữa danh sách bài viết (mỗi 10 bài)

---

## 🎨 3 Loại Quảng cáo

### 1️⃣ IMAGE - Banner hình ảnh

```javascript
{
  format: "IMAGE",
  imageUrl: "https://example.com/banner.jpg",
  targetUrl: "https://example.com/sale",
  width: "300px",
  height: "250px"
}
```

**Use case:** Banner quảng cáo thông thường

### 2️⃣ HTML - Custom HTML

```javascript
{
  format: "HTML",
  htmlContent: `
    <div style="background:#FF6600;color:white;padding:20px;text-align:center;border-radius:8px;">
      <h3 style="margin:0 0 10px 0;">🎉 Special Offer!</h3>
      <p style="margin:0 0 15px 0;">Get 50% off today only</p>
      <a href="https://example.com" 
         style="background:white;color:#FF6600;padding:10px 20px;text-decoration:none;border-radius:5px;display:inline-block;">
        Shop Now
      </a>
    </div>
  `
}
```

**Use case:** Quảng cáo tùy chỉnh với design riêng

### 3️⃣ SCRIPT - Google Ads, Facebook Ads

```javascript
{
  format: "SCRIPT",
  scriptCode: `
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <ins class="adsbygoogle"
         style="display:block"
         data-ad-client="ca-pub-XXXXXXXXXXXXXXXX"
         data-ad-slot="XXXXXXXXXX"
         data-ad-format="auto"
         data-full-width-responsive="true"></ins>
    <script>
      (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
  `
}
```

**Use case:** Google AdSense, Facebook Ads

---

## 🚀 Google Ads - Setup nhanh

### 1. Đăng ký Google AdSense

- Truy cập: https://www.google.com/adsense
- Đăng ký với domain của bạn
- Verify website

### 2. Tạo Ad Unit

1. Vào **Ads** → **Ad units**
2. Click **New ad unit**
3. Chọn **Display ads**
4. Copy ad code

### 3. Thêm vào NewsRoom

**Via Admin Panel:**
```
1. Login: /admin/login
2. Vào: Advertisements → Create
3. Fill form:
   - Title: Google AdSense - Sidebar
   - Position: SIDEBAR_TOP
   - Format: SCRIPT
   - Script Code: [Paste Google ad code]
   - Active: Yes
4. Save
```

**Xong!** Quảng cáo Google sẽ hiển thị.

---

## 📱 Facebook Ads - Setup nhanh

### Option 1: Facebook Audience Network

1. Đăng ký tại [Facebook Business](https://business.facebook.com)
2. Tạo Placement
3. Copy placement code
4. Add vào NewsRoom như Google Ads

### Option 2: Facebook Pixel (Remarketing)

**Thêm vào `frontend/src/app/layout.tsx`:**

```typescript
import Script from 'next/script';

// Add inside <body>
<Script id="facebook-pixel" strategy="afterInteractive">
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

## 📊 Tracking & Analytics

### Tự động Track

Hệ thống tự động track:
- ✅ **Impressions**: Khi ad hiển thị (50% viewport)
- ✅ **Clicks**: Khi user click
- ✅ **CTR**: Click-Through Rate tự tính

### Xem Stats

```
Admin Panel → Advertisements
```

Bạn sẽ thấy:
- Total Impressions
- Total Clicks
- CTR per ad
- Summary statistics

---

## 💡 Examples

### Banner 300x250 (Standard)

```javascript
db.advertisements.insertOne({
  title: "Standard Banner",
  position: "SIDEBAR_TOP",
  format: "IMAGE",
  imageUrl: "https://via.placeholder.com/300x250",
  targetUrl: "https://example.com",
  width: "300px",
  height: "250px",
  active: true,
  priority: 5,
  createdAt: new Date()
})
```

### In-Feed Promotion

```javascript
db.advertisements.insertOne({
  title: "Subscribe Promotion",
  position: "IN_FEED",
  format: "HTML",
  htmlContent: `
    <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
                color: white; 
                padding: 30px; 
                border-radius: 10px; 
                text-align: center;">
      <h2 style="font-size: 24px; margin-bottom: 10px;">📰 Subscribe to Our Newsletter</h2>
      <p style="margin-bottom: 20px;">Get daily news delivered to your inbox</p>
      <a href="/subscribe" 
         style="background: white; 
                color: #667eea; 
                padding: 12px 30px; 
                text-decoration: none; 
                border-radius: 5px; 
                font-weight: bold;
                display: inline-block;">
        Subscribe Now →
      </a>
    </div>
  `,
  active: true,
  priority: 3,
  createdAt: new Date()
})
```

### Google AdSense

```javascript
db.advertisements.insertOne({
  title: "Google AdSense - Top",
  position: "SIDEBAR_TOP",
  format: "SCRIPT",
  scriptCode: `
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-XXXXXXXXXXXXXXXX" crossorigin="anonymous"></script>
    <ins class="adsbygoogle"
         style="display:block"
         data-ad-client="ca-pub-XXXXXXXXXXXXXXXX"
         data-ad-slot="XXXXXXXXXX"
         data-ad-format="auto"
         data-full-width-responsive="true"></ins>
    <script>
      (adsbygoogle = window.adsbygoogle || []).push({});
    </script>
  `,
  width: "300px",
  height: "250px",
  active: true,
  priority: 10,
  createdAt: new Date()
})
```

---

## 🎯 Best Positions

### Hiệu quả cao:
1. **SIDEBAR_TOP** - Luôn nhìn thấy
2. **IN_FEED** - Trong luồng đọc
3. **SIDEBAR_MIDDLE** - Khi scroll

### Ít xâm phạm:
1. **SIDEBAR_BOTTOM** - Cuối sidebar
2. **BOTTOM_BANNER** - Cuối trang
3. **FOOTER** - Footer area

---

## ⚙️ Configuration

### Thay đổi tần suất In-Feed Ads

**File:** `frontend/src/app/page.tsx`

```typescript
<LatestNewsWithAds 
  news={latestNews} 
  adFrequency={10}  // Change this: 5, 10, 15, 20
/>
```

### Ad Rotation Time

**File:** `frontend/src/components/ads/AdContainer.tsx`

```typescript
setTimeout(() => {
  setCurrentAdIndex((prev) => prev + 1);
}, 30000); // Change this: 30000 = 30s
```

---

## ✅ Checklist

### Setup
- [ ] Backend running
- [ ] Frontend running
- [ ] Created sample ads in MongoDB

### Google Ads (Optional)
- [ ] AdSense account created
- [ ] Site verified
- [ ] Ad units created
- [ ] Ad code copied to NewsRoom

### Facebook Ads (Optional)
- [ ] Facebook Business account
- [ ] Placement created
- [ ] Code integrated

### Testing
- [ ] Ads display correctly
- [ ] Click tracking works
- [ ] Impression tracking works
- [ ] Stats show in Admin Panel

---

## 🐛 Troubleshooting

### Ads không hiển thị?
1. Check MongoDB có ads với `active: true`
2. Check position name đúng
3. Check browser console for errors
4. Verify API endpoint: `/api/advertisements/position/{position}`

### Google Ads không load?
1. Verify Publisher ID
2. Check AdSense account approved
3. Wait 24-48h for new accounts
4. Check browser ad-blockers

### Tracking không hoạt động?
1. Check browser console
2. Verify API endpoints
3. Check MongoDB ads collection

---

## 📞 Quick Help

| Issue | Solution |
|-------|----------|
| Ads không hiển thị | Check MongoDB + active status |
| Google Ads blank | Wait for approval (24-48h) |
| Tracking không work | Check API endpoints |
| Ads quá nhiều | Giảm adFrequency hoặc disable positions |

---

## 🎉 Done!

Bạn đã có hệ thống quảng cáo hoàn chỉnh với:
- ✅ Admin management
- ✅ Multiple positions
- ✅ Tracking (impressions, clicks)
- ✅ Google Ads support
- ✅ Facebook Ads support
- ✅ In-feed ads (every 10 posts)

**Happy Advertising! 💰**

---

Xem chi tiết: [ADVERTISEMENT_SYSTEM_README.md](ADVERTISEMENT_SYSTEM_README.md)

