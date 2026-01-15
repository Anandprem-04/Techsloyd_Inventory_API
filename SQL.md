# Database Schema Documentation

**Techsloyd Inventory Management System**

---

## 📊 Overview

- **Database:** `api`
- **DBMS:** PostgreSQL 12+
- **Tables:** 7 (3NF Normalized)
- **Purpose:** Retail/E-commerce inventory with hierarchical categories, product variants, and barcode scanning

---

## 🗂️ Tabular Entity-Relationship Diagram

### Complete ER Table Structure

| Table | PK | FK | Relationships | Cascade |
|---|---|---|---|---|
| **CATEGORIES** | id | parent_id → CATEGORIES.id (self) | 1:N with PRODUCTS | SET NULL |
| **PRODUCTS** | id | category_id → CATEGORIES.id | 1:N with PRODUCT_VARIANTS<br>1:N with BARCODES | RESTRICT<br>CASCADE<br>CASCADE |
| **VARIANT_OPTIONS** | id | - | 1:N with VARIANT_OPTION_VALUES | CASCADE |
| **VARIANT_OPTION_VALUES** | id | variant_option_id → VARIANT_OPTIONS.id | 1:N with VARIANT_COMBINATIONS | CASCADE |
| **PRODUCT_VARIANTS** | id | product_id → PRODUCTS.id | 1:N with VARIANT_COMBINATIONS<br>1:N with BARCODES | CASCADE<br>CASCADE |
| **VARIANT_COMBINATIONS** | id | product_variant_id → PRODUCT_VARIANTS.id<br>variant_option_value_id → VARIANT_OPTION_VALUES.id | M:N Bridge Table | CASCADE<br>RESTRICT |
| **BARCODES** | barcode | product_id → PRODUCTS.id (XOR)<br>product_variant_id → PRODUCT_VARIANTS.id | Links to either Product or Variant | CASCADE |

---

---

## 🔗 Relationship Mechanics

### How Categories & Products Connect

**Categories Table Structure:**
- Self-referencing: Each category can have ONE parent (parent_id) and ZERO to MANY children
- Unlimited nesting: No depth limit on hierarchy
- Root categories: Have parent_id = NULL

**Cascade Behavior:**
```
Delete parent category → child.parent_id becomes NULL (SET NULL)
Result: Child becomes root category instead of being deleted
Purpose: Preserve data when reorganizing hierarchy
```

**Products Link to Categories:**
```
One Category → Many Products (1:N)
Products.category_id → Categories.id [RESTRICT]

Constraint: Cannot delete category if it has products
Purpose: Maintain data integrity (prevent orphaned products)
```

---

### How Products & Variants Connect

**Products are Base Definitions:**
- Store global attributes: name, base price, cost, tax_rate, stock_level
- All variants inherit these values unless overridden
- Each product can have ZERO or MANY variants

**Variants are Saleable Items:**
```
One Product → Many Variants (1:N)
ProductVariants.product_id → Products.id [CASCADE]

Cascade Behavior: Delete product → ALL variants auto-deleted
Purpose: Maintain consistency (no orphaned variants)
```

**Independent Variant Properties:**
```
Product: T-Shirt ($20.00 base)
├─ Variant 1: TSHIRT-RED-S   ($21.00 - different price)
├─ Variant 2: TSHIRT-RED-M   ($22.00 - different price)
└─ Variant 3: TSHIRT-BLUE-L  ($25.00 - different price)

Each variant can override:
- price (different from base)
- cost_price
- stock_level (tracked per variant, not aggregated)
```

---

### How Variant Options & Values Connect

**Options are Global Templates:**
```
One Option → Many Values (1:N)
VariantOptionValues.variant_option_id → VariantOptions.id [CASCADE]

Example: One "Size" option has multiple values:
├─ Small (price_adj: $0 FIXED)
├─ Medium (price_adj: +$2 FIXED)
└─ Large (price_adj: +$5 FIXED)

Cascade Behavior: Delete option → DELETE all its values
Purpose: Option cannot exist without values
```

