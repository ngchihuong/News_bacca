# 🔐 Admin Panel Documentation

## 📋 Tổng quan

Admin Panel cho phép quản trị viên và tác giả quản lý nội dung của NewsRoom, bao gồm:
- ✅ Quản lý tin tức (CRUD)
- ✅ Quản lý danh mục
- ✅ Upload hình ảnh
- ✅ Dashboard với thống kê
- ✅ Authentication với JWT

---

## 🚀 Truy cập Admin Panel

### URL
```
http://localhost:3000/admin/login
```

### Demo Credentials (Cần tạo user trước)

**Bước 1: Tạo Admin User**

Sử dụng MongoDB hoặc API để tạo user admin:

```javascript
// MongoDB Shell hoặc Compass
use newsroom

db.users.insertOne({
  username: "admin",
  email: "admin@newsroom.com",
  password: "$2a$10$rqWVHmXEJvFgZ.OQzxXH5.HYKGj4rLmVYLj3fKGQ8RJvFjFKVzNm2", // admin123
  fullName: "Admin User",
  role: "ADMIN",
  active: true,
  createdAt: new Date(),
  updatedAt: new Date()
})
```

**Password Hash Guide:**
- Password: `admin123`
- BCrypt Hash: `$2a$10$rqWVHmXEJvFgZ.OQzxXH5.HYKGj4rLmVYLj3fKGQ8RJvFjFKVzNm2`

**Bước 2: Login**
```
Username: admin
Password: admin123
```

---

## 🏗️ Kiến trúc Admin Panel

### Backend Components

#### 1. Authentication & Security
```
backend/src/main/java/com/newsroom/
├── security/
│   ├── JwtTokenProvider.java        # JWT token generation & validation
│   ├── JwtAuthenticationFilter.java # JWT filter for requests
│   └── CustomUserDetailsService.java # User authentication service
├── service/
│   └── AuthService.java             # Login, register, current user
└── controller/
    └── AuthController.java          # /auth/login, /auth/register
```

#### 2. Admin Controllers
```
backend/src/main/java/com/newsroom/controller/admin/
├── AdminNewsController.java      # News management (CRUD + publish)
├── AdminCategoryController.java  # Category management (CRUD)
└── AdminFileController.java      # File upload & management
```

#### 3. File Upload Service
```
backend/src/main/java/com/newsroom/service/
└── FileStorageService.java       # Upload, delete, retrieve files
```

### Frontend Components

#### 1. Auth Context
```
frontend/src/context/
└── AuthContext.tsx               # Global auth state management
```

#### 2. Admin API Client
```
frontend/src/lib/
└── adminApi.ts                   # Admin API calls with JWT
```

#### 3. Admin Pages
```
frontend/src/app/admin/
├── login/page.tsx                # Login page
├── dashboard/page.tsx            # Dashboard with stats
├── news/page.tsx                 # News list & management
└── layout.tsx                    # Admin layout wrapper
```

#### 4. Admin Components
```
frontend/src/components/admin/
├── AdminNav.tsx                  # Sidebar navigation
└── ProtectedRoute.tsx            # Route protection wrapper
```

---

## 🔑 API Endpoints

### Authentication
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin",
  "email": "admin@newsroom.com",
  "role": "ADMIN"
}
```

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "fullName": "New User"
}
```

```http
GET /api/auth/me
Authorization: Bearer {token}
```

### Admin News Management

```http
GET /api/admin/news?page=0&size=20&status=PUBLISHED
Authorization: Bearer {token}
```

```http
POST /api/admin/news
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Breaking News",
  "excerpt": "Short description",
  "content": "Full content here...",
  "imageUrl": "https://example.com/image.jpg",
  "categoryId": "category-id",
  "tagIds": ["tag-id-1", "tag-id-2"],
  "status": "DRAFT",
  "featured": false,
  "trending": false
}
```

```http
PUT /api/admin/news/{id}
Authorization: Bearer {token}
```

```http
PUT /api/admin/news/{id}/publish
Authorization: Bearer {token}
```

```http
PUT /api/admin/news/{id}/unpublish
Authorization: Bearer {token}
```

```http
DELETE /api/admin/news/{id}
Authorization: Bearer {token}
```

```http
GET /api/admin/news/stats
Authorization: Bearer {token}

Response:
{
  "totalNews": 100,
  "publishedNews": 75,
  "draftNews": 20,
  "featuredNews": 10
}
```

### Admin File Upload

```http
POST /api/admin/files/upload
Authorization: Bearer {token}
Content-Type: multipart/form-data

file: [binary]

Response:
{
  "fileName": "abc-123-def.jpg",
  "fileUrl": "http://localhost:8080/api/files/abc-123-def.jpg",
  "fileType": "image/jpeg",
  "size": "524288"
}
```

```http
DELETE /api/admin/files/{fileName}
Authorization: Bearer {token}
```

### Public File Access

```http
GET /api/files/{fileName}
```

---

## 👤 User Roles

### ADMIN
- ✅ Full access to all features
- ✅ Create, edit, delete news
- ✅ Manage categories
- ✅ Upload files
- ✅ Manage users (future)
- ✅ Delete any content

### AUTHOR
- ✅ Create and edit news
- ✅ Upload files
- ✅ View categories
- ❌ Cannot delete news (only ADMIN)
- ❌ Cannot manage categories
- ❌ Cannot manage users

