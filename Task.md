# Internship Project - Task Assignment

**Intern:** Anand Prem  
**Project:** Techsloyd Inventory Management System  
**Focus:** Product & Catalog Management  
**Date:** January 2026  

---

## 📋 Project Overview

Build RESTful APIs for product and catalog management with hierarchical categories, product variants, and barcode scanning capabilities. The system should handle complex inventory scenarios including multi-option variants (Size + Color + Material), automatic variant matrix generation, and barcode-based product lookup.

---

## 🎯 Modules & Deliverables

### Module 1: Product Management
**Priority:** HIGH | **Complexity:** Medium | **APIs:** 8  
**Status:** [ ] In Progress

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/products` | GET | Get all products with filters/pagination |
| `/api/products/:id` | GET | Get single product details |
| `/api/products` | POST | Create new product |
| `/api/products/:id` | PUT | Update product |
| `/api/products/:id` | DELETE | Delete product |
| `/api/products/search` | GET | Search products (full-text) |
| `/api/products/bulk-update` | POST | Bulk update products |
| `/api/products/bulk-delete` | POST | Bulk delete products |

**Key Features:**
- ✓ Pagination & filtering
- ✓ Full-text search
- ✓ SKU uniqueness validation
- ✓ Stock level tracking
- ✓ Price history logging
- ✓ Image upload support

**Data Model:**
```
Product {
  id: string
  name: string
  sku: string (unique)
  barcode: string
  category: string (FK)
  price: decimal
  costPrice: decimal
  taxRate: decimal
  stockLevel: integer
  reorderLevel: integer
  unit: string
  description: text
  image: string
  status: 'active' | 'inactive'
  createdAt: timestamp
  updatedAt: timestamp
}
```

---

### Module 2: Category Management
**Priority:** HIGH | **Complexity:** Medium-High | **APIs:** 9  
**Status:** [ ] In Progress

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/categories` | GET | Get all categories |
| `/api/categories/tree` | GET | Get hierarchical tree |
| `/api/categories/:id` | GET | Get category by ID |
| `/api/categories` | POST | Create category |
| `/api/categories/:id` | PUT | Update category |
| `/api/categories/:id` | DELETE | Delete category |
| `/api/categories/move` | POST | Move category in tree |
| `/api/categories/reorder` | POST | Reorder categories |
| `/api/categories/statistics` | GET | Get category stats |

**Key Features:**
- ✓ Hierarchical tree structure (unlimited depth)
- ✓ Parent-child relationships
- ✓ Drag-and-drop reordering
- ✓ Cascade delete protection
- ✓ Product count aggregation
- ✓ SEO-friendly slug generation

**Data Model:**
```
Category {
  id: string
  name: string
  slug: string (unique, SEO-friendly)
  description: text
  parentId: string | null (self-referencing)
  icon: string
  color: string
  image: string
  isActive: boolean
  position: integer
  productCount: integer (cached)
  createdAt: timestamp
}
```

---

### Module 3: Variant Management
**Priority:** MEDIUM | **Complexity:** High | **APIs:** 10  
**Status:** [ ] In Progress

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/variants/options` | GET | Get all variant options |
| `/api/variants/options` | POST | Create variant option |
| `/api/variants/options/:id/values` | GET | Get option values |
| `/api/variants/options/:id/values` | POST | Create option value |
| `/api/products/:productId/variants` | GET | Get product variants |
| `/api/variants/generate-matrix` | POST | Generate variant matrix |
| `/api/variants/:id/inventory` | PUT | Update variant inventory |
| `/api/variants/:id/pricing` | PUT | Update variant pricing |
| `/api/variants/options/:id` | DELETE | Delete variant option |
| `/api/variants/options/:id/values/:valueId` | DELETE | Delete option value |

**Key Features:**
- ✓ Multi-option variants (Size + Color + Material)
- ✓ Auto-generate variant combinations
- ✓ Price/cost adjustments per variant (FIXED or PERCENTAGE)
- ✓ Independent inventory tracking per variant
- ✓ Auto-generated SKU formatting
- ✓ Matrix view for bulk editing

**Data Models:**
```
VariantOption {
  id: string
  name: string (e.g., "Size", "Color", "Material")
  type: 'BUTTON' | 'DROPDOWN' | 'SWATCH'
  position: integer
  isRequired: boolean
}

VariantOptionValue {
  id: string
  variantOptionId: string (FK)
  value: string (e.g., "Small", "Red", "Cotton")
  displayValue: string (e.g., "S", "🔴", "Cotton")
  priceAdjustmentType: 'FIXED' | 'PERCENTAGE'
  priceAdjustmentValue: decimal
  position: integer
}

ProductVariant {
  id: string
  productId: string (FK)
  sku: string (unique, auto-generated)
  combinations: [{optionId, valueId}]
  price: decimal
  cost: decimal
  stockLevel: integer
  isActive: boolean
}
```

---

### Module 4: Barcode Scanner
**Priority:** MEDIUM | **Complexity:** Low | **APIs:** 3  
**Status:** [ ] In Progress

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/barcode/scan` | POST | Process barcode scan |
| `/api/barcode/lookup/:barcode` | GET | Lookup product by barcode |
| `/api/barcode/validate` | POST | Validate barcode format |

**Key Features:**
- ✓ Multiple barcode format support (UPC-A, UPC-E, EAN-13, EAN-8, CODE-128)
- ✓ Checksum validation
- ✓ Product/variant lookup (XOR constraint)
- ✓ Duplicate detection