**Values Define Price Adjustments:**
```
Two Adjustment Types:

FIXED: Absolute amount added to base price
  Base: $20.00 + Small: $0 = $20.00
  Base: $20.00 + Large: +$5 = $25.00

PERCENTAGE: Relative percentage of base price
  Base: $20.00 × (1 + 0%) = $20.00
  Base: $20.00 × (1 + 10%) = $22.00
```

---

### How Variants Use Option Values (M:N Bridge)

**Variant Combinations Bridge Table:**
```
One Variant → Many Combinations (1:N)
VariantCombinations.product_variant_id → ProductVariants.id [CASCADE]

One Value → Many Combinations (1:N)
VariantCombinations.variant_option_value_id → VariantOptionValues.id [RESTRICT]

Result: M:N relationship (Variants ←→ Values via Combinations)
```

**How Combinations Define Variants:**
```
Variant: TSHIRT-RED-M requires:
├─ One Color value: Red (from Color option)
└─ One Size value: Medium (from Size option)

Stored as combinations:
Combination 1: TSHIRT-RED-M → Red
Combination 2: TSHIRT-RED-M → Medium

Price Calculation:
Base Product Price: $20.00
+ Size Adjustment (Medium): +$2.00
+ Color Adjustment (Red): $0.00
= Final Variant Price: $22.00
```

**RESTRICT on Values:**
```
Cannot delete a value if it's used in any combination
Cascade on Variant: Delete variant → delete its combinations
Purpose: Prevent orphaned option values in combinations
```

---

### How Barcodes Connect to Products

**XOR Constraint (Exclusive OR):**
```
Each barcode MUST satisfy ONE of these:
1. product_id IS NOT NULL AND product_variant_id IS NULL
   └─ Simple product (no variants)

2. product_id IS NULL AND product_variant_id IS NOT NULL
   └─ Variant-specific product

INVALID states (prevented):
❌ Both product_id and product_variant_id filled
❌ Both product_id and product_variant_id NULL
```

**Barcode Linking Mechanisms:**

For Simple Products (no variants):
```
Barcode → Product
  8901234567890 → product-001
  └─ One barcode can link to only ONE product
  └─ Product can have multiple barcodes (different formats)
```

For Variant Products:
```
Barcode → Product Variant (not the product itself)
  8901234567890 → variant-001 (TSHIRT-RED-M)
  8901234567891 → variant-002 (TSHIRT-BLUE-L)
  
Scanning barcode retrieves:
  Product info (from variant.product_id)
  Variant-specific price, stock, attributes
```

**Cascade Behavior:**
```
Delete Product → CASCADE delete all its barcodes
Delete Variant → CASCADE delete all its barcodes

Purpose: Barcodes always point to valid entities
```

---

## 🎯 Table Details & Specifications

### TABLE 1: CATEGORIES (Hierarchical Tree)
**Purpose:** Store product categories in a hierarchical tree structure with unlimited nesting

| Column | Type | Constraints | Description |
|---|---|---|---|
| id | VARCHAR(50) | PK | Unique category identifier |
| parent_id | VARCHAR(50) | FK (self) | References parent category (NULL = root) |
| name | VARCHAR(255) | NOT NULL | Category display name |
| slug | VARCHAR(255) | UNIQUE | SEO-friendly URL identifier |
| description | TEXT | - | Category description |
| icon | VARCHAR(255) | - | Icon file reference |
| color | VARCHAR(50) | - | Visual color code |
| image | VARCHAR(255) | - | Image file reference |
| is_active | BOOLEAN | DEFAULT TRUE | Active/inactive toggle |
| position | INTEGER | DEFAULT 0 | Manual ordering within parent |
| product_count | INTEGER | DEFAULT 0 | Cached count of direct products |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |

**Key Mechanism:** Self-referencing through `parent_id` allows unlimited hierarchy depth. When a category is deleted, its children's `parent_id` becomes NULL (ON DELETE SET NULL), converting them to root categories.

---

### TABLE 2: PRODUCTS
**Purpose:** Base product information that variants inherit from