### USER
- ❌ No admin access
- ✅ Can register and login
- ✅ Can view public content

---

## 🎨 Admin Pages Overview

### 1. Login Page (`/admin/login`)
- Clean, modern login form
- Username/password authentication
- Error handling
- Auto-redirect after login

### 2. Dashboard (`/admin/dashboard`)
- Statistics cards:
  - Total News
  - Published News
  - Draft News
  - Featured News
- Quick action buttons
- Responsive layout

### 3. News Management (`/admin/news`)
- Table view of all news
- Filter by status (All, Published, Draft, Archived)
- Pagination
- Actions:
  - Edit news
  - Publish/Unpublish
  - Delete
- Create new news button

### 4. News Editor (`/admin/news/create` & `/admin/news/{id}/edit`)
**Note**: Cần implement Rich Text Editor

Suggested fields:
- Title (required)
- Excerpt (required)
- Content (Rich Text Editor)
- Featured Image (File upload)
- Category (Dropdown)
- Tags (Multi-select)
- Status (Draft/Published)
- Featured checkbox
- Trending checkbox

---

## 🛠️ Setup Instructions

### 1. Create Admin User

**Option A: Using MongoDB Shell**
```javascript
use newsroom

db.users.insertOne({
  username: "admin",
  email: "admin@newsroom.com",
  password: "$2a$10$rqWVHmXEJvFgZ.OQzxXH5.HYKGj4rLmVYLj3fKGQ8RJvFjFKVzNm2",
  fullName: "Admin User",
  role: "ADMIN",
  active: true,
  createdAt: new Date(),
  updatedAt: new Date()
})
```

**Option B: Using POST API**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@newsroom.com",
    "password": "admin123",
    "fullName": "Admin User"
  }'

# Then update role to ADMIN in MongoDB
db.users.updateOne(
  {username: "admin"},
  {$set: {role: "ADMIN"}}
)
```

### 2. Login to Admin Panel

1. Open browser: `http://localhost:3000/admin/login`
2. Enter credentials:
   - Username: `admin`
   - Password: `admin123`
3. Click "Login"
4. Redirected to Dashboard

---

## 🔒 Security Features

### JWT Authentication
- Token-based authentication
- Secure password hashing (BCrypt)
- Token expiration (24 hours default)
- Auto logout on token expiry

### Route Protection
- `/admin/*` routes require authentication
- Role-based access control
- Protected API endpoints
- CORS configuration

### Password Security
- BCrypt hashing
- Minimum 6 characters
- Stored securely in database

---

## 📂 File Upload

### Supported Features
- Image upload
- File size limit: 10MB
- Unique filename generation (UUID)
- File storage in `uploads/` directory
- Public file access via `/api/files/{fileName}`

### Usage Example

```typescript
// Upload file
const formData = new FormData();
formData.append('file', fileInput.files[0]);

const response = await adminFileApi.upload(file);
// Response: { fileName, fileUrl, fileType, size }

// Use fileUrl in news imageUrl field
```

---

## 🎯 Features Status

### ✅ Implemented
- JWT Authentication
- Login page
- Protected routes
- Dashboard with stats
- News list management
- Admin navigation
- File upload API
- Publish/Unpublish news
- Delete news
- Category management API

### 🚧 To Implement
- [ ] News Create/Edit form with Rich Text Editor
- [ ] Category management UI
- [ ] Tag management UI
- [ ] User management UI
- [ ] Image upload component
- [ ] Preview news feature
- [ ] Bulk actions
- [ ] Search & filters

---

## 🔧 Customization

### Change JWT Secret
Edit `backend/src/main/resources/application.properties`:
```properties
app.jwt.secret=YourNewVeryLongSecretKeyHere
app.jwt.expiration=86400000  # 24 hours in milliseconds
```

### Change Upload Directory
```properties
file.upload-dir=uploads
```

### Change Base URL
```properties
app.base-url=http://localhost:8080
```

---

## 📝 Next Steps

### Recommended Implementations

1. **Rich Text Editor**
   - Integrate TinyMCE or Quill
   - Add to News Create/Edit form
   - Image upload within editor

2. **Image Upload Component**
   - Drag & drop interface
   - Image preview
   - Crop functionality

3. **Category Management UI**
   - CRUD interface
   - Image upload for categories

4. **User Management**
   - List users
   - Edit roles
   - Activate/deactivate users

5. **Analytics Dashboard**
   - View statistics
   - Popular articles
   - Traffic data

---

## 🐛 Troubleshooting

### Cannot Login
1. Check if user exists in database
2. Verify password hash is correct
3. Check backend logs for errors
4. Verify JWT secret is configured

### 401 Unauthorized
1. Token might be expired (24h default)
2. Re-login to get new token
3. Check token is sent in Authorization header

### File Upload Fails
1. Check `uploads/` directory exists
2. Verify file size < 10MB
3. Check permissions on uploads directory

### Dashboard Not Loading
1. Check backend is running
2. Verify `/admin/news/stats` endpoint works
3. Check browser console for errors

---

## 📞 Support

For issues or questions:
1. Check backend logs
2. Check browser console
3. Verify API endpoints in Postman
4. Review this documentation

---

**Admin Panel Version**: 1.0.0  
**Last Updated**: October 2025  
**Status**: ✅ Basic Features Complete

