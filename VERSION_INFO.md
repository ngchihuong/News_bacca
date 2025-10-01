# 📌 Thông tin Phiên bản

## ✅ Yêu cầu Hệ thống

### Backend
- **Java**: 21
- **Maven**: 3.6+
- **Spring Boot**: 3.2.0

### Frontend
- **Node.js**: 20.14.0
- **npm**: 10.0.0+
- **Next.js**: 14.0.4

### Database
- **MongoDB**: 5.0+

---

## 🔍 Kiểm tra Phiên bản Hiện tại

### Kiểm tra Java
```bash
java -version
# Nên thấy: openjdk version "21" hoặc java version "21"
```

### Kiểm tra Node.js
```bash
node -v
# Nên thấy: v20.14.0 hoặc cao hơn
```

### Kiểm tra npm
```bash
npm -v
# Nên thấy: 10.x.x hoặc cao hơn
```

### Kiểm tra Maven
```bash
mvn -v
# Nên thấy: Apache Maven 3.6.x hoặc cao hơn
```

### Kiểm tra MongoDB
```bash
mongod --version
# Nên thấy: db version v5.0.x hoặc cao hơn
```

---

## 📥 Hướng dẫn Cài đặt

### Cài đặt Java 21

#### Windows
1. Download từ [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) hoặc [OpenJDK 21](https://adoptium.net/)
2. Chạy installer
3. Thiết lập JAVA_HOME:
   ```powershell
   # Mở System Properties > Environment Variables
   # Thêm JAVA_HOME = C:\Program Files\Java\jdk-21
   # Thêm %JAVA_HOME%\bin vào PATH
   ```

#### macOS
```bash
# Sử dụng Homebrew
brew install openjdk@21

# Link OpenJDK
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# Thêm vào ~/.zshrc hoặc ~/.bash_profile
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```

#### Linux (Ubuntu/Debian)
```bash
# OpenJDK 21
sudo apt update
sudo apt install openjdk-21-jdk

# Kiểm tra
java -version
```

### Cài đặt Node.js 20.14.0

#### Sử dụng NVM (Khuyến nghị - tất cả OS)

**Windows:**
1. Download [nvm-windows](https://github.com/coreybutler/nvm-windows/releases)
2. Cài đặt installer

**macOS/Linux:**
```bash
# Cài đặt nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Hoặc với wget
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Reload shell
source ~/.bashrc  # hoặc ~/.zshrc
```

**Sau khi cài nvm (tất cả OS):**
```bash
# Cài đặt Node.js 20.14.0
nvm install 20.14.0

# Sử dụng version này
nvm use 20.14.0

# Đặt làm default
nvm alias default 20.14.0

# Kiểm tra
node -v
npm -v
```

#### Cài đặt trực tiếp

**Windows:**
1. Download từ [Node.js official website](https://nodejs.org/)
2. Chọn version 20.14.0 LTS
3. Chạy installer

**macOS:**
```bash
# Sử dụng Homebrew
brew install node@20

# Link version
brew link node@20
```

**Linux (Ubuntu/Debian):**
```bash
# Sử dụng NodeSource
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs

# Kiểm tra version
node -v
```

### Cài đặt MongoDB

#### Windows
1. Download [MongoDB Community Server](https://www.mongodb.com/try/download/community)
2. Chạy installer
3. Chọn "Complete" installation
4. Install as Windows Service

#### macOS
```bash
# Sử dụng Homebrew
brew tap mongodb/brew
brew install mongodb-community@7.0

# Khởi động MongoDB
brew services start mongodb-community@7.0
```

#### Linux (Ubuntu/Debian)
```bash
# Import MongoDB GPG key
curl -fsSL https://pgp.mongodb.com/server-7.0.asc | \
   sudo gpg -o /usr/share/keyrings/mongodb-server-7.0.gpg \
   --dearmor

# Add MongoDB repository
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | \
   sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

# Install MongoDB
sudo apt-get update
sudo apt-get install -y mongodb-org

# Khởi động MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod
```

---

## 🔧 Troubleshooting

### Java Version Conflicts

Nếu có nhiều Java version:

**Windows:**
```powershell
# Kiểm tra tất cả Java versions
where java

# Thay đổi JAVA_HOME trong System Properties
```

**macOS:**
```bash
# List all Java versions
/usr/libexec/java_home -V

# Switch to Java 21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

**Linux:**
```bash
# List alternatives
sudo update-alternatives --config java

# Select Java 21
```

### Node.js Version Conflicts

```bash
# Sử dụng nvm để chuyển đổi
nvm list
nvm use 20.14.0

# Hoặc set default
nvm alias default 20.14.0
```

### Maven Not Found

```bash
# Download Maven từ https://maven.apache.org/download.cgi
# Hoặc sử dụng wrapper đã có trong project
./mvnw --version  # Unix/Mac
mvnw.cmd --version  # Windows
```

---

## 🐳 Docker Versions

Nếu sử dụng Docker, versions được định nghĩa trong Dockerfiles:

### Backend Dockerfile
```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
FROM eclipse-temurin:21-jre-alpine
```

### Frontend Dockerfile
```dockerfile
FROM node:20.14.0-alpine
```

### Docker Compose
Xem `docker-compose.yml` để biết cấu hình đầy đủ.

---

## ✅ Checklist Cài đặt

- [ ] Java 21 đã cài đặt (`java -version`)
- [ ] Node.js 20.14.0 đã cài đặt (`node -v`)
- [ ] npm 10+ đã cài đặt (`npm -v`)
- [ ] Maven 3.6+ đã cài đặt (`mvn -v`) hoặc dùng wrapper
- [ ] MongoDB 5.0+ đã cài đặt và chạy
- [ ] Có thể kết nối MongoDB (`mongosh`)
- [ ] JAVA_HOME được set đúng
- [ ] PATH được cấu hình đúng

---

## 📝 Notes

### Tại sao Java 21?
- LTS (Long Term Support) release
- Virtual Threads (Project Loom)
- Pattern Matching improvements
- Record Patterns
- Better performance
- Security updates

### Tại sao Node.js 20.14.0?
- LTS version
- Stable và production-ready
- Permission Model
- Test Runner improvements
- Performance enhancements
- Long-term security updates

### Compatibility
- Spring Boot 3.2.0 hoàn toàn tương thích với Java 21
- Next.js 14 hoàn toàn tương thích với Node.js 20
- Tất cả dependencies đã được test với các versions này

---

## 🆘 Cần Trợ giúp?

Nếu gặp vấn đề với việc cài đặt hoặc cấu hình versions:

1. Kiểm tra lại hướng dẫn trên
2. Xem [QUICK_START.md](QUICK_START.md) để troubleshooting
3. Tạo Issue trên GitHub repository

---

**Last Updated**: October 2025