| Column | Type | Constraints | Description |
|---|---|---|---|
| id | VARCHAR(50) | PK | Unique product identifier |
| category_id | VARCHAR(50) | FK → CATEGORIES | Link to parent category |
| name | VARCHAR(255) | NOT NULL | Product display name |
| sku | VARCHAR(100) | UNIQUE | Stock Keeping Unit (global) |
| description | TEXT | - | Product description |
| image | VARCHAR(255) | - | Product image reference |
| unit | VARCHAR(50) | - | Unit of measure (e.g., "piece", "kg") |
| price | DECIMAL(10,2) | NOT NULL | Base selling price |
| cost_price | DECIMAL(10,2) | - | Cost/wholesale price |
| tax_rate | DECIMAL(5,2) | DEFAULT 0.0 | Tax percentage (0-100%) |
| stock_level | INTEGER | DEFAULT 0 | Aggregate stock across variants |
| reorder_level | INTEGER | DEFAULT 10 | Threshold for reorder alerts |
| status | VARCHAR(20) | CHECK constraint | 'active' or 'inactive' |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | - | Last modification timestamp |

**Key Mechanism:** ON DELETE RESTRICT on `category_id` prevents category deletion if products exist. All variants inherit base price/cost/tax unless overridden. Status field controls product visibility.

---

### TABLE 3: VARIANT_OPTIONS
**Purpose:** Define reusable option types across all products (not product-specific)

| Column | Type | Constraints | Description |
|---|---|---|---|
| id | VARCHAR(50) | PK | Unique option identifier |
| name | VARCHAR(100) | NOT NULL | Option type name (e.g., "Size", "Color") |
| position | INTEGER | DEFAULT 0 | Display order |
| type | VARCHAR(20) | CHECK constraint | UI type: BUTTON, DROPDOWN, SWATCH |

**Key Mechanism:** Global definitions - one "Size" option is reused across all products. Type determines how options are displayed in UI (radio buttons, dropdown, or color swatches). One option → Many values (1:N).

---

### TABLE 4: VARIANT_OPTION_VALUES
**Purpose:** Specific values for each option with price adjustments

| Column | Type | Constraints | Description |
|---|---|---|---|
| id | VARCHAR(50) | PK | Unique value identifier |
| variant_option_id | VARCHAR(50) | FK → VARIANT_OPTIONS | Parent option |
| value | VARCHAR(100) | NOT NULL | The actual value (e.g., "Small", "Red") |
| display_value | VARCHAR(100) | - | Custom display (e.g., "S", "🔴") |
| price_adjustment_type | VARCHAR(20) | CHECK constraint | FIXED or PERCENTAGE |
| price_adjustment_value | DECIMAL(10,2) | DEFAULT 0.0 | Adjustment amount |
| position | INTEGER | DEFAULT 0 | Display order within option |

**Key Mechanism:** ON DELETE CASCADE with option (delete option → delete all values). Price adjustments are applied to base price:
- **FIXED:** Base $20 + $5 = $25
- **PERCENTAGE:** Base $20 × 1.10 = $22

---

### TABLE 5: PRODUCT_VARIANTS
**Purpose:** Individual SKUs with specific attribute combinations (the actual saleable items)

| Column | Type | Constraints | Description |
|---|---|---|---|
| id | VARCHAR(50) | PK | Unique variant identifier |
| product_id | VARCHAR(50) | FK → PRODUCTS | Parent product |
| sku | VARCHAR(100) | UNIQUE | Variant-specific SKU (e.g., "TSHIRT-RED-M") |
| price | DECIMAL(10,2) | NOT NULL | Variant-specific price (overrides base) |
| cost_price | DECIMAL(10,2) | - | Variant-specific cost |
| stock_level | INTEGER | DEFAULT 0 | Inventory for THIS variant only |
| is_active | BOOLEAN | DEFAULT TRUE | Availability toggle |

**Key Mechanism:** ON DELETE CASCADE with product (delete product → delete all variants). Each variant is independent - different SKUs can have different prices and stock levels. Combinations table defines which options/values apply to each variant.

