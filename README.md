# NewsRoom - News Management System

Hệ thống quản lý tin tức hoàn chỉnh được xây dựng với **Java Spring Boot** (Backend), **Next.js** (Frontend) và **MongoDB** (Database).

> 🎉 **Version 1.3.0 Released!** - Advertisement System Added! 📢💰  
> 📌 [Ad System Guide](ADVERTISEMENT_QUICK_GUIDE.md) | [Admin Panel](ADMIN_PANEL_README.md) | [All Features](FEATURES_SUMMARY.md)

## 🏗️ Kiến trúc Hệ thống

```
NewsRoom/
├── backend/          # Java Spring Boot API
├── frontend/         # Next.js Application
└── img/             # Template assets (legacy)
```

## 🚀 Công nghệ Sử dụng

### Backend
- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data MongoDB**
- **Spring Security**
- **JWT Authentication**
- **Maven**

### Frontend
- **Next.js 14**
- **React 18**
- **TypeScript**
- **Tailwind CSS**
- **Axios** cho API calls
- **React Icons**
- **Node.js 20.14.0**

### Database
- **MongoDB**

## 📋 Yêu cầu Hệ thống

- Java JDK 21 hoặc cao hơn
- Node.js 20.14.0 hoặc cao hơn
- MongoDB 5.0 hoặc cao hơn
- Maven 3.6+ (hoặc sử dụng Maven Wrapper đi kèm)

## 🔧 Cài đặt và Chạy

### 1. Khởi động MongoDB

```bash
# Trên Windows
mongod

# Trên macOS/Linux
sudo systemctl start mongod
# hoặc
brew services start mongodb-community
```

### 2. Chạy Backend

```bash
cd backend

# Sử dụng Maven Wrapper (khuyến nghị)
./mvnw spring-boot:run

# Hoặc nếu đã cài Maven
mvn spring-boot:run
```

Backend sẽ chạy tại: `http://localhost:8080/api`

### 3. Chạy Frontend

```bash
cd frontend

# Cài đặt dependencies
npm install

# Chạy development server
npm run dev
```

Frontend sẽ chạy tại: `http://localhost:3000`

## 📁 Cấu trúc Dự án

### Backend Structure
```
backend/
├── src/main/java/com/newsroom/
│   ├── model/              # Entities (News, Category, Tag, User, Comment)
│   ├── repository/         # MongoDB Repositories
│   ├── service/            # Business Logic
│   ├── controller/         # REST API Controllers
│   ├── dto/               # Data Transfer Objects
│   ├── config/            # Configuration (Security, CORS)
│   └── NewsRoomApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

### Frontend Structure
```
frontend/
├── src/
│   ├── app/               # Next.js App Router
│   │   ├── layout.tsx
│   │   ├── page.tsx       # Home page
│   │   ├── news/[slug]/   # News detail page
│   │   └── category/[slug]/ # Category page
│   ├── components/        # React Components
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   ├── MainSlider.tsx
│   │   ├── FeaturedNews.tsx
│   │   ├── CategorySection.tsx
│   │   ├── LatestNews.tsx
│   │   └── Sidebar.tsx
│   ├── lib/              # Utilities
│   │   └── api.ts        # API client
│   └── types/            # TypeScript types
│       └── index.ts
├── package.json
├── tsconfig.json
├── next.config.js
└── tailwind.config.js
```

## 🔌 API Endpoints

### News API
- `GET /api/news` - Lấy tất cả tin tức (có phân trang)
- `GET /api/news/{id}` - Lấy tin tức theo ID
- `GET /api/news/slug/{slug}` - Lấy tin tức theo slug
- `GET /api/news/category/{categoryId}` - Lấy tin tức theo danh mục
- `GET /api/news/featured` - Lấy tin nổi bật
- `GET /api/news/trending` - Lấy tin xu hướng
- `POST /api/news` - Tạo tin tức mới (yêu cầu xác thực)
- `PUT /api/news/{id}` - Cập nhật tin tức (yêu cầu xác thực)
- `DELETE /api/news/{id}` - Xóa tin tức (yêu cầu xác thực)

### Category API
- `GET /api/categories` - Lấy tất cả danh mục
- `GET /api/categories/active` - Lấy danh mục đang hoạt động
- `GET /api/categories/{id}` - Lấy danh mục theo ID
- `GET /api/categories/slug/{slug}` - Lấy danh mục theo slug
- `POST /api/categories` - Tạo danh mục mới
- `PUT /api/categories/{id}` - Cập nhật danh mục
- `DELETE /api/categories/{id}` - Xóa danh mục

## ⚙️ Cấu hình

### Backend Configuration
Chỉnh sửa `backend/src/main/resources/application.properties`:

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/newsroom

# Server Port
server.port=8080

# CORS
app.cors.allowed-origins=http://localhost:3000
```

### Frontend Configuration
Tạo file `frontend/.env.local`:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## 🎨 Features

### Đã Hoàn thành
✅ Backend API với Spring Boot và MongoDB  
✅ Frontend với Next.js và TypeScript  
✅ Responsive design với Tailwind CSS  
✅ Trang chủ với tin nổi bật, xu hướng  
✅ Trang chi tiết tin tức  
✅ Trang danh mục  
✅ Tìm kiếm và phân trang  
✅ CORS configuration  
✅ Security configuration  

### Tính năng Có thể Mở rộng
- 🔐 Xác thực người dùng (JWT đã sẵn sàng)
- 💬 Hệ thống bình luận
- 📧 Newsletter subscription
- 🖼️ Upload hình ảnh
- 📊 Dashboard quản trị
- 🔍 Full-text search
- 🌐 Multi-language support
- 📱 Progressive Web App (PWA)

## 🧪 Testing

### Backend
```bash
cd backend
./mvnw test
```

### Frontend
```bash
cd frontend
npm test
```

## 📦 Build cho Production

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/newsroom-backend-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
npm start
```

## 🤝 Đóng góp

Mọi đóng góp đều được chào đón! Vui lòng tạo Pull Request hoặc Issue.

## 📄 License

Dự án này sử dụng template từ [HTML Codex](https://htmlcodex.com) và được chuyển đổi thành hệ thống full-stack.

## 📞 Liên hệ

Nếu có bất kỳ câu hỏi nào, vui lòng tạo Issue trên GitHub repository.

---

**Happy Coding! 🚀**

