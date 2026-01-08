# Shop Online - Backend API

Hệ thống quản lý cửa hàng online được xây dựng bằng Spring Boot, cung cấp các API để quản lý sản phẩm, danh mục, tồn kho và upload hình ảnh.

## 📋 Mục lục

- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt và chạy dự án](#cài-đặt-và-chạy-dự-án)
- [Cấu hình](#cấu-hình)
- [API Endpoints](#api-endpoints)
- [Cấu trúc Database](#cấu-trúc-database)
- [Tính năng chính](#tính-năng-chính)

## 🛠️ Công nghệ sử dụng

- **Java 17**: Ngôn ngữ lập trình
- **Spring Boot 4.0.0**: Framework chính
- **Spring Data JPA**: ORM và quản lý database
- **MySQL**: Hệ quản trị cơ sở dữ liệu
- **MapStruct 1.6.0**: Mapping giữa Entity và DTO
- **Lombok**: Giảm boilerplate code
- **OkHttp**: HTTP client cho Supabase Storage
- **Supabase Storage**: Lưu trữ hình ảnh

## 📁 Cấu trúc dự án

```
src/main/java/com/example/shop/
├── controller/          # REST Controllers
│   ├── BaseController.java
│   ├── CategoryController.java
│   ├── InventoryController.java
│   ├── ProductController.java
│   └── UploadController.java
├── dto/                 # Data Transfer Objects
│   ├── CategoryRequest.java
│   ├── InventoryRequest.java
│   └── ProductRequest.java
├── entity/              # JPA Entities
│   ├── Category.java
│   ├── Product.java
│   ├── ProductVariant.java
│   ├── ProductSize.java
│   ├── ProductColor.java
│   ├── ProductImage.java
│   ├── Inventory.java
│   └── ...
├── repository/          # JPA Repositories
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   ├── ProductVariantRepository.java
│   └── ...
├── service/             # Business Logic
│   ├── CategoryService.java
│   ├── ProductService.java
│   ├── ProductVariantService.java
│   ├── InventoryService.java
│   └── ProductImageService.java
├── mapper/              # MapStruct Mappers
│   ├── BaseMapper.java
│   ├── CategoryMapper.java
│   ├── ProductMapper.java
│   └── InventoryMapper.java
├── response/            # Response DTOs
│   ├── BaseResponse.java
│   ├── CategoryResponse.java
│   ├── ProductResponse.java
│   └── InventoryResponse.java
├── model/               # Enums
│   ├── ProductStatus.java
│   ├── OrderStatus.java
│   ├── PaymentStatus.java
│   └── ...
└── ShopApplication.java # Main class
```

## 💻 Yêu cầu hệ thống

- **JDK 17** hoặc cao hơn
- **MySQL 8.0** hoặc cao hơn
- **Gradle 7.x** hoặc cao hơn
- **Supabase Account** (để lưu trữ hình ảnh)

## 🚀 Cài đặt và chạy dự án

### 1. Clone repository

```bash
git clone <repository-url>
cd shop
```

### 2. Cấu hình database

Tạo database MySQL:

```sql
CREATE DATABASE shop_online;
```

### 3. Cấu hình application.properties

Cập nhật thông tin kết nối database và Supabase trong `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shop_online
spring.datasource.username=your_username
spring.datasource.password=your_password

# Supabase Configuration
supabase.url=your_supabase_url
supabase.service-key=your_supabase_service_key
supabase.bucket=your_bucket_name
```

### 4. Chạy dự án

**Sử dụng Gradle:**

```bash
./gradlew bootRun
```

**Hoặc build và chạy JAR:**

```bash
./gradlew build
java -jar build/libs/shop-0.0.1-SNAPSHOT.jar
```

### 5. Kiểm tra

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## ⚙️ Cấu hình

### Database Configuration

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Supabase Storage

Dự án sử dụng Supabase Storage để lưu trữ hình ảnh. Cần cấu hình:
- URL Supabase
- Service Key
- Bucket name

## 📡 API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Response Format

Tất cả API đều trả về format chuẩn:

```json
{
  "data": {...},
  "message": "Thông báo",
  "status": 200
}
```

### 1. Category APIs

#### GET `/api/category`
Lấy danh sách tất cả danh mục

**Response:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "Áo thun",
      "parentId": null,
      "createdAt": "2024-01-15 10:30:00"
    }
  ],
  "message": null,
  "status": 200
}
```

#### POST `/api/category`
Tạo danh mục mới

**Request Body:**
```json
{
  "name": "Áo thun",
  "parentId": null
}
```

#### PUT `/api/category`
Cập nhật danh mục

**Request Body:**
```json
{
  "id": 1,
  "name": "Áo thun nam",
  "parentId": null
}
```

#### DELETE `/api/category/{id}`
Xóa danh mục

---

### 2. Product APIs

#### GET `/api/product`
Lấy danh sách tất cả sản phẩm

**Response:**
```json
{
  "data": [
    {
      "id": 1,
      "category": {
        "id": 1,
        "name": "Áo thun",
        "parentId": null,
        "createdAt": "2024-01-15 10:30:00"
      },
      "name": "Áo thun nam cao cấp",
      "description": "Mô tả sản phẩm",
      "thumbnail": "https://example.com/thumbnail.jpg",
      "price": 299000.0,
      "discount": 10.0,
      "status": "ACTIVE",
      "createdAt": "2024-01-10 08:00:00",
      "images": [
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg"
      ],
      "variant": {
        "id": 1,
        "origin": "Việt Nam",
        "size": ["S", "M", "L", "XL"],
        "color": [
          {
            "name": "Đỏ",
            "hexCode": "#FF0000"
          },
          {
            "name": "Xanh",
            "hexCode": "#0000FF"
          }
        ]
      }
    }
  ],
  "message": null,
  "status": 200
}
```

#### POST `/api/product`
Tạo sản phẩm mới

**Request Body:**
```json
{
  "categoryId": 1,
  "name": "Áo thun nam cao cấp",
  "description": "Mô tả sản phẩm",
  "thumbnail": "https://example.com/thumbnail.jpg",
  "price": 299000.0,
  "discount": 10.0,
  "status": "ACTIVE",
  "images": [
    "https://example.com/image1.jpg"
  ],
  "variant": {
    "origin": "Việt Nam",
    "size": ["S", "M", "L"],
    "color": [
      {
        "name": "Đỏ",
        "hexCode": "#FF0000"
      }
    ]
  }
}
```

#### PUT `/api/product`
Cập nhật sản phẩm

**Request Body:**
```json
{
  "id": 1,
  "categoryId": 1,
  "name": "Áo thun nam cao cấp (Updated)",
  "description": "Mô tả đã cập nhật",
  "thumbnail": "https://example.com/thumbnail.jpg",
  "price": 299000.0,
  "discount": 15.0,
  "status": "ACTIVE",
  "images": [
    "https://example.com/image1.jpg"
  ],
  "variant": {
    "id": 1,
    "origin": "Việt Nam",
    "size": ["S", "M", "L", "XL"],
    "color": [
      {
        "name": "Đỏ",
        "hexCode": "#FF0000"
      },
      {
        "name": "Xanh",
        "hexCode": "#0000FF"
      }
    ]
  }
}
```

**Lưu ý:** 
- Nếu `variant.id` có giá trị: sẽ xóa sizes/colors cũ và tạo mới
- Nếu `variant.id` là null: sẽ tạo variant mới

#### DELETE `/api/product/{id}`
Xóa sản phẩm (sẽ tự động xóa variant, sizes, colors liên quan)

---

### 3. Inventory APIs

#### GET `/api/inventory`
Lấy danh sách tồn kho

**Response:**
```json
{
  "data": [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "Áo thun nam cao cấp",
        ...
      },
      "quantity": 100,
      "updatedAt": "2024-01-15 10:30:00"
    }
  ],
  "message": "Lấy danh sách tồn kho thành công",
  "status": 200
}
```

#### PUT `/api/inventory`
Cập nhật số lượng tồn kho

**Request Body:**
```json
{
  "id": 1,
  "quantity": 150
}
```

---

### 4. Upload APIs

#### POST `/api/upload`
Upload hình ảnh lên Supabase Storage

**Request:**
- Method: `POST`
- Content-Type: `multipart/form-data`
- Body: `file` (file image)

**Response:**
```json
{
  "data": "https://supabase-url/storage/v1/object/public/bucket/filename.jpg",
  "message": "Upload thành công",
  "status": 200
}
```

#### DELETE `/api/upload?fileName=filename.jpg`
Xóa hình ảnh từ Supabase Storage

**Response:**
```json
{
  "data": null,
  "message": "Xóa thành công",
  "status": 200
}
```

---

## 🗄️ Cấu trúc Database

### Các bảng chính:

1. **category**: Danh mục sản phẩm
   - `id` (PK)
   - `name`
   - `parent_id`
   - `created_at`

2. **product**: Sản phẩm
   - `id` (PK)
   - `category_id` (FK)
   - `name`
   - `description`
   - `thumbnail`
   - `price`
   - `discount`
   - `status` (ACTIVE/INACTIVE)
   - `created_at`

3. **product_variant**: Biến thể sản phẩm
   - `id` (PK)
   - `product_id` (FK)
   - `origin`
   - `sizes` (chuỗi id: "1, 2, 5, 10")
   - `colors` (chuỗi id: "1, 2, 5, 10")

4. **product_size**: Kích thước sản phẩm
   - `id` (PK)
   - `id_product_variant` (FK)
   - `name`

5. **product_color**: Màu sắc sản phẩm
   - `id` (PK)
   - `id_product_variant` (FK)
   - `name`
   - `hex_code`

6. **product_image**: Hình ảnh sản phẩm
   - `id` (PK)
   - `product_id` (FK)
   - `url`

7. **inventory**: Tồn kho
   - `id` (PK)
   - `product_id` (FK)
   - `quantity`
   - `updated_at`

### Quan hệ:

- `Product` 1-1 `ProductVariant`
- `ProductVariant` 1-N `ProductSize`
- `ProductVariant` 1-N `ProductColor`
- `Product` 1-N `ProductImage`
- `Product` 1-1 `Inventory`
- `Category` N-1 `Product`

## ✨ Tính năng chính

### 1. Quản lý Danh mục (Category)
- CRUD đầy đủ cho danh mục
- Hỗ trợ danh mục con (parent-child relationship)
- Validation tên danh mục trùng lặp

### 2. Quản lý Sản phẩm (Product)
- CRUD đầy đủ cho sản phẩm
- Quản lý nhiều hình ảnh cho mỗi sản phẩm
- Hỗ trợ variant (biến thể) với sizes và colors
- Tự động tạo sizes/colors mới nếu chưa tồn tại
- Xóa cascade: khi xóa sản phẩm sẽ tự động xóa variant, sizes, colors liên quan

### 3. Quản lý Variant
- Mỗi sản phẩm có thể có một variant
- Variant chứa:
  - Origin (xuất xứ)
  - Danh sách sizes (S, M, L, XL...)
  - Danh sách colors (với name và hexCode)
- Khi update variant: xóa sizes/colors cũ và tạo mới

### 4. Quản lý Tồn kho (Inventory)
- Tự động tạo inventory khi tạo sản phẩm mới
- Cập nhật số lượng tồn kho
- Xem danh sách tồn kho kèm thông tin sản phẩm

### 5. Upload hình ảnh
- Upload hình ảnh lên Supabase Storage
- Xóa hình ảnh từ Supabase Storage
- Tự động generate tên file unique

### 6. BaseController
- Chuẩn hóa response format
- Các method helper cho success/error responses
- Hỗ trợ các HTTP status codes phổ biến

## 🔧 Công nghệ và Pattern

- **RESTful API**: Thiết kế API theo chuẩn REST
- **Repository Pattern**: Sử dụng Spring Data JPA Repository
- **Service Layer**: Tách biệt business logic
- **DTO Pattern**: Sử dụng DTO cho request/response
- **Mapper Pattern**: Sử dụng MapStruct để map Entity ↔ DTO
- **Base Controller**: Tái sử dụng code xử lý response

## 📝 Lưu ý

1. **Database**: Hibernate sẽ tự động tạo/cập nhật schema khi `ddl-auto=update`
2. **Supabase**: Cần cấu hình đúng URL, service key và bucket name
3. **Product Variant**: 
   - Khi tạo sản phẩm mới với variant, sizes và colors sẽ được tạo mới
   - Khi update variant có id, sizes và colors cũ sẽ bị xóa và tạo mới
   - Khi xóa sản phẩm, variant và tất cả sizes/colors liên quan sẽ bị xóa
4. **Response Format**: Tất cả API đều trả về format chuẩn với `data`, `message`, `status`

## 👨‍💻 Tác giả

Dự án được phát triển bởi team Shop Online.

## 📄 License

Dự án này là dự án demo.

---

**Lưu ý:** Đây là tài liệu mô tả tổng quan về dự án. Để biết thêm chi tiết về từng API, vui lòng xem code hoặc sử dụng Swagger/Postman để test.