---

### TABLE 6: VARIANT_COMBINATIONS (Bridge Table)
**Purpose:** M:N relationship linking variants to their defining option values

| Column | Type | Constraints | Description |
|---|---|---|---|
| id | VARCHAR(50) | PK | Unique combination identifier |
| product_variant_id | VARCHAR(50) | FK → PRODUCT_VARIANTS | Which variant |
| variant_option_value_id | VARCHAR(50) | FK → VARIANT_OPTION_VALUES | Which option value |

**Key Mechanism:** M:N Bridge table - ONE variant links to MANY values (Size: M, Color: Red). 
- ON DELETE CASCADE on variant_id: Delete variant → delete its combinations
- ON DELETE RESTRICT on value_id: Cannot delete value if in use (prevents orphaned data)

Example:
```
Variant: TSHIRT-RED-M (var-001)
├─ Combination 1: var-001 → Size/Medium
└─ Combination 2: var-001 → Color/Red
```

---

### TABLE 7: BARCODES
**Purpose:** Central registry mapping barcodes to products OR variants for scanning

| Column | Type | Constraints | Description |
|---|---|---|---|
| barcode | VARCHAR(100) | PK | Barcode value (12-128 chars depending on format) |
| format | VARCHAR(20) | CHECK constraint | Format type: UPC_A, UPC_E, EAN_13, EAN_8, CODE_128 |
| product_id | VARCHAR(50) | FK → PRODUCTS (nullable) | Simple products (no variants) |
| product_variant_id | VARCHAR(50) | FK → PRODUCT_VARIANTS (nullable) | Variant-specific products |
| created_at | TIMESTAMP | DEFAULT NOW() | Barcode assignment timestamp |

**Key Mechanism:** 
- **XOR Constraint:** Must link to product_id OR product_variant_id (never both, never neither)
  - Simple product → barcode links to product
  - Variant product → barcode links to specific variant
- **Format Validation:** Only accepts 5 standard barcode formats (UPC-A 12 digits, EAN-13 13 digits, etc.)
- **CASCADE deletion:** Delete product/variant → automatically delete associated barcode

---

## � Workflow Visualizations

### Workflow 1: Creating a Product with Variants

```
Step-by-Step Process:

1. CREATE CATEGORY
   └─ Electronics (cat-001)

2. CREATE BASE PRODUCT
   └─ T-Shirt (prod-001)
      ├─ Base Price: $20.00
      ├─ Cost Price: $8.00
      └─ Category: Electronics

3. CREATE VARIANT OPTIONS
   ├─ Size (opt-001)
   │   ├─ Small ($0)
   │   ├─ Medium ($2)
   │   └─ Large ($5)
   └─ Color (opt-002)
       ├─ Red ($0)
       ├─ Blue ($0)
       └─ Gold (+10%)

4. CREATE PRODUCT VARIANT
   └─ TSHIRT-RED-M (var-001)
      ├─ Product: T-Shirt
      ├─ Price: $22.00 (20 + 2)
      └─ Stock: 50 units

5. LINK VARIANT COMBINATIONS
   └─ Variant TSHIRT-RED-M has:
      ├─ Red (from Color option)
      └─ Medium (from Size option)

6. ASSIGN BARCODE
   └─ 8902190410203 → TSHIRT-RED-M

Result: Complete product ready for POS
```

---

### Workflow 2: POS Barcode Scanning

