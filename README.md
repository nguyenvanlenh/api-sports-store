
<h2 align="center"> <strong>SPORTER STORE WEB APPLICATION (Spring/ReactJS)</strong> </h2>

**Website:** [sporterstore.netlify.app](https://sporterstore.netlify.app)
<br/>
**API Documents:** [sporter.id.vn/swagger-ui](https://sporter.id.vn/swagger-ui/index.html)
<br/>
**Design Database** [sporter database](https://drive.google.com/file/d/11oIXctvnPg8BxmAlNT4CsUMCygEJFC0q/view?usp=sharing)

---

## 🚀 Hướng dẫn chạy Project

### Yêu cầu hệ thống (Prerequisites)

- **Java:** JDK 17 trở lên
- **Maven:** 3.6+ (hoặc dùng Maven Wrapper có sẵn trong project)
- **MySQL:** 5.7+ hoặc 8.0+
- **Docker & Docker Compose** (tùy chọn - để chạy qua container)
- **Git**

---

### 1️⃣ Cách 1: Chạy cục bộ với Maven

#### Bước 1: Clone project
```bash
git clone https://github.com/nguyenvanlenh/api-sports-store.git
cd api-sports-store
```

#### Bước 2: Cấu hình MySQL
Tạo database:
```sql
CREATE DATABASE freetime;
```

#### Bước 3: Cấu hình file `application.yaml`
Mở file `src/main/resources/application.yaml` và chỉnh sửa các thông số MySQL:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/freetime?createDatabaseIfNotExist=true
    username: root
    password: your_mysql_password
```

#### Bước 4: Chạy project
```bash
# Sử dụng Maven Wrapper (Windows)
mvnw.cmd spring-boot:run

# Hoặc trên Linux/Mac
./mvnw spring-boot:run

# Hoặc với Maven (nếu đã cài toàn cục)
mvn spring-boot:run
```

Ứng dụng sẽ khởi động tại: **http://localhost:8080**

---

### 2️⃣ Cách 2: Build và chạy với Docker

#### Bước 1: Build Docker image
```bash
docker build -t sporter-api:latest .
```

#### Bước 2: Chạy container
```bash
docker run -d \
  --name sporter-api \
  -p 8080:8080 \
  -e MYSQL_HOST=host.docker.internal \
  -e MYSQL_PORT=3306 \
  -e MYSQL_USERNAME=root \
  -e MYSQL_PASSWORD=your_password \
  -e DATABASE_NAME=freetime \
  sporter-api:latest
```

---

### 3️⃣ Cách 3: Sử dụng Docker Compose (Recommended)

#### Bước 1: Tạo file `docker-compose.yml`
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: sporter-mysql
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: freetime
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - sporter-network

  api:
    build: .
    container_name: sporter-api
    ports:
      - "8080:8080"
    environment:
      MYSQL_HOST: mysql
      MYSQL_PORT: 3306
      MYSQL_USERNAME: root
      MYSQL_PASSWORD: your_password
      DATABASE_NAME: freetime
    depends_on:
      - mysql
    networks:
      - sporter-network

volumes:
  mysql-data:

networks:
  sporter-network:
    driver: bridge
```

#### Bước 2: Chạy
```bash
docker-compose up -d
```

---

### 📋 Kiểm tra tình trạng ứng dụng

Sau khi khởi động, truy cập các endpoint sau:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Health Check:** http://localhost:8080/actuator/health
- **H2 Console (nếu dùng H2):** http://localhost:8080/h2-console

---

### 🔍 Tìm và sửa lỗi

#### Lỗi kết nối Database
```
ERROR 28000 (HY000): Access denied for user 'root'@'localhost'
```
**Giải pháp:** Kiểm tra lại username, password, host, port trong `application.yaml`

#### Lỗi port đã được sử dụng
```
Port 8080 already in use
```
**Giải pháp:** Dùng port khác hoặc kill process trên port 8080
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

#### Lỗi Build Maven
```bash
mvn clean package
```

---

### 📁 Cấu trúc Project

```
api-sports-store/
├── src/
│   ├── main/
│   │   ├── java/com/watermelon/
│   │   │   ├── config/          (Cấu hình)
│   │   │   ├── controller/      (REST Controllers)
│   │   │   ├── dto/             (Data Transfer Objects)
│   │   │   ├── service/         (Business Logic)
│   │   │   ├── model/           (Entity/Models)
│   │   │   ├── repository/      (Database Access)
│   │   │   └── utils/           (Utilities)
│   │   └── resources/
│   │       ├── application.yaml (Cấu hình chính)
│   │       └── templates/       (Email templates)
│   └── test/
├── pom.xml                      (Maven Dependencies)
├── Dockerfile                   (Docker Configuration)
└── README.md                    (File này)
```

---

### Source Code

- **API Source Code:** [nguyenvanlenh/api-sports-store](https://github.com/nguyenvanlenh/api-sports-store)
- **Web Client Source Code:** [nguyenvanlenh/frontend-sports-store](https://github.com/nguyenvanlenh/frontend-sports-store)

---

### Account Login

| Role       | Username     | Password  |
|------------|--------------|-----------|
| **Admin**  | nguyenlenh   | 12345678  |
| **User**   | lenhnguyen   | 12345678  |

### Account Paypal

| Email                        | Password  |
|------------------------------|-----------|
| nguyenlenh@paypal.gmail.com | 12345678  |

### Account VNPay

| Bank  | Card Number         | Cardholder Name | Issue Date | OTP Password |
|-------|---------------------|------------------|------------|--------------|
| NCB   | 9704198526191432198 | NGUYEN VAN A   | 07/15      | 123456       |