**Data Model:**
```
BarcodeScanner {
  barcode: string (PK)
  format: 'UPC_A' | 'UPC_E' | 'EAN_13' | 'EAN_8' | 'CODE_128'
  productId: string | null (XOR with variantId)
  productVariantId: string | null (XOR with productId)
  timestamp: timestamp
}
```

---

## 📊 Summary

| Module | APIs | Priority | Complexity | Status |
|--------|------|----------|------------|--------|
| Product Management | 8 | HIGH | Medium | [ ] |
| Category Management | 9 | HIGH | Medium-High | [ ] |
| Variant Management | 10 | MEDIUM | High | [ ] |
| Barcode Scanner | 3 | MEDIUM | Low | [ ] |
| **TOTAL** | **30** | - | - | - |

---

## 🎯 Success Criteria

### Functional Requirements
- [ ] All 30 endpoints implemented and functional
- [ ] Endpoints return correct HTTP status codes
- [ ] Pagination works correctly (limit, offset)
- [ ] Filtering works on all required fields
- [ ] Hierarchical categories maintain integrity
- [ ] Variant matrix generation is accurate
- [ ] Barcode validation is robust (all formats)
- [ ] XOR constraint enforced (Barcode → Product OR Variant)
- [ ] Cascade behaviors work correctly (delete protection)
- [ ] Search results are accurate and ranked

### Performance Requirements
- [ ] List endpoint response time < 200ms
- [ ] Single item endpoint response time < 100ms
- [ ] Bulk operations optimize database queries
- [ ] Image upload handled efficiently

### Code Quality
- [ ] Unit tests with 80%+ coverage
- [ ] Input validation middleware on all endpoints
- [ ] Error handling with meaningful messages
- [ ] Structured logging (request/response)
- [ ] Consistent API response format

### Documentation
- [ ] Swagger/OpenAPI specification complete
- [ ] Postman test collection created
- [ ] README with setup instructions
- [ ] Code comments on complex logic
- [ ] API endpoint documentation

---

## 🛠 Technology Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.4.12
- **Database:** PostgreSQL 12+
- **ORM:** Hibernate/JPA 6.x
- **Build Tool:** Maven 3.9.11
- **Architecture:** 3-Layer (Controller → Service → Repository)

---

## 📁 Project Structure

```
backend-api/
├── src/main/java/com/inventory/backend_api/
│   ├── controller/              ← REST endpoints (30 APIs)
│   │   ├── ProductController
│   │   ├── CategoryController
│   │   ├── VariantController
│   │   └── BarcodeController
│   ├── service/                 ← Business logic
│   ├── repository/              ← Database access
│   ├── entity/                  ← JPA entities
│   └── dto/                     ← Request/Response models
├── src/test/java/               ← Unit tests
├── pom.xml                      ← Dependencies
└── Schema.sql                   ← Database schema (7 tables)
```

---

## ✅ Checklist

### Module 1: Product Management
- [ ] Implement GET /api/products
- [ ] Implement GET /api/products/:id
- [ ] Implement POST /api/products
- [ ] Implement PUT /api/products/:id
- [ ] Implement DELETE /api/products/:id
- [ ] Implement GET /api/products/search
- [ ] Implement POST /api/products/bulk-update
- [ ] Implement POST /api/products/bulk-delete
- [ ] Write unit tests (80%+ coverage)

### Module 2: Category Management
- [ ] Implement GET /api/categories
- [ ] Implement GET /api/categories/tree
- [ ] Implement GET /api/categories/:id
- [ ] Implement POST /api/categories
- [ ] Implement PUT /api/categories/:id
- [ ] Implement DELETE /api/categories/:id
- [ ] Implement POST /api/categories/move
- [ ] Implement POST /api/categories/reorder
- [ ] Implement GET /api/categories/statistics
- [ ] Write unit tests (80%+ coverage)

### Module 3: Variant Management
- [ ] Implement GET /api/variants/options
- [ ] Implement POST /api/variants/options
- [ ] Implement GET /api/variants/options/:id/values
- [ ] Implement POST /api/variants/options/:id/values
- [ ] Implement GET /api/products/:productId/variants
- [ ] Implement POST /api/variants/generate-matrix
- [ ] Implement PUT /api/variants/:id/inventory
- [ ] Implement PUT /api/variants/:id/pricing
- [ ] Implement DELETE /api/variants/options/:id
- [ ] Implement DELETE /api/variants/options/:id/values/:valueId
- [ ] Write unit tests (80%+ coverage)

### Module 4: Barcode Scanner
- [ ] Implement POST /api/barcode/scan
- [ ] Implement GET /api/barcode/lookup/:barcode
- [ ] Implement POST /api/barcode/validate
- [ ] Write unit tests (80%+ coverage)

### Documentation & Testing
- [ ] Create Swagger/OpenAPI spec
- [ ] Create Postman test collection
- [ ] Write API documentation
- [ ] Add code comments
- [ ] Verify performance benchmarks
- [ ] Final code review

---

## 📝 Notes

- Database schema already created in [Schema.sql](Schema.sql)
- 7 tables with 3NF normalization
- Self-referencing categories for unlimited depth
- XOR constraint on barcodes (Product OR Variant)
- Cascade behaviors configured in schema
- All relationships defined with foreign keys

---

**Last Updated:** January 16, 2026  
**Project Repository:** [Backend API](backend-api/)