```
Customer scans barcode at checkout:

┌─────────────────────────────────┐
│ INPUT: Barcode "8902190410203"  │
└────────────┬────────────────────┘
             ↓
        ┌─────────────────────┐
        │  LOOKUP BARCODE     │  Query: SELECT * FROM barcodes
        │ in BARCODES table   │         WHERE barcode = '8902190410203'
        └────────────┬────────┘
                     ↓
        ┌─────────────────────┐
        │ VALIDATE XOR        │  Check: product_id OR variant_id
        │ constraint          │         (not both, not neither)
        └────────────┬────────┘
                     ↓
        ┌─────────────────────┐
        │ FETCH VARIANT DETAILS
        │ from PRODUCT_VARIANTS│  Found: TSHIRT-RED-M
        └────────────┬────────┘  Price: $22.00, Stock: 50
                     ↓
        ┌─────────────────────┐
        │ GET VARIANT OPTIONS │  Join through:
        │ (Size, Color, etc)  │  - VARIANT_COMBINATIONS
        └────────────┬────────┘  - VARIANT_OPTION_VALUES
                     ↓            - VARIANT_OPTIONS
        ┌─────────────────────┐
        │ DISPLAY ON POS      │  T-Shirt (Red, Medium)
        │                     │  Price: $22.00
        └─────────────────────┘  Stock: 50 ✓
```

---

### Workflow 3: Category Hierarchy Query

```
Query: Find all products under "Electronics" (including sub-categories)

Database Tree Structure:
┌─────────────────┐
│  Electronics    │ (cat-001)
└────────┬────────┘
         │
    ┌────┴──────┬──────────┐
    ↓           ↓          ↓
  Phones   Computers    Cameras
  (cat-002) (cat-003)   (cat-004)
    │
    ↓
 Smartphones
 (cat-005)

SQL Execution:
1. Start with "Electronics" (cat-001)
2. Recursively find all children
   └─ Phones (cat-002)
      └─ Smartphones (cat-005)
   └─ Computers (cat-003)
   └─ Cameras (cat-004)
3. Get all products linked to any category in the tree
4. Return full product list across hierarchy

Result: All products from cat-001, cat-002, cat-003, cat-004, cat-005
```

---

## 📊 Data Integrity Rules

### Constraints & Validations

| Table | Constraint | Rule | Purpose |
|---|---|---|---|
| **PRODUCTS** | RESTRICT on category_id | Cannot delete category with products | Protect data structure |
| **PRODUCT_VARIANTS** | CASCADE on product_id | Auto-delete variants if product deleted | Maintain consistency |
| **VARIANT_COMBINATIONS** | CASCADE on variant_id | Auto-delete combinations if variant deleted | Maintain consistency |
| **VARIANT_COMBINATIONS** | RESTRICT on value_id | Cannot delete value if in use | Prevent orphaned data |
| **BARCODES** | XOR constraint | Must link to product OR variant (not both) | Ensure valid state |
| **CATEGORIES** | SET NULL on parent_id | Orphaned categories become roots | Allow tree restructuring |

---

## � Validation Rules

**Check Constraints Applied:**
- Product prices: >= 0
- Variant stock levels: >= 0  
- Tax rates: 0-100%
- Product cost < Product price
- Product status: 'active' | 'inactive'
- Barcode format: UPC_A | UPC_E | EAN_13 | EAN_8 | CODE_128
- Barcode XOR: (product_id IS NOT NULL) XOR (variant_id IS NOT NULL)

**Unique Constraints Applied:**
- Categories.slug: SEO-friendly URL must be globally unique
- Products.sku: Stock Keeping Unit must be globally unique
- ProductVariants.sku: Each variant SKU must be globally unique
- Barcodes.barcode: Barcode value must be globally unique

---

## 🎯 Data Integrity

### Check Constraints
- Prices ≥ 0
- Stock ≥ 0
- Tax rate: 0-100%
- Cost < Price
- Status: active | inactive
- Format: UPC_A | UPC_E | EAN_13 | EAN_8 | CODE_128
- XOR: (product_id XOR product_variant_id)

### Unique Constraints
- Category slug (SEO URLs)
- Product SKU (global)
- Variant SKU (global)
- Barcode value (global)

---

## 📊 Useful Queries

### 1. Category Tree (Hierarchical)
```sql
WITH RECURSIVE tree AS (
    SELECT id, parent_id, name, 0 as level
    FROM categories WHERE parent_id IS NULL
    UNION ALL
    SELECT c.id, c.parent_id, c.name, t.level + 1
    FROM categories c JOIN tree t ON c.parent_id = t.id
)
SELECT * FROM tree ORDER BY level, name;
```

