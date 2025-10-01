# 📂 Cấu trúc Dự án NewsRoom

## 🗂️ Tổng quan Thư mục

```
NewsRoom/
│
├── 📁 backend/                    # Java Spring Boot API (Port 8080)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/newsroom/
│   │   │   │   ├── 📦 model/              # 5 Entities
│   │   │   │   ├── 📦 repository/         # 5 Repositories  
│   │   │   │   ├── 📦 service/            # 2 Services
│   │   │   │   ├── 📦 controller/         # 2 Controllers
│   │   │   │   ├── 📦 dto/                # 4 DTOs
│   │   │   │   ├── 📦 config/             # 2 Configs
│   │   │   │   └── 📄 NewsRoomApplication.java
│   │   │   └── resources/
│   │   │       └── 📄 application.properties
│   │   └── test/
│   ├── 📄 pom.xml                 # Maven config (Java 21)
│   ├── 📄 Dockerfile              # Backend container
│   ├── 📄 README.md               # Backend docs
│   └── 📄 mvnw / mvnw.cmd         # Maven wrapper
│
├── 📁 frontend/                   # Next.js App (Port 3000)
│   ├── src/
│   │   ├── app/                   # Next.js App Router
│   │   │   ├── 📄 layout.tsx     # Root layout
│   │   │   ├── 📄 page.tsx       # Home page
│   │   │   ├── 📄 globals.css    # Global styles
│   │   │   ├── 📁 news/[slug]/   # News detail
│   │   │   └── 📁 category/[slug]/ # Category page
│   │   ├── components/            # React components
│   │   │   ├── 📄 Header.tsx
│   │   │   ├── 📄 Footer.tsx
│   │   │   ├── 📄 MainSlider.tsx
│   │   │   ├── 📄 FeaturedNews.tsx
│   │   │   ├── 📄 CategorySection.tsx
│   │   │   ├── 📄 LatestNews.tsx
│   │   │   └── 📄 Sidebar.tsx
│   │   ├── lib/
│   │   │   └── 📄 api.ts         # API client
│   │   └── types/
│   │       └── 📄 index.ts       # TypeScript types
│   ├── public/                    # Static files
│   ├── 📄 package.json           # npm config (Node 20.14.0)
│   ├── 📄 tsconfig.json          # TypeScript config
│   ├── 📄 next.config.js         # Next.js config
│   ├── 📄 tailwind.config.js     # Tailwind config
│   ├── 📄 postcss.config.js
│   ├── 📄 Dockerfile             # Frontend container
│   └── 📄 README.md              # Frontend docs
│
├── 📁 img/                        # Template assets (legacy)
│
├── 📄 docker-compose.yml          # Docker orchestration
├── 📄 .gitignore                  # Git ignore
│
├── 📄 README.md                   # 📚 Main documentation
├── 📄 QUICK_START.md              # 🚀 Quick start guide
├── 📄 UPDATE_SUMMARY.md           # ⚡ Update summary
├── 📄 VERSION_INFO.md             # 📌 Version details & install guide
├── 📄 CHANGELOG.md                # 📝 Change history
├── 📄 PROJECT_OVERVIEW.md         # 📊 Detailed overview
└── 📄 PROJECT_STRUCTURE.md        # 📂 This file
```

---

## 🎯 Backend Structure (Java 21 + Spring Boot)

### Models (5 Entities)
```
model/
├── News.java           # Tin tức chính
├── Category.java       # Danh mục
├── Tag.java           # Thẻ tag
├── User.java          # Người dùng
└── Comment.java       # Bình luận
```

**Relationships:**
- News ↔ Category (Many-to-One)
- News ↔ Tags (Many-to-Many)
- News ↔ User/Author (Many-to-One)
- Comment ↔ News (Many-to-One)
- Comment ↔ User (Many-to-One)

### Repositories (5)
```
repository/
├── NewsRepository.java
├── CategoryRepository.java
├── TagRepository.java
├── UserRepository.java
└── CommentRepository.java
```

**Features:**
- MongoDB query methods
- Custom queries với @Query
- Pagination support
- DBRef relationships

### Services (2)
```
service/
├── NewsService.java      # Business logic cho News
└── CategoryService.java  # Business logic cho Category
```

**Responsibilities:**
- CRUD operations
- Data validation
- Business rules
- DTO conversions
- Slug generation

### Controllers (2)
```
controller/
├── NewsController.java      # 9 endpoints
└── CategoryController.java  # 7 endpoints
```

**Total: 16+ API Endpoints**

### DTOs (4)
```
dto/
├── NewsDTO.java
├── CategoryDTO.java
├── AuthRequest.java
└── AuthResponse.java
```

### Configuration (2)
```
config/
├── SecurityConfig.java    # Spring Security + JWT
└── CorsConfig.java        # CORS settings
```

---

## 🎨 Frontend Structure (Node.js 20.14.0 + Next.js)

### Pages (3)
```
app/
├── page.tsx              # Home page
├── news/[slug]/
│   └── page.tsx         # News detail (dynamic)
└── category/[slug]/
    └── page.tsx         # Category page (dynamic)
```

**Routing:**
- `/` - Home
- `/news/{slug}` - News detail
- `/category/{slug}` - Category listing

### Components (7)
```
components/
├── Header.tsx           # Navigation, search, trending
├── Footer.tsx           # Links, social, categories
├── MainSlider.tsx       # Hero slider
├── FeaturedNews.tsx     # Featured news grid
├── CategorySection.tsx  # Category news section
├── LatestNews.tsx       # Latest news list
└── Sidebar.tsx          # Trending, social, newsletter
```

