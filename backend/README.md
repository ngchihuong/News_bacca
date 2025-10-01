# NewsRoom Backend API

Backend REST API cho hệ thống NewsRoom được xây dựng với Java Spring Boot và MongoDB.

## 🚀 Bắt đầu Nhanh

### Yêu cầu
- Java JDK 21+
- Maven 3.6+
- MongoDB 5.0+

### Cài đặt và Chạy

1. **Khởi động MongoDB:**
```bash
mongod
```

2. **Clone và chạy ứng dụng:**
```bash
cd backend
./mvnw spring-boot:run
```

API sẽ chạy tại: `http://localhost:8080/api`

## 📋 Dependencies

- **Spring Boot Starter Web** - RESTful API
- **Spring Boot Starter Data MongoDB** - MongoDB integration
- **Spring Boot Starter Security** - Security framework
- **Spring Boot Starter Validation** - Input validation
- **JWT (jjwt)** - JSON Web Token authentication
- **Lombok** - Reduce boilerplate code
- **Spring Boot DevTools** - Development tools

## 🗂️ Cấu trúc Dự án

```
src/main/java/com/newsroom/
├── config/
│   ├── CorsConfig.java           # CORS configuration
│   └── SecurityConfig.java        # Security configuration
├── controller/
│   ├── NewsController.java        # News API endpoints
│   └── CategoryController.java    # Category API endpoints
├── dto/
│   ├── NewsDTO.java              # News data transfer object
│   ├── CategoryDTO.java          # Category data transfer object
│   ├── AuthRequest.java          # Authentication request
│   └── AuthResponse.java         # Authentication response
├── model/
│   ├── News.java                 # News entity
│   ├── Category.java             # Category entity
│   ├── Tag.java                  # Tag entity
│   ├── User.java                 # User entity
│   └── Comment.java              # Comment entity
├── repository/
│   ├── NewsRepository.java       # News MongoDB repository
│   ├── CategoryRepository.java   # Category MongoDB repository
│   ├── TagRepository.java        # Tag MongoDB repository
│   ├── UserRepository.java       # User MongoDB repository
│   └── CommentRepository.java    # Comment MongoDB repository
├── service/
│   ├── NewsService.java          # News business logic
│   └── CategoryService.java      # Category business logic
└── NewsRoomApplication.java      # Main application class
```

## 🔌 API Endpoints

### News Endpoints

#### Lấy tất cả tin tức (Public)
```http
GET /api/news?page=0&size=10
```

#### Lấy tin theo slug (Public)
```http
GET /api/news/slug/{slug}
```

#### Lấy tin nổi bật (Public)
```http
GET /api/news/featured?page=0&size=10
```

#### Lấy tin xu hướng (Public)
```http
GET /api/news/trending?page=0&size=10
```

#### Lấy tin theo danh mục (Public)
```http
GET /api/news/category/{categoryId}?page=0&size=10
```

#### Tạo tin mới (Authenticated)
```http
POST /api/news
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "Breaking News",
  "excerpt": "Short description",
  "content": "Full article content",
  "imageUrl": "https://example.com/image.jpg",
  "categoryId": "category-id",
  "tagIds": ["tag-id-1", "tag-id-2"],
  "status": "PUBLISHED",
  "featured": false,
  "trending": false
}
```

#### Cập nhật tin (Authenticated)
```http
PUT /api/news/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "Updated Title",
  "excerpt": "Updated excerpt",
  "content": "Updated content",
  "imageUrl": "https://example.com/new-image.jpg",
  "categoryId": "category-id",
  "status": "PUBLISHED"
}
```

#### Xóa tin (Authenticated)
```http
DELETE /api/news/{id}
Authorization: Bearer {token}
```

### Category Endpoints

#### Lấy tất cả danh mục (Public)
```http
GET /api/categories
```

#### Lấy danh mục active (Public)
```http
GET /api/categories/active
```

#### Lấy danh mục theo slug (Public)
```http
GET /api/categories/slug/{slug}
```

#### Tạo danh mục mới (Authenticated)
```http
POST /api/categories
Content-Type: application/json

{
  "name": "Technology",
  "description": "Tech news and updates",
  "imageUrl": "https://example.com/tech.jpg"
}
```

## ⚙️ Configuration

### application.properties

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/newsroom
spring.data.mongodb.database=newsroom

# JWT Configuration
app.jwt.secret=YourSecretKeyForJWTTokenGenerationShouldBeLongAndSecure
app.jwt.expiration=86400000

# CORS Configuration
app.cors.allowed-origins=http://localhost:3000

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.com.newsroom=DEBUG
```

## 🗃️ Database Schema

### News Collection
```javascript
{
  "_id": ObjectId,
  "title": String,
  "slug": String,
  "excerpt": String,
  "content": String,
  "imageUrl": String,
  "category": DBRef,
  "tags": [DBRef],
  "author": DBRef,
  "viewCount": Number,
  "featured": Boolean,
  "trending": Boolean,
  "status": String, // DRAFT, PUBLISHED, ARCHIVED
  "publishedAt": Date,
  "createdAt": Date,
  "updatedAt": Date
}
```

### Category Collection
```javascript
{
  "_id": ObjectId,
  "name": String,
  "slug": String,
  "description": String,
  "imageUrl": String,
  "active": Boolean,
  "createdAt": Date,
  "updatedAt": Date
}
```

## 🔐 Security

- CORS được cấu hình cho phép frontend (localhost:3000)
- JWT authentication sẵn sàng cho các endpoints protected
- Password encryption với BCrypt
- Stateless session management

## 🧪 Testing

```bash
# Chạy tất cả tests
./mvnw test

# Chạy với coverage
./mvnw test jacoco:report
```

## 📦 Build & Deploy

### Development
```bash
./mvnw spring-boot:run
```

### Production Build
```bash
# Build JAR file
./mvnw clean package

# Run JAR
java -jar target/newsroom-backend-1.0.0.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/newsroom-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
docker build -t newsroom-backend .
docker run -p 8080:8080 newsroom-backend
```

## 📝 Notes

- Tất cả endpoints news và categories đều public để dễ dàng phát triển
- JWT authentication đã được cấu hình nhưng chưa bắt buộc
- Có thể dễ dàng thêm authentication bằng cách uncomment trong SecurityConfig

## 🐛 Troubleshooting

### MongoDB Connection Issues
```bash
# Kiểm tra MongoDB đang chạy
mongosh

# Nếu lỗi, khởi động lại MongoDB
sudo systemctl restart mongod
```

### Port Already in Use
```bash
# Tìm process đang dùng port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

## 📞 Support

Nếu gặp vấn đề, vui lòng tạo Issue trên GitHub repository.

