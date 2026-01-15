# Database Schema Documentation

**Techsloyd Inventory Management System**

---

## 📊 Overview

- **Database:** `api`
- **DBMS:** PostgreSQL 12+
- **Tables:** 7 (3NF Normalized)
- **Purpose:** Retail/E-commerce inventory with hierarchical categories, product variants, and barcode scanning

### Architecture Flow
```
                    ┌──────────────────────────────────────────────────┐
                    │         CATEGORIES (Hierarchical Tree)           │
                    │  PK: id                                          │
                    │  FK: parent_id → CATEGORIES.id (Self-reference)  │
                    └────────────┬─────────────────────────────────────┘
                                 │ 1:N (ON DELETE RESTRICT)
                                 ↓
                    ┌──────────────────────────────────────────────────┐
                    │              PRODUCTS (Base Info)                │
                    │  PK: id                                          │
                    │  FK: category_id → CATEGORIES.id                 │
                    │  Fields: name, sku, price, stock_level           │
                    └────────┬──────────────────────────┬───────────────┘
                             │                          │
                             │ 1:N                      │ 1:N
                             │ (CASCADE)                │ (CASCADE)
                             ↓                          ↓
        ┌────────────────────────────────┐   ┌─────────────────────────────┐
        │    PRODUCT_VARIANTS (SKUs)     │   │   BARCODES (Scanner IDs)    │
        │  PK: id                        │   │  PK: barcode                │
        │  FK: product_id → PRODUCTS.id  │   │  FK: product_id ──────────┐ │
        │  Fields: sku, price, stock     │   │  FK: variant_id ────────┐ │ │
        └──────────┬─────────────────────┘   │  XOR: One FK must exist  │ │ │
                   │                          └──────────┬───────────────┼─┘ │
                   │ 1:N (CASCADE)                       │               │   │
                   ↓                                     │               │   │
        ┌────────────────────────────────┐              │ 1:N           └───┼─┐
        │  VARIANT_COMBINATIONS          │              │ (CASCADE)         │ │
        │  PK: id                        │              ↓                   │ │
        │  FK: product_variant_id ───────┼──────────────┘                   │ │
        │  FK: variant_option_value_id   │◄─────────────────────────────────┘ │
        └──────────┬─────────────────────┘                                    │
                   │ M:N Bridge Table                                         │
                   │ N:1 (RESTRICT)                                           │
                   ↓                                                          │
        ┌────────────────────────────────┐                                    │
        │  VARIANT_OPTION_VALUES         │                                    │
        │  PK: id                        │                                    │
        │  FK: variant_option_id         │                                    │
        │  Fields: value, price_adj      │                                    │
        └──────────┬─────────────────────┘                                    │
                   │ N:1 (CASCADE)                                            │
                   ↓                                                          │
        ┌────────────────────────────────┐                                    │
        │    VARIANT_OPTIONS             │                                    │
        │  PK: id                        │                                    │
        │  Fields: name, type, position  │                                    │
        └────────────────────────────────┘                                    │
                                                                               │
        Legend:                                                                │
        ────→  Foreign Key Relationship                                       │
        1:N    One-to-Many                                                    │
        M:N    Many-to-Many (Bridge Table)                                    │
        XOR    Exclusive OR Constraint                                        │
        CASCADE  Child deleted with parent                                    │
        RESTRICT Cannot delete if children exist                              │
```

---

## 🗂 Complete Entity-Relationship Diagram

### Detailed Table Relationships

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          DATABASE: api (PostgreSQL)                         │
│                              7 Tables - 3NF Normalized                      │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│  TABLE 1: CATEGORIES                                    [Self-Referencing]   │
├──────────────────────────────────────────────────────────────────────────────┤
│  PK: id (VARCHAR)                                                            │
│  FK: parent_id → CATEGORIES.id (ON DELETE SET NULL)                          │
│  Fields: name, slug, description, icon, color, image, is_active, position   │
│                                                                              │
│  Relationship:                                                               │
│    • Self → Self (parent_id)         [0..1 : 0..*]                          │
│    • Self → PRODUCTS (category_id)   [1 : 0..*]        ON DELETE RESTRICT   │
└──────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1:N
                                    ↓
┌──────────────────────────────────────────────────────────────────────────────┐
│  TABLE 2: PRODUCTS                                          [Base Product]   │
├──────────────────────────────────────────────────────────────────────────────┤
│  PK: id (VARCHAR)                                                            │
│  FK: category_id → CATEGORIES.id (ON DELETE RESTRICT)                        │
│  Fields: name, sku, price, cost_price, tax_rate, stock_level, status        │
│                                                                              │
│  Relationships:                                                              │
│    • CATEGORIES → Self (category_id)     [1 : 0..*]                          │
│    • Self → PRODUCT_VARIANTS (product_id) [1 : 0..*]    ON DELETE CASCADE   │
│    • Self → BARCODES (product_id)        [1 : 0..*]    ON DELETE CASCADE   │
└──────────────────────────────────────────────────────────────────────────────┘
                        │                               │
                        │ 1:N                           │ 1:N
                        ↓                               ↓
┌─────────────────────────────────────────┐  ┌────────────────────────────────┐
│  TABLE 5: PRODUCT_VARIANTS    [SKUs]    │  │  TABLE 7: BARCODES             │
├─────────────────────────────────────────┤  ├────────────────────────────────┤
│  PK: id (VARCHAR)                       │  │  PK: barcode (VARCHAR)         │
│  FK: product_id → PRODUCTS.id (CASCADE) │  │  FK: product_id (CASCADE)      │
│  Fields: sku, price, stock_level        │  │  FK: product_variant_id        │
│                                         │  │      (CASCADE)                 │
│  Relationship:                          │  │  Fields: format                │
│    • PRODUCTS → Self (product_id)       │  │                                │
│         [1 : 0..*]                      │  │  XOR Constraint:               │
│    • Self → BARCODES (variant_id)       │  │    (product_id IS NOT NULL     │
│         [1 : 0..*]   ON DELETE CASCADE  │  │     AND variant_id IS NULL)    │
│    • Self → VARIANT_COMBINATIONS        │  │    OR                          │
│         (product_variant_id) [1 : 1..*] │  │    (product_id IS NULL         │
│         ON DELETE CASCADE               │  │     AND variant_id IS NOT NULL)│
└─────────────┬───────────────────────────┘  └────────────────────────────────┘
              │ 1:N                                       ↑
              │                                           │ 1:N
              ↓                                           │
┌─────────────────────────────────────────┐              │
│  TABLE 6: VARIANT_COMBINATIONS          │              │
│              [M:N Bridge]               │              │
├─────────────────────────────────────────┤              │
│  PK: id (VARCHAR)                       │              │
│  FK: product_variant_id                 │──────────────┘
│      → PRODUCT_VARIANTS.id (CASCADE)    │
│  FK: variant_option_value_id            │
│      → VARIANT_OPTION_VALUES.id         │
│      (RESTRICT)                         │
└─────────────┬───────────────────────────┘
              │ N:1
              ↓
┌─────────────────────────────────────────┐
│  TABLE 4: VARIANT_OPTION_VALUES         │
│              [Option Values]            │
├─────────────────────────────────────────┤
│  PK: id (VARCHAR)                       │
│  FK: variant_option_id                  │
│      → VARIANT_OPTIONS.id (CASCADE)     │
│  Fields: value, position,               │
│          price_adjustment_type,         │
│          price_adjustment_value         │
│                                         │
│  Relationships:                         │
│    • VARIANT_OPTIONS → Self             │
│         [1 : 1..*]                      │
│    • Self → VARIANT_COMBINATIONS        │
│         [1 : 0..*]   ON DELETE RESTRICT │
└─────────────┬───────────────────────────┘
              │ N:1
              ↓
┌─────────────────────────────────────────┐
│  TABLE 3: VARIANT_OPTIONS               │
│              [Option Types]             │
├─────────────────────────────────────────┤
│  PK: id (VARCHAR)                       │
│  Fields: name, position, type           │
│          (BUTTON|DROPDOWN|SWATCH)       │
│                                         │
│  Relationship:                          │
│    • Self → VARIANT_OPTION_VALUES       │
│         (variant_option_id) [1 : 1..*]  │
│         ON DELETE CASCADE               │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│  CARDINALITY LEGEND:                                                        │
│  ──────────────────────                                                     │
│  [1 : 1]      One-to-One                                                    │
│  [1 : 0..*]   One-to-Many (Optional)                                        │
│  [1 : 1..*]   One-to-Many (Required)                                        │
│  [M : N]      Many-to-Many (Bridge Table Required)                          │
│                                                                             │
│  CASCADE:   Child deleted when parent deleted                               │
│  RESTRICT:  Cannot delete parent if children exist                          │
│  SET NULL:  Foreign key set to NULL when parent deleted                     │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow Examples

### Flow 1: Creating Product with Variants
```
Step 1: Create Category
┌─────────────────────┐
│   CATEGORIES        │  INSERT: id='cat-001', name='Electronics'
└─────────────────────┘

Step 2: Create Base Product
┌─────────────────────┐
│   PRODUCTS          │  INSERT: id='prod-001', category_id='cat-001',
└─────────────────────┘         name='T-Shirt', price=20.00

Step 3: Create Option Types
┌─────────────────────┐
│  VARIANT_OPTIONS    │  INSERT: id='opt-001', name='Size'
└─────────────────────┘  INSERT: id='opt-002', name='Color'

Step 4: Create Option Values
┌─────────────────────────────┐
│  VARIANT_OPTION_VALUES      │  INSERT: id='val-001', option_id='opt-001',
└─────────────────────────────┘         value='Small', price_adj=0
                                 INSERT: id='val-002', value='Medium', price_adj=2
                                 INSERT: id='val-003', value='Red', price_adj=0

Step 5: Create Variant (SKU)
┌─────────────────────┐
│  PRODUCT_VARIANTS   │  INSERT: id='var-001', product_id='prod-001',
└─────────────────────┘         sku='TSHIRT-RED-M', price=22.00

Step 6: Link Combinations (M:N)
┌─────────────────────────────┐
│  VARIANT_COMBINATIONS       │  INSERT: variant_id='var-001', value_id='val-002' (Medium)
└─────────────────────────────┘  INSERT: variant_id='var-001', value_id='val-003' (Red)

Step 7: Assign Barcode
┌─────────────────────┐
│   BARCODES          │  INSERT: barcode='8902190410203',
└─────────────────────┘         product_variant_id='var-001'
```

### Flow 2: POS Barcode Scanning
```
┌────────────┐
│ 1. SCAN    │  Barcode: "8902190410203"
└──────┬─────┘
       ↓
┌──────────────────────────────────────┐
│ 2. QUERY BARCODES TABLE              │
│    SELECT * FROM barcodes            │
│    WHERE barcode = '8902190410203'   │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ 3. CHECK XOR CONSTRAINT              │
│    Found: product_variant_id='var-001'│
│    (product_id is NULL)              │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ 4. FETCH VARIANT DETAILS             │
│    SELECT * FROM product_variants    │
│    WHERE id = 'var-001'              │
│    Result: sku='TSHIRT-RED-M',       │
│            price=22.00, stock=50     │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ 5. FETCH ATTRIBUTE COMBINATIONS      │
│    SELECT opt.name, val.value        │
│    FROM variant_combinations vc      │
│    JOIN variant_option_values val    │
│      ON vc.value_id = val.id         │
│    JOIN variant_options opt          │
│      ON val.option_id = opt.id       │
│    WHERE vc.variant_id = 'var-001'   │
│    Result: Color=Red, Size=Medium    │
└──────┬───────────────────────────────┘
       ↓
┌──────────────────────────────────────┐
│ 6. DISPLAY ON POS                    │
│    T-Shirt (Red, Medium)             │
│    Price: $22.00                     │
│    Stock: 50 units                   │
│    [Add to Cart]                     │
└──────────────────────────────────────┘
```

### Flow 3: Category Tree Query
```
Query: Get all products under "Electronics" (including sub-categories)

┌─────────────────────────────────────────────────────────────────┐
│  WITH RECURSIVE category_tree AS (                              │
│    -- Anchor: Start with Electronics                            │
│    SELECT id FROM categories WHERE id = 'cat-001'               │
│                                                                 │
│    UNION ALL                                                    │
│                                                                 │
│    -- Recursive: Get all children                               │
│    SELECT c.id FROM categories c                                │
│    JOIN category_tree ct ON c.parent_id = ct.id                │
│  )                                                              │
│  SELECT p.* FROM products p                                     │
│  JOIN category_tree ct ON p.category_id = ct.id                │
└─────────────────────────────────────────────────────────────────┘

Execution Flow:
  Electronics (cat-001)
       ↓
  ┌───────────┬────────────┐
  ↓           ↓            ↓
Phones    Computers    Cameras
(cat-002)  (cat-003)   (cat-004)
  ↓
Smartphones
(cat-005)

Result: All products linked to cat-001, cat-002, cat-003, cat-004, cat-005
```

---

## 📋 Table Details

### 1. **CATEGORIES**
**Purpose:** Unlimited-depth hierarchical tree

| Key Features | Description |
|---|---|
| Self-referencing | `parent_id` references same table |
| Cascade | ON DELETE SET NULL (orphans become roots) |
| Unique Slug | SEO-friendly URLs |
| Manual Ordering | `position` field for custom sort |

**Example Hierarchy:**
```
Electronics
├── Phones
│   └── Smartphones
└── Computers
```

---

### 2. **PRODUCTS**
**Purpose:** Base product information (variants inherit from this)

| Key Features | Description |
|---|---|
| Category Link | ON DELETE RESTRICT (protect categories) |
| Unique SKU | Global uniqueness enforced |
| Pricing | Base price + cost + tax rate |
| Stock Tracking | Total stock across all variants |
| Status Check | `active` \| `inactive` constraint |

**Pricing Flow:**
```
Base Price: $20.00
Cost Price: $8.00
Tax Rate: 10%
→ Final Price: $20.00 + ($20.00 × 0.10) = $22.00
```

---

### 3. **VARIANT_OPTIONS**
**Purpose:** Global option types (Size, Color, Material)

| UI Types | Use Case |
|---|---|
| BUTTON | Small choices (S, M, L) |
| DROPDOWN | Long lists (30+ colors) |
| SWATCH | Visual selection (color squares) |

---

### 4. **VARIANT_OPTION_VALUES**
**Purpose:** Specific values with price adjustments

| Adjustment Type | Formula | Example |
|---|---|---|
| FIXED | Base + Amount | $20 + $5 = $25 |
| PERCENTAGE | Base × (1 + %) | $20 × 1.10 = $22 |

**Example Data:**
- Size: Small ($0), Medium (+$2), Large (+$5)
- Color: Red ($0), Blue ($0), Gold (+10%)

---

### 5. **PRODUCT_VARIANTS**
**Purpose:** Physical SKU with specific attributes

| Key Features | Description |
|---|---|
| Unique SKU | Per variant (TSHIRT-RED-M) |
| Independent Pricing | Overrides base product price |
| Stock Tracking | Per-variant inventory |
| Cascade | ON DELETE CASCADE with product |

---

### 6. **VARIANT_COMBINATIONS**
**Purpose:** Bridge table (M:N relationship)

**How It Works:**
```
Variant: TSHIRT-RED-M (var-002)
├── Combination 1: var-002 → Red (val-004)
└── Combination 2: var-002 → Medium (val-003)
```

Each variant has multiple combinations defining its attributes.

---

### 7. **BARCODES**
**Purpose:** Central registry with format validation

| Format | Length | Use Case |
|---|---|---|
| UPC-A | 12 digits | North America |
| EAN-13 | 13 digits | International |
| CODE-128 | Variable | Alphanumeric |

**XOR Constraint:** Links to EITHER product OR variant (never both, never neither)

**Scanning Flow:**
```
1. Scan barcode "8902190410203"
2. Query barcodes table
3. Find linked product/variant
4. Retrieve price, stock, details
5. Add to cart
```

---

## 🔗 Cascade Behaviors

| Action | Result |
|---|---|
| Delete Category (with products) | ❌ BLOCKED (RESTRICT) |
| Delete Category (no products) | ✅ Children become roots |
| Delete Product | ✅ All variants deleted (CASCADE) |
| Delete Product | ✅ All barcodes deleted (CASCADE) |
| Delete Variant | ✅ All combinations deleted (CASCADE) |
| Delete Option Value (in use) | ❌ BLOCKED (RESTRICT) |

---

## 💡 Key Workflows

### Creating Product with Variants
```
1. Create Category (Electronics)
2. Create Product (T-Shirt, base price $20)
3. Create Options (Size, Color)
4. Create Values (Small, Red)
5. Create Variant (TSHIRT-RED-S, price $21)
6. Link Combinations (variant → Red + Small)
7. Assign Barcode (8902190410203 → variant)
```

### Barcode Scanning at POS
```
Scan → Lookup barcode table → Get product/variant ID
     → Fetch details (price, tax, stock)
     → Display on screen
     → Process transaction
```

### Category Tree Navigation
```
SELECT with RECURSIVE CTE
→ Start from root (parent_id IS NULL)
→ Join children iteratively
→ Build full tree with levels
```

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

### Indexes (Already in Schema.sql)
```
categories: parent_id, slug
products: category_id, sku
product_variants: product_id
variant_combinations: product_variant_id
barcodes: product_id, product_variant_id
```

### Best Practices
1. Filter by indexed columns (id, sku, slug)
2. Use JOINs over multiple queries
3. Pagination with LIMIT/OFFSET
4. Cache static data (options, values)
5. Run ANALYZE periodically

---

## 🔄 Maintenance

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
ANALYZE;  -- Update statistics
VACUUM;   -- Free space
```

---

## 📝 Normalization (3NF)

**Why 3NF?**

| Normal Form | Rule | Implementation |
|---|---|---|
| 1NF | Atomic values | No comma-separated lists |
| 2NF | No partial dependencies | Bridge tables (combinations) |
| 3NF | No transitive dependencies | Option values separate from options |

**Benefits:**
- ✅ No redundancy
- ✅ Data integrity
- ✅ Easy updates
- ✅ Flexible queries

---

## 📚 Schema Version

- **Version:** 1.0
- **Updated:** January 16, 2026
- **PostgreSQL:** 12+
- **Encoding:** UTF-8

---

**For detailed SQL with comments, see [Schema.sql](Schema.sql)**

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