**Component Tree:**
```
Layout
├── Header
├── {page content}
│   ├── MainSlider
│   ├── FeaturedNews
│   ├── CategorySection (x4)
│   ├── LatestNews
│   └── Sidebar
└── Footer
```

### API Client
```
lib/
└── api.ts
    ├── newsApi
    │   ├── getAll()
    │   ├── getBySlug()
    │   ├── getByCategory()
    │   ├── getFeatured()
    │   └── getTrending()
    └── categoryApi
        ├── getAll()
        ├── getActive()
        └── getBySlug()
```

### TypeScript Types
```
types/
└── index.ts
    ├── News
    ├── Category
    ├── Tag
    └── PaginatedResponse<T>
```

---

## 📄 Documentation Files

### Main Documentation
```
📄 README.md              # Tổng quan, setup, features
📄 QUICK_START.md         # Hướng dẫn khởi động nhanh 5 phút
📄 PROJECT_OVERVIEW.md    # Chi tiết kiến trúc, thống kê
📄 PROJECT_STRUCTURE.md   # File structure (file này)
```

### Version & Updates
```
📄 UPDATE_SUMMARY.md      # Tóm tắt cập nhật v1.1.0
📄 VERSION_INFO.md        # Hướng dẫn cài đặt Java 21 & Node 20.14.0
📄 CHANGELOG.md           # Lịch sử thay đổi chi tiết
```

### Module Documentation
```
📄 backend/README.md      # Backend API docs
📄 frontend/README.md     # Frontend component docs
```

---

## 🐳 Docker Structure

### Docker Compose
```yaml
docker-compose.yml
├── mongodb (service)     # Port 27017
├── backend (service)     # Port 8080
└── frontend (service)    # Port 3000
```

### Dockerfiles
```
backend/Dockerfile        # Multi-stage build
├── Stage 1: Build (Maven + JDK 21)
└── Stage 2: Runtime (JRE 21 Alpine)

frontend/Dockerfile       # Multi-stage build
├── Stage 1: Dependencies (Node 20.14.0)
├── Stage 2: Build
└── Stage 3: Runtime (Node 20.14.0 Alpine)
```

---

## 🔧 Configuration Files

### Backend
```
backend/
├── pom.xml                    # Maven dependencies
└── src/main/resources/
    └── application.properties # App configuration
```

**Key Configs:**
- Server port: 8080
- MongoDB URI
- JWT settings
- CORS origins
- File upload limits

### Frontend
```
frontend/
├── package.json           # npm dependencies + engines
├── tsconfig.json          # TypeScript config
├── next.config.js         # Next.js config
├── tailwind.config.js     # Tailwind CSS config
└── postcss.config.js      # PostCSS config
```

**Key Configs:**
- Node.js version: 20.14.0
- Image domains
- API URL env variable

---

## 📊 Statistics

### Backend
- **Java Files**: 20+
- **Lines of Code**: ~2,000+
- **API Endpoints**: 16+
- **Entities**: 5
- **Services**: 2
- **Controllers**: 2

### Frontend
- **TypeScript Files**: 15+
- **Lines of Code**: ~1,500+
- **Components**: 7
- **Pages**: 3
- **API Methods**: 8+

### Total Project
- **Total Files**: 35+
- **Total LOC**: ~3,500+
- **Documentation Files**: 9
- **Technologies**: 10+

---

## 🎯 Entry Points

### Development
```bash
# Backend Entry Point
backend/src/main/java/com/newsroom/NewsRoomApplication.java

# Frontend Entry Point
frontend/src/app/layout.tsx
frontend/src/app/page.tsx
```

### Production
```bash
# Backend JAR
backend/target/newsroom-backend-1.0.0.jar

# Frontend Build
frontend/.next/
```

### Docker
```bash
docker-compose.yml  # Orchestration entry point
```

---

## 📁 Key Directories

| Directory | Purpose | Files |
|-----------|---------|-------|
| `backend/src/main/java/com/newsroom/` | Java source code | 20+ |
| `frontend/src/app/` | Next.js pages | 5+ |
| `frontend/src/components/` | React components | 7 |
| `frontend/src/lib/` | Utilities | 1 |
| `frontend/src/types/` | TypeScript types | 1 |
| `(root)/` | Documentation | 9 |

---

## 🗺️ Navigation Map

```
Bắt đầu ở đây
    ↓
📄 README.md ────────┐
    ↓                │
Muốn khởi động nhanh?│  Muốn biết chi tiết?
    ↓                ↓
📄 QUICK_START.md   📄 PROJECT_OVERVIEW.md
    │                │
    │                ↓
    │           📄 PROJECT_STRUCTURE.md (bạn đang ở đây)
    │
    ↓
Cài đặt version?
    ↓
📄 VERSION_INFO.md
    │
    ├─→ Backend details? → 📄 backend/README.md
    └─→ Frontend details? → 📄 frontend/README.md
```

---

## 🔍 Quick Find

**Tìm API endpoints?** → `backend/README.md`  
**Tìm React components?** → `frontend/README.md`  
**Setup environment?** → `VERSION_INFO.md`  
**Quick start?** → `QUICK_START.md`  
**Recent updates?** → `UPDATE_SUMMARY.md`  
**Change history?** → `CHANGELOG.md`  

---

**Last Updated**: October 2025  
**Version**: 1.1.0

