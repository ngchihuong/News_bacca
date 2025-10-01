# 🚀 Hướng dẫn Khởi động Nhanh

## Bước 1: Cài đặt Môi trường

### Yêu cầu
- ✅ Java JDK 21+
- ✅ Node.js 20.14.0+
- ✅ MongoDB 5.0+
- ✅ Maven 3.6+ (hoặc dùng wrapper)

### Kiểm tra Version
```bash
# Kiểm tra Java (cần version 21)
java -version

# Kiểm tra Node.js (cần version 20.14.0+)
node -v

# Kiểm tra npm (cần version 10+)
npm -v

# Kiểm tra MongoDB
mongod --version
```

**📌 Nếu chưa cài đặt đúng version, xem file [VERSION_INFO.md](VERSION_INFO.md) để hướng dẫn chi tiết.**

## Bước 2: Khởi động MongoDB

### Windows
```powershell
# Mở Command Prompt hoặc PowerShell
mongod
```

### macOS
```bash
# Dùng Homebrew
brew services start mongodb-community

# Hoặc chạy trực tiếp
mongod --config /usr/local/etc/mongod.conf
```

### Linux
```bash
sudo systemctl start mongod
# hoặc
sudo service mongod start
```

### Kiểm tra MongoDB đang chạy
```bash
mongosh
# Nếu kết nối được là OK!
```

## Bước 3: Khởi động Backend

```bash
# Mở Terminal/CMD thứ nhất
cd backend

# Windows
mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

### Kiểm tra Backend
Mở trình duyệt: http://localhost:8080/api/categories

Nếu thấy `[]` hoặc dữ liệu JSON là thành công!

## Bước 4: Khởi động Frontend

```bash
# Mở Terminal/CMD thứ hai
cd frontend

# Cài đặt dependencies (chỉ lần đầu)
npm install

# Chạy development server
npm run dev
```

### Kiểm tra Frontend
Mở trình duyệt: http://localhost:3000

## Bước 5: Thêm Dữ liệu Mẫu (Optional)

### Sử dụng MongoDB Compass hoặc mongosh

```javascript
// Kết nối MongoDB
use newsroom

// Thêm categories
db.categories.insertMany([
  {
    name: "Technology",
    slug: "technology",
    description: "Latest tech news",
    imageUrl: "https://picsum.photos/500/80?random=1",
    active: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: "Business",
    slug: "business",
    description: "Business updates",
    imageUrl: "https://picsum.photos/500/80?random=2",
    active: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: "Sports",
    slug: "sports",
    description: "Sports highlights",
    imageUrl: "https://picsum.photos/500/80?random=3",
    active: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    name: "Entertainment",
    slug: "entertainment",
    description: "Entertainment news",
    imageUrl: "https://picsum.photos/500/80?random=4",
    active: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
])

// Lấy category ID
var techCategory = db.categories.findOne({slug: "technology"})

// Thêm sample news
db.news.insertOne({
  title: "Breaking: New Technology Breakthrough",
  slug: "breaking-new-technology-breakthrough",
  excerpt: "Scientists discover revolutionary new technology that will change everything.",
  content: "<p>This is a sample news article content. Scientists have made an incredible breakthrough that promises to revolutionize the tech industry.</p><p>The discovery was made after years of research and development.</p>",
  imageUrl: "https://picsum.photos/700/435?random=1",
  category: techCategory._id,
  tags: [],
  viewCount: 0,
  featured: true,
  trending: true,
  status: "PUBLISHED",
  publishedAt: new Date(),
  createdAt: new Date(),
  updatedAt: new Date()
})
```

## Bước 6: Truy cập Ứng dụng

### URLs
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **MongoDB**: mongodb://localhost:27017

## 🐛 Xử lý Lỗi Thường Gặp

### Lỗi: "Port 8080 already in use"
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:8080 | xargs kill -9
```

### Lỗi: "Port 3000 already in use"
```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:3000 | xargs kill -9

# Hoặc dùng port khác
npm run dev -- -p 3001
```

### Lỗi: MongoDB không kết nối được
```bash
# Kiểm tra MongoDB đang chạy
mongosh

# Nếu lỗi, khởi động lại
# Windows
net start MongoDB

# macOS
brew services restart mongodb-community

# Linux
sudo systemctl restart mongod
```

### Lỗi: "Cannot find module" trong Frontend
```bash
cd frontend
rm -rf node_modules
rm package-lock.json
npm install
```

### Lỗi: CORS trong Browser Console
Kiểm tra file `backend/src/main/resources/application.properties`:
```properties
app.cors.allowed-origins=http://localhost:3000
```

## 📝 Lưu ý Quan trọng

1. **Backend phải chạy trước Frontend** để API sẵn sàng
2. **MongoDB phải chạy trước Backend**
3. Cổng mặc định:
   - MongoDB: 27017
   - Backend: 8080
   - Frontend: 3000
4. Đảm bảo không có ứng dụng nào đang chiếm các cổng này

## 🎯 Checklist Khởi động

- [ ] MongoDB đang chạy (mongosh kết nối được)
- [ ] Backend đang chạy (http://localhost:8080/api/categories)
- [ ] Frontend đang chạy (http://localhost:3000)
- [ ] Đã thêm dữ liệu mẫu (optional)
- [ ] Không có lỗi trong console

## 🆘 Cần Trợ giúp?

1. Xem file README.md chính
2. Xem backend/README.md cho Backend
3. Xem frontend/README.md cho Frontend
4. Tạo Issue trên GitHub

---

**Chúc bạn code vui vẻ! 🎉**