### 2. Product with All Variants
```sql
SELECT p.name, pv.sku, pv.price, pv.stock_level
FROM products p
LEFT JOIN product_variants pv ON p.id = pv.product_id
WHERE p.id = 'prod-001';
```

### 3. Variant Attributes (Decoding Combinations)
```sql
SELECT pv.sku, opt.name as option_name, val.value
FROM product_variants pv
JOIN variant_combinations vc ON pv.id = vc.product_variant_id
JOIN variant_option_values val ON vc.variant_option_value_id = val.id
JOIN variant_options opt ON val.variant_option_id = opt.id
WHERE pv.id = 'var-001';
```

### 4. Stock Alert (Reorder Needed)
```sql
SELECT name, sku, stock_level, reorder_level,
    CASE 
        WHEN stock_level <= reorder_level THEN 'REORDER NOW'
        ELSE 'SUFFICIENT'
    END as status
FROM products
WHERE stock_level <= reorder_level;
```

### 5. Search by Name or SKU
```sql
SELECT id, name, sku, price, stock_level
FROM products
WHERE LOWER(name) LIKE LOWER('%wireless%') 
   OR LOWER(sku) LIKE LOWER('%wireless%');
```

---

## 🔧 Performance Optimization

### Indexes
The following indexes are defined in [Schema.sql](Schema.sql):
- Categories: parent_id, slug
- Products: category_id, sku  
- Product Variants: product_id
- Variant Combinations: product_variant_id
- Barcodes: product_id, product_variant_id

### Query Optimization Tips
1. **Filter by indexed columns:** Always use id, sku, or slug in WHERE clauses
2. **Use JOINs:** Fetch related data in single query instead of multiple queries
3. **Pagination:** Use LIMIT/OFFSET for large result sets
4. **Cache static data:** Options and values rarely change, consider caching
5. **Regular maintenance:** Run ANALYZE and VACUUM periodically

---

## 📝 Database Design

### 3NF Normalization

| Level | Rule | Our Implementation |
|---|---|---|
| **1NF** | Atomic values only | No repeating groups, each cell has single value |
| **2NF** | Remove partial dependencies | Bridge table for M:N relationships |
| **3NF** | Remove transitive dependencies | Option values separate from options |

**Benefits:**
- ✅ No data redundancy
- ✅ Data integrity maintained  
- ✅ Easy to update/maintain
- ✅ Flexible querying
- ✅ Scalable design

---

## 🔐 Database Maintenance

### Backup
```bash
pg_dump -U postgres api > backup_$(date +%Y%m%d).sql
```

### Restore  
```bash
psql -U postgres api < backup_20260116.sql
```

### Optimize
```sql
ANALYZE;    -- Update table statistics
VACUUM;     -- Reclaim space
```

---

## 📌 Schema Information

- **Version:** 1.0
- **Updated:** January 16, 2026
- **DBMS:** PostgreSQL 12+
- **Status:** Production Ready
- **Encoding:** UTF-8

---

**For SQL code with comments, see [Schema.sql](Schema.sql)**

---

## 🔗 Cascade Behaviors

| Scenario | Behavior | Impact |
|----------|----------|--------|
| Delete Category → | Products ON DELETE RESTRICT | Cannot delete (products exist) |
| Delete Product → | Variants ON DELETE CASCADE | All variants also deleted |
| Delete Product → | Barcodes ON DELETE CASCADE | All barcodes also deleted |
| Delete Variant → | Combinations ON DELETE CASCADE | All combinations also deleted |
| Delete Variant → | Barcodes ON DELETE CASCADE | All barcode links deleted |
| Delete VariantOption → | Values ON DELETE CASCADE | All option values deleted |
| Delete Value → | Combinations ON DELETE RESTRICT | Cannot delete (in use) |

---

## � Key Workflows

### 1. Get Product with All Variants
```sql
SELECT p.id, p.name, p.sku, pv.id as variant_id, pv.sku as variant_sku, pv.price
FROM products p
LEFT JOIN product_variants pv ON p.id = pv.product_id
WHERE p.id = 'prod-001'
ORDER BY pv.sku;
```

### 2. Get Variant Details with All Options
```sql
SELECT 
    pv.id as variant_id,
    pv.sku,
    pv.price,
    pv.stock_level,
    opt.name as option_name,
    ov.value as option_value
FROM product_variants pv
JOIN variant_combinations vc ON pv.id = vc.product_variant_id
JOIN variant_option_values ov ON vc.variant_option_value_id = ov.id
JOIN variant_options opt ON ov.variant_option_id = opt.id
WHERE pv.id = 'var-001'
ORDER BY opt.position, ov.position;
```

### 3. Get Complete Category Tree
```sql
WITH RECURSIVE category_tree AS (
    SELECT id, parent_id, name, slug, 0 as level
    FROM categories
    WHERE parent_id IS NULL
    
    UNION ALL
    
    SELECT c.id, c.parent_id, c.name, c.slug, ct.level + 1
    FROM categories c
    JOIN category_tree ct ON c.parent_id = ct.id
)
SELECT 
    REPEAT('  ', level) || name as category_name,
    slug,
    level
FROM category_tree
ORDER BY level, slug;
```

### 4. Find All Products by Category (Including Children)
```sql
WITH category_tree AS (
    SELECT id FROM categories WHERE id = 'cat-001'
    
    UNION ALL
    
    SELECT c.id FROM categories c
    JOIN category_tree ct ON c.parent_id = ct.id
)
SELECT p.id, p.name, p.sku, COUNT(pv.id) as variant_count
FROM products p
JOIN category_tree ct ON p.category_id = ct.id
LEFT JOIN product_variants pv ON p.id = pv.product_id
GROUP BY p.id, p.name, p.sku;
```

### 5. Search Products by Name or SKU
```sql
SELECT id, name, sku, price, stock_level, status
FROM products
WHERE 
    LOWER(name) LIKE LOWER('%wireless%') 
    OR LOWER(sku) LIKE LOWER('%wireless%')
ORDER BY name;
```

---

## Query Optimization Tips
1. **Use indexes:** Always filter by indexed columns (id, sku, slug)
2. **Avoid SELECT *:** Only fetch needed columns
3. **Use JOINs instead of loops:** Retrieve related data in single query
4. **Limit results:** Use pagination (LIMIT + OFFSET)
5. **Cache static data:** Option types and values rarely change

---

## 🔄 Database Maintenance

### Backup
```bash
# Full database backup
pg_dump -U postgres api > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore
psql -U postgres api < backup_20240115_143022.sql
```

### Analyze & Vacuum
```sql
-- Optimize table statistics (run periodically)
ANALYZE;

-- Free up space (run during maintenance window)
VACUUM;

-- Full vacuum (locks tables)
VACUUM FULL;
```

---

## 📝 Normalization (3NF)

### Why 3NF?

The database follows **Third Normal Form (3NF)** principles:

1. **1NF - Atomic Values:**
   - All columns contain atomic (non-divisible) values
   - No repeating groups
   - Example: `option_values` table instead of comma-separated string

2. **2NF - Remove Partial Dependencies:**
   - All non-key attributes depend on the entire primary key
   - Example: `variant_combinations` bridge table links variants to values

3. **3NF - Remove Transitive Dependencies:**
   - Non-key attributes depend only on primary key, not other non-key attributes
   - Example: `product_count` in categories is denormalized cache (acceptable)

### Benefits
- ✅ Data integrity (no anomalies)
- ✅ Efficient storage (no redundancy)
- ✅ Easy updates (minimal cascades)
- ✅ Flexible queries (proper relationships)
- ✅ Scalability (normalized structure)

---

## � Schema Version

- **Version:** 1.0
- **Last Updated:** January 16, 2026
- **PostgreSQL:** 12+
- **Status:** Production Ready
- **Encoding:** UTF-8

---

**End of Database Schema Documentation**
