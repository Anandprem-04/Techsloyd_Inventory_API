# Techsloyd Inventory Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.12-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12%2B-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-red.svg)](https://maven.apache.org/)

A production-ready **Spring Boot REST API** for retail and e-commerce inventory management with hierarchical categories, product variants, and barcode scanning capabilities.

## 📋 Quick Navigation
- 🚀 [Quick Start](#quick-start) - Get running in 5 minutes
- 📚 [Documentation](#documentation) - Detailed guides
- 🏗 [Architecture](#architecture) - System design overview
- 🛠 [Tech Stack](#tech-stack) - Technologies used
- 📁 [Project Structure](#project-structure) - Folder organization

---

## 🎯 Overview

**Techsloyd** is an enterprise-grade inventory management system designed for:
- 🏪 Retail POS Systems
- 🛍️ E-commerce Platforms  
- 📦 Warehouse Management
- 📱 Mobile Applications

### Key Features
- ✅ Hierarchical product categories (unlimited nesting)
- ✅ Product variants with attribute combinations (Size, Color, etc.)
- ✅ Barcode registry (5 formats: UPC-A, UPC-E, EAN-13, EAN-8, CODE-128)
- ✅ Real-time stock tracking
- ✅ Price management with variant adjustments
- ✅ Tree operations with cycle detection
- ✅ Full-text product search
- ✅ CORS-enabled for frontend integration

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Maven 3.9+ (or use bundled `mvnw`)

### 3-Step Setup

```bash
# 1. Clone repository
git clone <repo-url> && cd Techsloyd

# 2. Create database and run schema
psql -U postgres -d api -f Schema.sql

# 3. Configure & Run
cd backend-api
mvnw spring-boot:run
```

**API available at:** `http://localhost:8080/api`

➡️ **Detailed Setup:** See [Setup.md](Setup.md)

---

## 📚 Documentation

| Document | Purpose |
|---|---|
| [Task.md](Task.md) | Internship project assignment - 30 APIs across 4 modules |
| [PROJECT.md](PROJECT.md) | Complete system architecture, module organization, API endpoints |
| [SQL.md](SQL.md) | Database schema, ER diagrams, table relationships, workflows |
| [Setup.md](Setup.md) | Step-by-step installation for all operating systems |

---

## 🏗 Architecture

### 3-Layer Design
```
Controllers (REST API)
    ↓
Services & Repositories (Business Logic & Data Access)
    ↓
Database (PostgreSQL - 7 Normalized Tables)
```

### Entity Relationships
```
Category (self-referencing tree)
    ↓
Product
    ├→ ProductVariant
    │   ├→ VariantCombination
    │   └→ VariantOption & VariantOptionValue
    │
    └→ Barcode
```

---

## 🛠 Tech Stack

| Component | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Framework | Spring Boot | 3.4.12 |
| Database | PostgreSQL | 12+ |
| ORM | Hibernate/JPA | 6.x |
| Build | Maven | 3.9.11 |
| Utilities | Lombok | 1.18.42 |
| Testing | JUnit 5 | 5.x |

---

## 📁 Project Structure

```
Techsloyd/
├── README.md              # This file
├── PROJECT.md             # System architecture & module details
├── SQL.md                 # Database schema & ER diagrams
├── Setup.md               # Installation guide
├── Task.md                # Internship project assignment
├── Schema.sql             # Database initialization script
│
└── backend-api/           # Spring Boot application
    ├── pom.xml            # Maven dependencies
    ├── mvnw               # Maven wrapper (Linux/macOS)
    ├── mvnw.cmd           # Maven wrapper (Windows)
    ├── HELP.md
    │
    ├── src/
    │   ├── main/
    │   │   ├── java/com/inventory/backend_api/
    │   │   │   ├── BackendApiApplication.java       # Main entry point
    │   │   │   │
    │   │   │   ├── controller/                       # REST endpoints (30 APIs)
    │   │   │   │   ├── ProductController.java        # 8 Product APIs
    │   │   │   │   ├── CategoryController.java       # 9 Category APIs
    │   │   │   │   ├── VariantController.java        # 10 Variant APIs
    │   │   │   │   └── BarcodeController.java        # 3 Barcode APIs
    │   │   │   │
    │   │   │   ├── services/                         # Business logic
    │   │   │   │   └── VariantService.java
    │   │   │   │
    │   │   │   ├── entity/                           # JPA entities (7 classes)
    │   │   │   │   ├── Barcode.java
    │   │   │   │   ├── Category.java
    │   │   │   │   ├── Product.java
    │   │   │   │   ├── ProductVariant.java
    │   │   │   │   ├── VariantCombination.java
    │   │   │   │   ├── VariantOption.java
    │   │   │   │   └── VariantOptionValue.java
    │   │   │   │
    │   │   │   ├── repository/                       # Data access (7 interfaces)
    │   │   │   │   ├── BarcodeRepository.java
    │   │   │   │   ├── CategoryRepository.java
    │   │   │   │   ├── ProductRepository.java
    │   │   │   │   ├── ProductVariantRepository.java
    │   │   │   │   ├── VariantCombinationRepository.java
    │   │   │   │   ├── VariantOptionRepository.java
    │   │   │   │   └── VariantOptionValueRepository.java
    │   │   │   │
    │   │   │   └── dto/                              # DTOs & models
    │   │   │       ├── BarcodeService.java
    │   │   │       ├── CategoryReorderRequest.java
    │   │   │       ├── CategoryStatsResponse.java
    │   │   │       ├── CreateOptionRequest.java
    │   │   │       ├── CreateValueRequest.java
    │   │   │       ├── GenerateMatrixRequest.java
    │   │   │       ├── MoveCategoryRequest.java
    │   │   │       └── ScanResponse.java
    │   │   │
    │   │   └── resources/
    │   │       ├── application.properties             # Spring configuration
    │   │       ├── static/                            # Static assets
    │   │       └── templates/                         # HTML templates
    │   │
    │   └── test/
    │       └── java/com/inventory/backend_api/
    │           └── BackendApiApplicationTests.java    # Unit tests
    │
    └── target/                                        # Build output (auto-generated)
        ├── classes/
        ├── generated-sources/
        └── test-classes/
```

---

## 🎯 Modules Overview

This project consists of **4 modules** with **30 RESTful APIs**:

### Module 1: Product Management (8 APIs)
**Priority:** HIGH | **Complexity:** Medium

Core product CRUD operations with search, filtering, pagination, and bulk operations.

| Endpoint | Method | Purpose |
|---|---|---|
| `/api/products` | GET | Get all products with pagination |
| `/api/products/:id` | GET | Get product details |
| `/api/products` | POST | Create new product |
| `/api/products/:id` | PUT | Update product |
| `/api/products/:id` | DELETE | Delete product |
| `/api/products/search` | GET | Full-text search |
| `/api/products/bulk-update` | POST | Bulk update products |
| `/api/products/bulk-delete` | POST | Bulk delete products |

### Module 2: Category Management (9 APIs)
**Priority:** HIGH | **Complexity:** Medium-High

Hierarchical category tree management with reordering and statistics.

| Endpoint | Method | Purpose |
|---|---|---|
| `/api/categories` | GET | Get all categories |
| `/api/categories/tree` | GET | Get hierarchical tree |
| `/api/categories/:id` | GET | Get category details |
| `/api/categories` | POST | Create category |
| `/api/categories/:id` | PUT | Update category |
| `/api/categories/:id` | DELETE | Delete category |
| `/api/categories/move` | POST | Move category in tree |
| `/api/categories/reorder` | POST | Reorder categories |
| `/api/categories/statistics` | GET | Get category stats |

### Module 3: Variant Management (10 APIs)
**Priority:** MEDIUM | **Complexity:** High

Product variants with options, values, auto-generated matrix, and inventory management.

| Endpoint | Method | Purpose |
|---|---|---|
| `/api/variants/options` | GET | Get all options |
| `/api/variants/options` | POST | Create option |
| `/api/variants/options/:id/values` | GET | Get option values |
| `/api/variants/options/:id/values` | POST | Create option value |
| `/api/products/:id/variants` | GET | Get product variants |
| `/api/variants/generate-matrix` | POST | Auto-generate variants |
| `/api/variants/:id/inventory` | PUT | Update inventory |
| `/api/variants/:id/pricing` | PUT | Update pricing |
| `/api/variants/options/:id` | DELETE | Delete option |
| `/api/variants/options/:id/values/:valueId` | DELETE | Delete value |

### Module 4: Barcode Scanner (3 APIs)
**Priority:** MEDIUM | **Complexity:** Low

Barcode scanning and validation with POS integration.

| Endpoint | Method | Purpose |
|---|---|---|
| `/api/barcode/scan` | POST | Process barcode scan |
| `/api/barcode/lookup/:barcode` | GET | Lookup by barcode |
| `/api/barcode/validate` | POST | Validate format |

**Total:** 8 + 9 + 10 + 3 = **30 APIs** ✅

---

## 📡 API Overview

---

## 💻 Quick Commands

```bash
# Build project
mvnw clean install

# Run application
mvnw spring-boot:run

# Run tests
mvnw test

# Build JAR for production
mvnw clean package
java -jar target/backend-api-0.0.1-SNAPSHOT.jar
```

---

## 🗄 Database

**PostgreSQL** with 7 normalized tables (3NF):
- categories, products, product_variants, variant_options, variant_option_values, variant_combinations, barcodes

➡️ **Schema Details:** See [SQL.md](SQL.md) for ER diagrams, relationships, and workflows

---

## 🔧 Configuration

Edit `backend-api/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/api
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=validate
```

---

## 🎯 Example Usage

### Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "slug": "electronics",
    "description": "Electronic devices"
  }'
```

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "category": {"id": "category-uuid"},
    "name": "Wireless Mouse",
    "sku": "MOUSE-001",
    "price": 29.99,
    "stockLevel": 50
  }'
```

### Search Products
```bash
curl "http://localhost:8080/api/products/search?query=mouse"
```

---

## 🐛 Troubleshooting

| Issue | Solution |
|---|---|
| PostgreSQL connection refused | Ensure PostgreSQL is running: `psql -U postgres` |
| Table not found | Run Schema.sql: `psql -U postgres -d api -f Schema.sql` |
| Port 8080 in use | Change port: `server.port=8090` in application.properties |
| Build fails | Clear cache: `mvnw clean compile` |

➡️ **More Help:** See [Setup.md - Troubleshooting](Setup.md#troubleshooting)

---

## 📖 Learning Path

1. Read [PROJECT.md](PROJECT.md) - Understand system architecture
2. Review [SQL.md](SQL.md) - Study database design
3. Follow [Setup.md](Setup.md) - Get it running
4. Explore API endpoints - Test with Postman/cURL
5. Review source code - Understand implementation

---

## 📝 Features & Workflows

### Supported Workflows
- ✅ Create hierarchical product categories
- ✅ Add products with multiple variants
- ✅ Link variant options (Size, Color, etc.)
- ✅ Manage product pricing and stock
- ✅ Scan barcodes for POS systems
- ✅ Search products by name/SKU
- ✅ Move categories in tree structure

### Business Logic Highlights
- **Cycle Detection**: Prevents circular category hierarchies
- **Variant Pricing**: Supports fixed and percentage adjustments
- **Barcode Formats**: UPC, EAN, CODE-128 validation
- **Stock Tracking**: Independent variant-level inventory

---

## 🤝 Contributing

1. Clone repository
2. Create feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m "Add feature"`
4. Push branch: `git push origin feature/your-feature`
5. Open pull request

---

## 📄 Project Files

- **Schema.sql** - Database creation script
- **pom.xml** - Maven dependencies
- **application.properties** - Spring Boot configuration
- **BackendApiApplication.java** - Application entry point

---

## 🔗 Related Documentation

- [PROJECT.md](PROJECT.md) - Detailed system architecture
- [SQL.md](SQL.md) - Database schema reference
- [Setup.md](Setup.md) - Complete installation guide
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/3.4.12/reference/)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)

---

## 📊 Project Status

- **Version:** 1.0
- **Status:** ✅ Production Ready
- **Last Updated:** January 16, 2026
- **License:** MIT

---

## 💡 Support

For issues or questions:
1. Check [Setup.md - Troubleshooting](Setup.md#troubleshooting)
2. Review [PROJECT.md](PROJECT.md) for detailed architecture
3. Consult [SQL.md](SQL.md) for database questions
4. Search [Spring Boot Documentation](https://docs.spring.io/spring-boot/)

---

**Happy Coding! 🚀**


