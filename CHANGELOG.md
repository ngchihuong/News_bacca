# 📝 Changelog

## [1.1.0] - 2025-10-01

### ⬆️ Upgraded Versions

#### Backend
- **Java**: 17 → **21**
  - Updated `pom.xml` 
  - Updated `backend/Dockerfile`
  - Reason: LTS release với Virtual Threads, better performance

#### Frontend
- **Node.js**: 18 → **20.14.0**
  - Updated `frontend/Dockerfile`
  - Updated `frontend/package.json` (added engines specification)
  - Reason: LTS release, stable, production-ready

### 📝 Updated Documentation

- ✅ `README.md` - Cập nhật tech stack và requirements
- ✅ `backend/README.md` - Cập nhật Java version requirements
- ✅ `frontend/README.md` - Cập nhật Node.js version requirements
- ✅ `QUICK_START.md` - Thêm phần kiểm tra version
- ✅ `PROJECT_OVERVIEW.md` - Cập nhật tech stack table
- ✅ **NEW**: `VERSION_INFO.md` - Hướng dẫn cài đặt chi tiết các versions
- ✅ **NEW**: `CHANGELOG.md` - File này

### 🔧 Technical Changes

#### Backend (Java 21)
```xml
<!-- pom.xml -->
<properties>
    <java.version>21</java.version>
</properties>
```

```dockerfile
# Dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
FROM eclipse-temurin:21-jre-alpine
```

#### Frontend (Node.js 20.14.0)
```json
// package.json
"engines": {
  "node": ">=20.14.0",
  "npm": ">=10.0.0"
}
```

```dockerfile
# Dockerfile
FROM node:20.14.0-alpine
```

### 🎯 Benefits

#### Java 21
- ✨ Virtual Threads (Project Loom) - Better concurrency
- ✨ Pattern Matching improvements
- ✨ Record Patterns
- ✨ Sequenced Collections
- ✨ Better performance and GC
- 🔒 Security updates
- 📅 LTS support until 2028

#### Node.js 20.14.0
- ✨ Permission Model for better security
- ✨ Test Runner improvements
- ✨ V8 JavaScript engine 11.3
- ✨ Better performance
- 🔒 Security updates
- 📅 LTS support until 2026

### ⚠️ Breaking Changes
Không có breaking changes về code. Chỉ cần cập nhật:
- Java SDK từ 17 → 21
- Node.js từ 18 → 20.14.0

### 🔄 Migration Guide

#### Nếu đang dùng versions cũ:

1. **Cập nhật Java:**
   ```bash
   # Xem VERSION_INFO.md để hướng dẫn cài đặt Java 21
   java -version  # Kiểm tra version mới
   ```

2. **Cập nhật Node.js:**
   ```bash
   # Khuyến nghị dùng nvm
   nvm install 20.14.0
   nvm use 20.14.0
   nvm alias default 20.14.0
   ```

3. **Pull code mới và rebuild:**
   ```bash
   # Backend
   cd backend
   ./mvnw clean install
   ./mvnw spring-boot:run
   
   # Frontend
   cd frontend
   rm -rf node_modules package-lock.json
   npm install
   npm run dev
   ```

### 📊 Compatibility

| Component | Old Version | New Version | Status |
|-----------|------------|-------------|--------|
| Java | 17 | 21 | ✅ Fully Compatible |
| Node.js | 18 | 20.14.0 | ✅ Fully Compatible |
| Spring Boot | 3.2.0 | 3.2.0 | ✅ No Change |
| Next.js | 14.0.4 | 14.0.4 | ✅ No Change |
| MongoDB | 5.0+ | 5.0+ | ✅ No Change |

### 🧪 Testing

All functionality tested with new versions:
- ✅ Backend API endpoints working
- ✅ Frontend rendering correctly
- ✅ Database connection stable
- ✅ Docker builds successful
- ✅ Production builds working

### 📦 Files Modified

#### Configuration Files (8)
- `backend/pom.xml`
- `backend/Dockerfile`
- `frontend/package.json`
- `frontend/Dockerfile`
- `README.md`
- `backend/README.md`
- `frontend/README.md`
- `QUICK_START.md`
- `PROJECT_OVERVIEW.md`

#### New Files (2)
- `VERSION_INFO.md`
- `CHANGELOG.md`

---

## [1.0.0] - 2025-10-01

### 🎉 Initial Release

- ✅ Complete Backend with Java Spring Boot 17
- ✅ Complete Frontend with Next.js 14
- ✅ MongoDB integration
- ✅ Full documentation
- ✅ Docker support
- ✅ 16+ API endpoints
- ✅ 7 React components
- ✅ 3 pages (Home, News Detail, Category)

---

## 📌 Version Support

| Version | Release Date | Support Until | Status |
|---------|--------------|---------------|--------|
| 1.1.0 | 2025-10-01 | Active | ✅ Current |
| 1.0.0 | 2025-10-01 | 2025-12-31 | 🔄 Maintenance |

---

## 🔮 Planned Updates

### v1.2.0 (Future)
- [ ] JWT Authentication implementation
- [ ] Comment system activation
- [ ] File upload functionality
- [ ] Admin dashboard
- [ ] Search functionality

### v1.3.0 (Future)
- [ ] Real-time notifications
- [ ] Newsletter system
- [ ] User profiles
- [ ] Social sharing
- [ ] Analytics integration

---

**For detailed installation instructions, see [VERSION_INFO.md](VERSION_INFO.md)**

