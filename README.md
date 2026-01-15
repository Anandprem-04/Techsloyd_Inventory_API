# Techsloyd Inventory Management System - Complete Project Documentation

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.12-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12%2B-blue.svg)](https://www.postgresql.org/)

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [System Architecture](#system-architecture)
3. [Technology Stack](#technology-stack)
4. [Complete Project Structure](#complete-project-structure)
5. [Database Schema Deep Dive](#database-schema-deep-dive)
6. [Entity-Relationship Documentation](#entity-relationship-documentation)
7. [Repository Layer Documentation](#repository-layer-documentation)
8. [Controller Layer Documentation](#controller-layer-documentation)
9. [Setup Guide (Step-by-Step)](#setup-guide-step-by-step)
10. [API Endpoints Reference](#api-endpoints-reference)
11. [Configuration Files Explained](#configuration-files-explained)
12. [Running the Application](#running-the-application)
13. [Testing Guide](#testing-guide)
14. [Business Logic Explained](#business-logic-explained)
15. [Common Workflows](#common-workflows)
16. [Troubleshooting](#troubleshooting)

---

## 🎯 Project Overview

**Techsloyd Inventory Management System** is a production-ready Spring Boot REST API designed for retail and e-commerce inventory management. The system supports complex product hierarchies, variant management, and barcode scanning workflows.

### Key Features
- ✅ **Hierarchical Categories**: Unlimited nesting depth with parent-child relationships
- ✅ **Product Management**: Base products with SKU tracking
- ✅ **Product Variants**: Handle multiple variants per product (Size, Color, etc.)
- ✅ **Barcode Registry**: Central barcode management supporting 5 formats
- ✅ **Stock Tracking**: Real-time inventory levels at product and variant levels
- ✅ **Price Management**: Base pricing with variant-specific adjustments
- ✅ **Tree Operations**: Move categories with cycle detection
- ✅ **Search Capabilities**: Full-text search across products
- ✅ **CORS Enabled**: Works seamlessly with frontend applications

### Use Cases
1. **Retail POS Systems**: Barcode scanning for quick checkout
2. **E-commerce Platforms**: Product catalog with variants
3. **Warehouse Management**: Stock level tracking and reorder alerts
4. **Multi-store Inventory**: Centralized product database
5. **Mobile Applications**: REST API for mobile apps

### Target Users
- Retail shop owners
- E-commerce developers
- Inventory managers
- System integrators
- Warehouse administrators

---

## 🏗 System Architecture

### Architecture Pattern
This project follows a **3-Layer Architecture**:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (REST Controllers - JSON Response)     │
│  - CategoryController.java              │
│  - ProductController.java               │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│         Business Logic Layer            │
│     (Embedded in Controllers)           │
│  - Validation Logic                     │
│  - Tree Cycle Detection                 │
│  - Search Logic                         │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│         Data Access Layer               │
│  (Spring Data JPA Repositories)         │
│  - CategoryRepository.java              │
│  - ProductRepository.java               │
│  - BarcodeRepository.java               │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│         Database Layer                  │
│         (PostgreSQL 12+)                │
│  - 7 Normalized Tables                  │
│  - Foreign Key Constraints              │
│  - Check Constraints                    │
└─────────────────────────────────────────┘
```

### Design Patterns Used
- **Repository Pattern**: Spring Data JPA abstracts database access
- **DTO Pattern**: MoveCategoryRequest for data transfer
- **Entity Pattern**: JPA entities map to database tables
- **RESTful API Pattern**: Standard HTTP methods (GET, POST, PUT, DELETE)
- **Lazy Loading**: FetchType.LAZY for performance optimization
- **Cascade Operations**: Automatic deletion of child records

---

## 🛠 Technology Stack

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| **Language** | Java | 17 | Core programming language |
| **Framework** | Spring Boot | 3.4.12 | Application framework |
| **Web Framework** | Spring Web | 3.4.12 | REST API development |
| **ORM** | Hibernate (via JPA) | 6.x | Object-relational mapping |
| **Data Access** | Spring Data JPA | 3.4.12 | Repository abstraction |
| **Database** | PostgreSQL | 12+ | Relational database |
| **Validation** | Spring Validation | 3.4.12 | Input validation |
| **Build Tool** | Maven | 3.9.11 | Dependency management |
| **Utilities** | Lombok | 1.18.42 | Boilerplate code reduction |
| **Testing** | JUnit 5 | 5.x | Unit testing framework |
| **JSON** | Jackson | 2.x | JSON serialization |

### Maven Dependencies Explained

From `backend-api/pom.xml`:

```xml
<!-- Web Layer: REST Controllers -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data Layer: JPA + Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Database Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Validation Framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Code Generation (Getters/Setters) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 📁 Complete Project Structure

```
c:\Techsloyd\
│
├── README.md                                    # Project overview
├── PROJECT.md                                   # This comprehensive documentation
├── Setup.md                                     # Setup & installation guide
├── Schema.sql                                   # Database initialization script
│
├── .idea/                                       # IntelliJ IDEA configuration
│   ├── .gitignore                              # IDE-specific ignore rules
│   ├── compiler.xml                            # Java compiler settings
│   ├── dataSources.xml                         # Database connection config
│   ├── dataSources.local.xml                   # Local database credentials
│   ├── encodings.xml                           # File encoding settings (UTF-8)
│   ├── jarRepositories.xml                     # Maven repository URLs
│   ├── misc.xml                                # Project SDK configuration (Java 17)
│   ├── modules.xml                             # Project module structure
│   ├── sqldialects.xml                         # SQL dialect mappings
│   ├── Techsloyd.iml                           # IntelliJ module file
│   ├── vcs.xml                                 # Git version control settings
│   └── workspace.xml                           # IDE workspace state (ignored)
│
├── .vscode/                                     # Visual Studio Code configuration
│   └── settings.json                           # Java null analysis settings
│
└── backend-api/                                 # Main Spring Boot application
    │
    ├── .gitattributes                          # Git line-ending rules
    ├── .gitignore                              # Git ignore patterns
    ├── HELP.md                                  # Spring Boot generated help
    ├── mvnw                                     # Maven wrapper (Unix/Mac)
    ├── mvnw.cmd                                 # Maven wrapper (Windows)
    ├── pom.xml                                  # Maven project configuration
    │
    ├── .mvn/
    │   └── wrapper/
    │       └── maven-wrapper.properties         # Maven wrapper version config
    │
    ├── src/
    │   ├── main/
    │   │   ├── java/com/inventory/backend_api/
    │   │   │   │
    │   │   │   ├── BackendApiApplication.java          # Spring Boot entry point
    │   │   │   │
    │   │   │   ├── controller/                         # REST API Layer
    │   │   │   │   ├── CategoryController.java        # Handles category endpoints
    │   │   │   │   ├── ProductController.java         # Handles product endpoints
    │   │   │   │   ├── BarcodeController.java         # Handles barcode operations
    │   │   │   │   └── VariantController.java         # Handles variant endpoints
    │   │   │   │
    │   │   │   ├── services/                           # Business Logic Layer (Optional)
    │   │   │   │   ├── BarcodeService.java            # Barcode scanning logic
    │   │   │   │   ├── CategoryService.java           # Category operations logic
    │   │   │   │   └── VariantService.java            # Variant management logic
    │   │   │   │
    │   │   │   ├── dto/                                # Data Transfer Objects
    │   │   │   │   ├── MoveCategoryRequest.java       # DTO for moving categories
    │   │   │   │   ├── ScanResponse.java              # DTO for barcode scan response
    │   │   │   │   ├── CategoryStatsResponse.java     # DTO for category statistics
    │   │   │   │   └── CreateOptionRequest.java       # DTO for variant options
    │   │   │   │
    │   │   │   ├── entity/                             # JPA Entity Models (Database Tables)
    │   │   │   │   ├── Barcode.java                   # Maps to barcodes table
    │   │   │   │   ├── Category.java                  # Maps to categories table
    │   │   │   │   ├── Product.java                   # Maps to products table
    │   │   │   │   ├── ProductVariant.java            # Maps to product_variants table
    │   │   │   │   ├── VariantCombination.java        # Maps to variant_combinations table
    │   │   │   │   ├── VariantOption.java             # Maps to variant_options table
    │   │   │   │   └── VariantOptionValue.java        # Maps to variant_option_values table
    │   │   │   │
    │   │   │   └── repository/                         # Data Access Layer (Repositories)
    │   │   │       ├── BarcodeRepository.java         # Queries barcodes table
    │   │   │       ├── CategoryRepository.java        # Queries categories table
    │   │   │       ├── ProductRepository.java         # Queries products table
    │   │   │       ├── ProductVariantRepository.java  # Queries product_variants table
    │   │   │       ├── VariantCombinationRepository.java  # Queries combinations table
    │   │   │       ├── VariantOptionRepository.java   # Queries options table
    │   │   │       └── VariantOptionValueRepository.java  # Queries option_values table
    │   │   │
    │   │   └── resources/
    │   │       └── application.properties              # Database connection config
    │   │
    │   └── test/
    │       └── java/com/inventory/backend_api/
    │           └── BackendApiApplicationTests.java     # Spring Boot test class
    │
    └── target/                                          # Compiled build artifacts (auto-generated)
        ├── classes/                                     # Compiled .class files
        ├── generated-sources/                           # Lombok-generated code
        ├── generated-test-sources/                      # Test annotation processing
        └── backend-api-0.0.1-SNAPSHOT.jar              # Executable JAR file
```

---

## 🔄 Module Structure & File Organization

### Folder Organization & Relationships

#### 1. **Controller Layer** (`controller/` folder)
Contains REST API endpoints that handle HTTP requests.

| File | Purpose | Calls | Receives From |
|---|---|---|---|
| `CategoryController.java` | Category CRUD & tree operations | CategoryRepository | HTTP Client |
| `ProductController.java` | Product CRUD & search | ProductRepository | HTTP Client |
| `BarcodeController.java` | Barcode scanning & validation | BarcodeService | HTTP Client |
| `VariantController.java` | Variant management | ProductVariantRepository | HTTP Client |

#### 2. **Entity Layer** (`entity/` folder)
Contains JPA entities that map to database tables.

| File | Database Table | Used By | Relationships |
|---|---|---|---|
| `Category.java` | categories | CategoryRepository, CategoryController | Self-referencing (parent-child) |
| `Product.java` | products | ProductRepository, ProductController | ManyToOne → Category; OneToMany → ProductVariant |
| `ProductVariant.java` | product_variants | ProductVariantRepository | ManyToOne → Product; OneToMany → VariantCombination |
| `VariantOption.java` | variant_options | VariantOptionRepository | OneToMany → VariantOptionValue |
| `VariantOptionValue.java` | variant_option_values | VariantOptionValueRepository | ManyToOne → VariantOption |
| `VariantCombination.java` | variant_combinations | VariantCombinationRepository | Links ProductVariant ↔ VariantOptionValue |
| `Barcode.java` | barcodes | BarcodeRepository | OneToOne → Product OR ProductVariant (XOR) |

#### 3. **Repository Layer** (`repository/` folder)
Contains Spring Data JPA interfaces for database access.

| File | Entity | Uses | Called By |
|---|---|---|---|
| `CategoryRepository.java` | Category | JpaRepository | CategoryController, CategoryService |
| `ProductRepository.java` | Product | JpaRepository | ProductController, ProductService |
| `ProductVariantRepository.java` | ProductVariant | JpaRepository | VariantController, VariantService |
| `VariantOptionRepository.java` | VariantOption | JpaRepository | VariantService |
| `VariantOptionValueRepository.java` | VariantOptionValue | JpaRepository | VariantService |
| `VariantCombinationRepository.java` | VariantCombination | JpaRepository | VariantService |
| `BarcodeRepository.java` | Barcode | JpaRepository | BarcodeService, BarcodeController |

#### 4. **Service Layer** (`services/` folder - Optional)
Contains business logic and validation.

| File | Purpose | Uses | Used By |
|---|---|---|---|
| `BarcodeService.java` | Barcode scanning, validation, checksum | BarcodeRepository | BarcodeController |
| `CategoryService.java` | Category operations, cycle detection | CategoryRepository | CategoryController |
| `VariantService.java` | Variant creation, option management | ProductVariantRepository, VariantOptionRepository | VariantController |

#### 5. **DTO Layer** (`dto/` folder)
Contains request/response data transfer objects.

| File | Used For | Linked To |
|---|---|---|
| `MoveCategoryRequest.java` | POST /api/categories/move | CategoryController, Category |
| `ScanResponse.java` | Barcode scan response | BarcodeController, Barcode |
| `CategoryStatsResponse.java` | GET /api/categories/statistics | CategoryController |
| `CreateOptionRequest.java` | Create variant options | VariantController, VariantOption |

### How Files Communicate

```
┌─────────────────────────────────────────────────────────────┐
│                     CategoryController.java                 │
│  (REST endpoints: GET, POST, PUT, DELETE /api/categories)  │
└──────────────────────┬──────────────────────────────────────┘
                       │ calls
                       ↓
┌─────────────────────────────────────────────────────────────┐
│                  CategoryRepository.java                     │
│            (Spring Data JPA interface)                       │
│  Methods: findByParentIsNull(), findByParentId(), etc       │
└──────────────────────┬──────────────────────────────────────┘
                       │ queries
                       ↓
┌─────────────────────────────────────────────────────────────┐
│                    Category.java                             │
│              (JPA Entity - Data Model)                       │
│  Properties: id, name, slug, parent, children, etc          │
└──────────────────────┬──────────────────────────────────────┘
                       │ maps to
                       ↓
┌─────────────────────────────────────────────────────────────┐
│               PostgreSQL Database                            │
│                 categories table                             │
└─────────────────────────────────────────────────────────────┘
```

### Real Example: Product Creation Flow

```
HTTP Request: POST /api/products
    ↓
ProductController.createProduct()
    ↓
Receives: Product object
    ↓
ProductRepository.save()
    ↓
Validates: category_id exists in Category
    ↓
Product entity saved
    ↓
products table INSERT
    ↓
Return: HTTP 200 with created Product JSON
```

### Real Example: Product Search Flow

```
HTTP Request: GET /api/products/search?query=mouse
    ↓
ProductController.searchProducts()
    ↓
ProductRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase()
    ↓
Database SELECT query
    ↓
Filter results matching "mouse" in name or SKU
    ↓
Return: List<Product>
    ↓
Convert to JSON
    ↓
HTTP 200 response
```

---

## 🗄 Database Schema Deep Dive

### Schema Overview
The database consists of **7 normalized tables** following **3NF (Third Normal Form)** principles.

### Entity-Relationship Diagram (ERD)

```
┌──────────────────┐
│   CATEGORIES     │◄──┐ (Self-referencing)
├──────────────────┤   │
│ id (PK)          │   │
│ parent_id (FK) ──┴───┘
│ name             │
│ slug (UNIQUE)    │
│ description      │
│ icon             │
│ color            │
│ image            │
│ is_active        │
│ position         │
│ product_count    │
│ created_at       │
│ updated_at       │
└────────┬─────────┘
         │
         │ (1:N)
         │
         ▼
┌──────────────────────────┐
│      PRODUCTS            │
├──────────────────────────┤
│ id (PK)                  │
│ category_id (FK) ────────┘
│ name                     │
│ sku (UNIQUE)             │
│ description              │
│ image                    │
│ unit                     │
│ price                    │
│ cost_price               │
│ tax_rate                 │
│ stock_level              │
│ reorder_level            │
│ status (CHECK)           │
│ created_at               │
│ updated_at               │
└────────┬─────────────────┘
         │
         │ (1:N)
         │
         ▼
┌──────────────────────────┐         ┌─────────────────────────┐
│  PRODUCT_VARIANTS        │         │   VARIANT_OPTIONS       │
├──────────────────────────┤         ├─────────────────────────┤
│ id (PK)                  │         │ id (PK)                 │
│ product_id (FK) ─────────┘         │ name                    │
│ sku (UNIQUE)             │         │ position                │
│ price                    │         │ is_required             │
│ cost                     │         │ type (ENUM)             │
│ stock_level              │         └──────────┬──────────────┘
│ is_active                │                    │
└────────┬─────────────────┘                    │ (1:N)
         │                                       │
         │ (1:N)                                 ▼
         │                     ┌──────────────────────────────────┐
         │                     │  VARIANT_OPTION_VALUES           │
         │                     ├──────────────────────────────────┤
         │                     │ id (PK)                          │
         │                     │ variant_option_id (FK) ──────────┘
         │                     │ value                            │
         │                     │ display_value                    │
         │                     │ position                         │
         │                     │ price_adjustment_type (ENUM)     │
         │                     │ price_adjustment_value           │
         │                     └──────────┬───────────────────────┘
         │                                │
         │                                │ (M:N via Bridge)
         │                                │
         │                     ┌──────────▼───────────────┐
         │                     │  VARIANT_COMBINATIONS    │
         │                     ├──────────────────────────┤
         │                     │ id (PK)                  │
         └─────────────────────┤ product_variant_id (FK)  │
                               │ variant_option_value_id  │
                               └──────────────────────────┘

┌─────────────────────────┐
│      BARCODES           │
├─────────────────────────┤
│ barcode (PK)            │
│ format (ENUM)           │
│ product_id (FK, opt)    │────► PRODUCTS
│ product_variant_id (FK) │────► PRODUCT_VARIANTS
│ created_at              │
└─────────────────────────┘
Constraint: XOR (either product OR variant)
```

### Table Specifications

#### 1. CATEGORIES Table
Stores hierarchical product categories with unlimited nesting.

```sql
CREATE TABLE categories (
    id VARCHAR(50) PRIMARY KEY,
    parent_id VARCHAR(50),
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(255),
    color VARCHAR(50),
    image VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    position INTEGER DEFAULT 0,
    product_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) 
        REFERENCES categories(id) ON DELETE SET NULL
);
```

**Key Features:**
- Self-referencing via `parent_id` enables tree structure
- `ON DELETE SET NULL`: Orphaned categories become roots
- `slug` must be globally unique for URL routing
- `position` allows manual ordering within siblings
- `product_count` caches the number of products

**Example Data:**
```
Electronics (id: e1, parent_id: NULL)
├── Phones (id: p1, parent_id: e1)
│   └── Smartphones (id: s1, parent_id: p1)
└── Computers (id: c1, parent_id: e1)
```

#### 2. PRODUCTS Table
Core product information without variant details.

```sql
CREATE TABLE products (
    id VARCHAR(50) PRIMARY KEY,
    category_id VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    image VARCHAR(255),
    unit VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    cost_price DECIMAL(10, 2),
    tax_rate DECIMAL(5, 2) DEFAULT 0.0,
    stock_level INTEGER DEFAULT 0,
    reorder_level INTEGER DEFAULT 10,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT chk_product_status CHECK (status IN ('active', 'inactive'))
);
```

**Key Features:**
- `ON DELETE RESTRICT`: Cannot delete category with products
- `sku` is unique across all products
- `stock_level` can be sum of variant stocks
- `status` determines visibility in POS
- `tax_rate` is product-specific for compliance

**Example Data:**
```json
{
  "id": "prod-001",
  "category_id": "c1",
  "name": "Basic T-Shirt",
  "sku": "TSHIRT-BASIC",
  "price": 19.99,
  "cost_price": 8.00,
  "tax_rate": 10.0,
  "stock_level": 150,
  "status": "active"
}
```

#### 3. VARIANT_OPTIONS Table
Defines global option types (Size, Color, Material).

```sql
CREATE TABLE variant_options (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position INTEGER DEFAULT 0,
    is_required BOOLEAN DEFAULT TRUE,
    type VARCHAR(20) NOT NULL,
    
    CONSTRAINT chk_option_type CHECK (type IN ('BUTTON', 'DROPDOWN', 'SWATCH'))
);
```

**Option Types:**
- `BUTTON`: Radio button selector
- `DROPDOWN`: Dropdown menu
- `SWATCH`: Visual color/image selector

**Example Data:**
```json
[
  {"id": "opt-1", "name": "Size", "type": "BUTTON", "position": 1},
  {"id": "opt-2", "name": "Color", "type": "SWATCH", "position": 2}
]
```

#### 4. VARIANT_OPTION_VALUES Table
Specific values for each option.

```sql
CREATE TABLE variant_option_values (
    id VARCHAR(50) PRIMARY KEY,
    variant_option_id VARCHAR(50) NOT NULL,
    value VARCHAR(100) NOT NULL,
    display_value VARCHAR(100),
    position INTEGER DEFAULT 0,
    price_adjustment_type VARCHAR(20) DEFAULT 'FIXED',
    price_adjustment_value DECIMAL(10, 2) DEFAULT 0.0,
    
    CONSTRAINT fk_value_option FOREIGN KEY (variant_option_id) 
        REFERENCES variant_options(id) ON DELETE CASCADE,
    CONSTRAINT chk_price_adj_type CHECK (price_adjustment_type IN ('FIXED', 'PERCENTAGE'))
);
```

**Price Adjustment Examples:**
- `FIXED`: +$5.00 for "Large" size
- `PERCENTAGE`: +10% for "Premium" material

**Example Data:**
```json
{
  "id": "val-1",
  "variant_option_id": "opt-2",
  "value": "Red",
  "display_value": "#FF0000",
  "price_adjustment_type": "FIXED",
  "price_adjustment_value": 2.00
}
```

#### 5. PRODUCT_VARIANTS Table
Physical SKUs with specific attribute combinations.

```sql
CREATE TABLE product_variants (
    id VARCHAR(50) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    cost DECIMAL(10, 2),
    stock_level INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) 
        REFERENCES products(id) ON DELETE CASCADE
);
```

**Key Features:**
- Each variant has unique `sku`
- `ON DELETE CASCADE`: Delete variants when product deleted
- Price can differ from base product price
- Stock tracked independently per variant

**Example Data:**
```json
{
  "id": "var-001",
  "product_id": "prod-001",
  "sku": "TSHIRT-RED-M",
  "price": 21.99,
  "stock_level": 25,
  "is_active": true
}
```

#### 6. VARIANT_COMBINATIONS Table
Bridge table linking variants to their option values.

```sql
CREATE TABLE variant_combinations (
    id VARCHAR(50) PRIMARY KEY,
    product_variant_id VARCHAR(50) NOT NULL,
    variant_option_value_id VARCHAR(50) NOT NULL,
    
    CONSTRAINT fk_combo_variant FOREIGN KEY (product_variant_id) 
        REFERENCES product_variants(id) ON DELETE CASCADE,
    CONSTRAINT fk_combo_value FOREIGN KEY (variant_option_value_id) 
        REFERENCES variant_option_values(id) ON DELETE RESTRICT
);
```

**Example: "Red Medium T-Shirt"**
```
variant_id: var-001 (TSHIRT-RED-M)
Combinations:
  - combo-1: var-001 → val-1 (Red)
  - combo-2: var-001 → val-5 (Medium)
```

#### 7. BARCODES Table
Central barcode registry with format validation.

```sql
CREATE TABLE barcodes (
    barcode VARCHAR(100) PRIMARY KEY,
    format VARCHAR(20) NOT NULL,
    product_id VARCHAR(50),
    product_variant_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_barcode_format CHECK (
        format IN ('UPC_A', 'UPC_E', 'EAN_13', 'EAN_8', 'CODE_128')
    ),
    CONSTRAINT chk_barcode_target CHECK (
        (product_id IS NOT NULL AND product_variant_id IS NULL) OR 
        (product_id IS NULL AND product_variant_id IS NOT NULL)
    ),
    CONSTRAINT fk_barcode_product FOREIGN KEY (product_id) 
        REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_barcode_variant FOREIGN KEY (product_variant_id) 
        REFERENCES product_variants(id) ON DELETE CASCADE
);
```

**Barcode Formats:**
- **UPC-A**: 12 digits (North America)
- **UPC-E**: 8 digits (compact)
- **EAN-13**: 13 digits (International)
- **EAN-8**: 8 digits (small items)
- **CODE-128**: Alphanumeric

**XOR Constraint:** A barcode links to EITHER a product OR a variant, never both.

---

## 🔐 Entity & Repository Documentation

### Entity Classes Overview & Relationships

#### Entity Layer Structure

| Entity | Database Table | Primary Purpose | Key Relationships |
|--------|---|---|---|
| **Category** | `categories` | Hierarchical product categories | Self-referencing (parent-child tree) |
| **Product** | `products` | Base product information | ManyToOne → Category; OneToMany → ProductVariant |
| **ProductVariant** | `product_variants` | Specific SKU with attributes | ManyToOne → Product; OneToMany → VariantCombination |
| **VariantOption** | `variant_options` | Option types (Size, Color) | OneToMany → VariantOptionValue |
| **VariantOptionValue** | `variant_option_values` | Option values (Red, Small, etc) | ManyToOne → VariantOption |
| **VariantCombination** | `variant_combinations` | Bridge between Variants & Values | ManyToOne → ProductVariant; ManyToOne → VariantOptionValue |
| **Barcode** | `barcodes` | Barcode registry | OneToOne → Product OR OneToOne → ProductVariant |

#### Entity Relationships Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      Category.java                           │
│                   (Hierarchical Tree)                        │
│  Self-referencing: parent ← → children                       │
└──────────────────────┬──────────────────────────────────────┘
                       │ 1:N (category → products)
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                      Product.java                            │
│           (Base Product with SKU & Pricing)                 │
│  Links to: Category, ProductVariant, Barcode               │
└──────────────────────┬──────────────────────────────────────┘
                       │ 1:N (product → variants)
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                 ProductVariant.java                          │
│            (Physical Item with Specific SKU)                │
│  Links to: Product, VariantCombination, Barcode            │
└──────────────────────┬──────────────────────────────────────┘
                       │ 1:N (variant → combinations)
                       ▼
┌──────────────────────────────────┬──────────────────────────┐
│   VariantCombination.java        │                          │
│ (Bridge: Variant ↔ Values)       │                          │
└────────────────────┬─────────────┘                          │
                     │ N:1                                     │ N:1
                     ▼                                         ▼
     ┌───────────────────────────────┐     ┌─────────────────────────┐
     │  VariantOptionValue.java      │     │  VariantOption.java     │
     │  (Specific Values: Red, M)    │     │  (Option Types)         │
     │  Links to: VariantOption      │     │  Links to: Values       │
     └───────────────────────────────┘     └─────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                      Barcode.java                            │
│              (Central Barcode Registry)                      │
│  XOR Link: Either Product OR ProductVariant (not both)      │
└─────────────────────────────────────────────────────────────┘
```

### Repository Interfaces (Data Access Layer)

| Repository | Entity | Purpose | Key Methods |
|---|---|---|---|
| **CategoryRepository** | Category | Category tree operations | `findByParentIsNull()`, `findByParentId()` |
| **ProductRepository** | Product | Product search & filtering | `findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase()`, `findByCategoryOrParentId()` |
| **ProductVariantRepository** | ProductVariant | Variant lookup | `findByProductId()`, `findBySku()` |
| **VariantOptionRepository** | VariantOption | Option type filtering | `findByType()` |
| **VariantOptionValueRepository** | VariantOptionValue | Value lookup | `findByVariantOptionId()` |
| **VariantCombinationRepository** | VariantCombination | Bridge table CRUD | Standard JPA operations |
| **BarcodeRepository** | Barcode | Barcode scanning | `findByBarcode()`, `existsByBarcode()` |

#### How Repositories Connect:

```
CategoryRepository
    ↓
ProductRepository (queries categories via joins)
    ↓
ProductVariantRepository (finds variants by product)
    ↓
VariantCombinationRepository (links variants to option values)
    ↓
VariantOptionRepository & VariantOptionValueRepository
    
BarcodeRepository (independent, links to Product OR ProductVariant)
```

---

## 🎮 Controller Layer Documentation

### Controller Responsibilities & Relationships

| Controller | Primary Entity | Related Entities | Key Operations |
|---|---|---|---|
| **CategoryController** | Category | Products (via category) | CRUD, Tree reorganization, Cycle detection, Statistics |
| **ProductController** | Product | Category, ProductVariant | CRUD, Search, Filtering by category, Bulk operations |
| **BarcodeController** | Barcode | Product & ProductVariant | Scan, Lookup, Assign, Validate |
| **VariantController** | ProductVariant | Product, VariantCombination, VariantOption | Variant management, Attribute linking |

### Data Flow Between Layers

```
HTTP Request
    ↓
┌─────────────────────────────────────────┐
│   Controller Layer (REST Endpoints)     │
│   - Request validation                  │
│   - Response formatting                 │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│   Repository Layer (Data Access)        │
│   - Query execution                     │
│   - Entity retrieval/persistence        │
└──────────────┬──────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│   Database Layer (PostgreSQL)           │
│   - Tables with constraints             │
│   - Relationships & triggers            │
└─────────────────────────────────────────┘
```

### File Connections

**CategoryController.java** connects to:
- `CategoryRepository` (for database queries)
- `Category` entity (data model)
- `ProductRepository` (indirect - for counting products in category)

**ProductController.java** connects to:
- `ProductRepository` (main data access)
- `Product` entity (data model)
- `Category` entity (foreign key relationship)
- `ProductVariant` entity (variants of product)

**BarcodeController.java** connects to:
- `BarcodeService` (business logic)
- `BarcodeRepository` (data access)
- `Barcode` entity (data model)
- `Product` & `ProductVariant` (XOR relationship)

---

## ⚙️ Quick Setup Summary

For detailed step-by-step installation instructions, **refer to [Setup.md](Setup.md)** in the project root.

### Quick Checklist
- [ ] Java 17+ installed
- [ ] PostgreSQL 12+ running with `api` database
- [ ] `Schema.sql` executed to create tables
- [ ] `application.properties` configured with correct credentials
- [ ] Maven build successful: `mvnw clean install`
- [ ] Application running: `mvnw spring-boot:run`
- [ ] API accessible at `http://localhost:8080/api`

---

## 🔗 Module Interconnection Overview

### Complete Flow Example: Creating a Product with Variants

```
1. CategoryController (HTTP POST /api/categories)
   ↓
   CategoryRepository.save()
   ↓
   categories table created

2. ProductController (HTTP POST /api/products)
   ↓
   ProductRepository.save()
   ↓
   Validates foreign key: category_id → categories table
   ↓
   products table created with category_id

3. VariantController (HTTP POST /api/product-variants)
   ↓
   ProductVariantRepository.save()
   ↓
   Validates foreign key: product_id → products table
   ↓
   product_variants table created

4. VariantOptionController (HTTP POST /api/variant-options)
   ↓
   VariantOptionRepository.save()
   ↓
   variant_options table created

5. VariantOptionValueController (HTTP POST /api/variant-option-values)
   ↓
   VariantOptionValueRepository.save()
   ↓
   variant_option_values table created

6. VariantCombinationController (HTTP POST /api/variant-combinations)
   ↓
   VariantCombinationRepository.save()
   ↓
   Validates: product_variant_id → product_variants
   Validates: variant_option_value_id → variant_option_values
   ↓
   variant_combinations table created
```

### Dependency Chain

```
BackendApiApplication.java (Entry Point)
    ↓
Application Configuration
    ↓
├── CategoryController ─→ CategoryRepository ─→ Category Entity ─→ categories table
│
├── ProductController ─→ ProductRepository ─→ Product Entity ─→ products table
│                           ↓
│                    (requires: Category)
│
├── VariantController ─→ ProductVariantRepository ─→ ProductVariant Entity ─→ product_variants table
│                            ↓
│                     (requires: Product)
│
├── VariantOptionController ─→ VariantOptionRepository ─→ VariantOption Entity ─→ variant_options table
│
├── VariantOptionValueController ─→ VariantOptionValueRepository ─→ VariantOptionValue Entity ─→ variant_option_values table
│                                        ↓
│                                 (requires: VariantOption)
│
├── VariantCombinationController ─→ VariantCombinationRepository ─→ VariantCombination Entity ─→ variant_combinations table
│                                        ↓
│                         (requires: ProductVariant + VariantOptionValue)
│
└── BarcodeController ─→ BarcodeService ─→ BarcodeRepository ─→ Barcode Entity ─→ barcodes table
                                              ↓
                         (requires: Product OR ProductVariant)
```

### Service Layer Integration

| Service | Interacts With | Primary Function |
|---|---|---|
| **BarcodeService** | BarcodeRepository, ProductRepository, ProductVariantRepository | Scan validation, barcode format checking, checksum verification |
| **CategoryService** (if exists) | CategoryRepository | Tree cycle detection, parent-child validation |
| **ProductService** (if exists) | ProductRepository, VariantRepository | Search, filtering, stock aggregation |

---

## 📡 API Endpoints Reference

### Base URL
```
http://localhost:8080/api
```

### Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/categories` | Get all categories (flat) |
| GET | `/categories/tree` | Get category tree (hierarchical) |
| GET | `/categories/{id}` | Get single category |
| POST | `/categories` | Create new category |
| PUT | `/categories/{id}` | Update category |
| DELETE | `/categories/{id}` | Delete category |
| POST | `/categories/move` | Move category in tree |

### Product Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | Get all products (paginated) |
| GET | `/products/{id}` | Get single product |
| POST | `/products` | Create new product |
| PUT | `/products/{id}` | Update product |
| DELETE | `/products/{id}` | Delete product |
| GET | `/products/search` | Search products by name/SKU |
| GET | `/products/by-category/{id}` | Get products by category |

### Example Requests

#### Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "slug": "electronics",
    "description": "Electronic devices and accessories",
    "isActive": true,
    "position": 1
  }'
```

#### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "category": {"id": "category-uuid-here"},
    "name": "Wireless Mouse",
    "sku": "MOUSE-001",
    "description": "Ergonomic wireless mouse",
    "price": 29.99,
    "costPrice": 15.00,
    "taxRate": 10.0,
    "stockLevel": 50,
    "reorderLevel": 10,
    "status": "active"
  }'
```

#### Search Products
```bash
curl "http://localhost:8080/api/products/search?query=mouse"
```

---

## 🔧 Configuration Files Explained

### application.properties
Located at: `backend-api/src/main/resources/application.properties`

```properties
# Application Name
spring.application.name=backend-api

# Database Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/api
spring.datasource.username=postgres
spring.datasource.password=Anand@2004#

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
# Options:
#   - validate: Check schema matches entities (RECOMMENDED FOR PRODUCTION)
#   - update: Auto-update schema (USE WITH CAUTION)
#   - create-drop: Recreate schema on startup (DEVELOPMENT ONLY)
#   - none: No schema management

# SQL Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Database Dialect
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### pom.xml Key Sections
Located at: `backend-api/pom.xml`

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.12</version>
</parent>

<properties>
    <java.version>17</java.version>
</properties>
```

---

## 🚀 Running the Application

### Development Mode
```bash
cd backend-api
./mvnw spring-boot:run
```

### Production Mode
```bash
# Build JAR
./mvnw clean package

# Run JAR
java -jar target/backend-api-0.0.1-SNAPSHOT.jar
```

### Using IDE

#### IntelliJ IDEA
1. Open project: File → Open → Select `Techsloyd` folder
2. Wait for Maven import to complete
3. Right-click `BackendApiApplication.java`
4. Select "Run 'BackendApiApplication'"

#### VS Code
1. Install "Extension Pack for Java"
2. Open `Techsloyd` folder
3. Press `F5` to start debugging

---

## 🧪 Testing Guide

### Running Unit Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BackendApiApplicationTests
```

### Test Structure
```
src/test/java/com/inventory/backend_api/
└── BackendApiApplicationTests.java
```

---

## 💼 Business Logic Explained

### Category Tree Management
When moving a category, the system:
1. Validates both category IDs exist
2. Checks for self-parenting
3. Walks up the tree from new parent to root
4. Detects cycles

### Product Variant System
Final variant price = Base price + Adjustments

**Example:**
```
Base Product: T-Shirt ($20)
Variant: Large + Red

Option Values:
- Large: +$5 (FIXED)
- Red: +10% (PERCENTAGE)

Calculation:
$20 + $5 = $25
$25 * 1.10 = $27.50 (final price)
```

### Barcode Scanning Workflow
1. Scan barcode at POS
2. Lookup in `barcodes` table
3. Determine target (product or variant)
4. Retrieve price and stock
5. Process transaction

---

## 🔄 Common Workflows

### Workflow 1: Add New Product with Variants
```bash
# Create variant options
curl -X POST http://localhost:8080/api/variant-options \
  -d '{"name":"Size","type":"BUTTON"}'

# Create option values
curl -X POST http://localhost:8080/api/variant-option-values \
  -d '{"variantOptionId":"size-id","value":"Small"}'

# Create base product
curl -X POST http://localhost:8080/api/products \
  -d '{"name":"T-Shirt","sku":"TSHIRT-001","price":19.99}'

# Create variant
curl -X POST http://localhost:8080/api/product-variants \
  -d '{"productId":"prod-id","sku":"TSHIRT-RED-S","price":21.99}'

# Link variant to values
curl -X POST http://localhost:8080/api/variant-combinations \
  -d '{"productVariantId":"var-id","variantOptionValueId":"red-id"}'
```

### Workflow 2: Reorganize Category Tree
```bash
# Move "Smartphones" under "Phones"
curl -X POST http://localhost:8080/api/categories/move \
  -d '{"categoryId":"smartphones-id","newParentId":"phones-id"}'
```

### Workflow 3: Search and Filter
```bash
# Search products
curl "http://localhost:8080/api/products/search?query=wireless"

# Get category products
curl "http://localhost:8080/api/products/by-category/electronics-id?page=0&size=20"
```

---

## 🐛 Troubleshooting

### Issue 1: Application Won't Start
**Error:** `Error creating bean with name 'dataSource'`

**Solution:**
- Check PostgreSQL is running: `psql -U postgres`
- Verify credentials in `application.properties`
- Ensure database `api` exists

### Issue 2: Table Not Found
**Error:** `PSQLException: ERROR: relation "categories" does not exist`

**Solution:**
- Run `Schema.sql` in PostgreSQL
- Verify `ddl-auto=validate` in properties
- Check database name in connection URL

### Issue 3: Port Already in Use
**Error:** `Web server failed to start. Port 8080 was already in use`

**Solution:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080  # Windows
lsof -i :8080                  # Mac/Linux

# Kill process or change port
server.port=8090
```

### Issue 4: Lombok Not Working
**Error:** `java: cannot find symbol symbol: method getName()`

**Solution:**
- Enable annotation processing:
  - IntelliJ: Settings → Build → Compiler → Annotation Processors
  - VS Code: Install Lombok extension
- Rebuild: `./mvnw clean compile`

### Issue 5: JSON Infinite Recursion
**Error:** `StackOverflowError during JSON serialization`

**Solution:**
Already handled with `@JsonBackReference` / `@JsonManagedReference` in Category entity.

---

## 📚 Additional Resources

### Spring Boot Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/3.4.12/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)

### PostgreSQL
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

### Maven
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 📝 Summary

This Techsloyd Inventory Management System is a comprehensive, production-ready Spring Boot application with:

- **7 normalized database tables** for complex inventory management
- **RESTful API** with 14+ endpoints
- **Complete CRUD operations** for categories and products
- **Advanced features** like tree reorganization, variant management, and barcode scanning
- **Clean architecture** following industry best practices
- **Detailed documentation** for beginner and advanced developers

For questions or support, refer to the troubleshooting section or consult the Spring Boot documentation.

---

**Last Updated:** January 13, 2026  
**Version:** 1.0  
**Status:** Production Ready
