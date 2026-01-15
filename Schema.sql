-- ==========================================
-- DATABASE CREATION
-- ==========================================
DROP DATABASE IF EXISTS api;
CREATE DATABASE api;
\c api;

-- ==========================================
-- TECHSLOYD INVENTORY MANAGEMENT SCHEMA
-- ==========================================
-- Purpose: Retail/E-commerce inventory with product variants & barcode scanning
-- Tables: 7 (3NF Normalized)
-- Architecture: Categories → Products → Variants → Combinations ← Option Values
--
-- Key Flows:
-- 1. Category Tree: Self-referencing hierarchy with unlimited depth
-- 2. Product Variants: Base product + attributes (Size/Color) = Specific SKU
-- 3. Barcode Scanning: XOR link to Product OR Variant for POS systems
-- 4. Price Calculation: Base + Variant Adjustments (FIXED/PERCENTAGE)
-- ==========================================

-- ==========================================
-- TABLE 1: CATEGORIES
-- ==========================================
-- Purpose: Hierarchical product categorization (tree structure)
-- Flow: parent_id references same table → unlimited nesting
-- Cascade: ON DELETE SET NULL → orphaned children become root categories
-- Use Case: Electronics → Phones → Smartphones (3 levels)
-- ==========================================
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

-- ==========================================
-- TABLE 2: PRODUCTS
-- ==========================================
-- Purpose: Base product info (variants inherit from this)
-- Flow: category_id → categories table
-- Cascade: ON DELETE RESTRICT → cannot delete category if products exist
-- Working: Base price/cost/tax applied to all variants unless overridden
-- Note: Barcodes in separate table (1:N relationship)
-- ==========================================
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
    status VARCHAR(20) DEFAULT 'active' 
        CHECK (status IN ('active', 'draft', 'archived')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE RESTRICT
);

-- ==========================================
-- TABLE 3: VARIANT_OPTIONS
-- ==========================================
-- Purpose: Define global option types (Size, Color, Material)
-- Working: Reusable across all products (not product-specific)
-- Types: BUTTON (radio), DROPDOWN (select), SWATCH (visual)
-- Flow: One option → Many values (e.g., Size → Small/Medium/Large)
-- ==========================================
CREATE TABLE variant_options (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position INTEGER DEFAULT 0,
    type VARCHAR(20) NOT NULL 
        CHECK (type IN ('BUTTON', 'DROPDOWN', 'SWATCH'))
);

-- ==========================================
-- TABLE 4: VARIANT_OPTION_VALUES
-- ==========================================
-- Purpose: Specific values for each option + price adjustments
-- Flow: variant_option_id → variant_options
-- Cascade: ON DELETE CASCADE → removing option removes all its values
-- Price Logic:
--   FIXED: Base $20 + Large $5 = $25
--   PERCENTAGE: Base $20 + 10% = $22
-- Working: Multiple values per option (Size: S/M/L/XL)
-- ==========================================
CREATE TABLE variant_option_values (
    id VARCHAR(50) PRIMARY KEY,
    variant_option_id VARCHAR(50) NOT NULL,
    value VARCHAR(100) NOT NULL,
    position INTEGER DEFAULT 0,
    price_adjustment_type VARCHAR(20) DEFAULT 'FIXED' 
        CHECK (price_adjustment_type IN ('FIXED', 'PERCENTAGE')),
    price_adjustment_value DECIMAL(10, 2) DEFAULT 0.0,

    CONSTRAINT fk_value_option FOREIGN KEY (variant_option_id) 
        REFERENCES variant_options(id) ON DELETE CASCADE
);

-- ==========================================
-- TABLE 5: PRODUCT_VARIANTS
-- ==========================================
-- Purpose: Physical SKU with specific attribute combinations
-- Flow: product_id → products (parent)
-- Cascade: ON DELETE CASCADE → variants deleted with product
-- Working: Each variant = unique SKU (e.g., TSHIRT-RED-M)
-- Price: Can override base product price independently
-- Stock: Tracked per variant (not aggregated)
-- Example: iPhone-Red-128GB vs iPhone-Blue-256GB (different SKUs)
-- ==========================================
CREATE TABLE product_variants (
    id VARCHAR(50) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_level INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) 
        REFERENCES products(id) ON DELETE CASCADE
);

-- ==========================================
-- TABLE 6: VARIANT_COMBINATIONS (Bridge Table)
-- ==========================================
-- Purpose: Links variants to their option values (M:N relationship)
-- Flow: product_variant_id → product_variants
--       variant_option_value_id → variant_option_values
-- Cascade: 
--   - Variant deleted → CASCADE removes combinations
--   - Value deleted → RESTRICT prevents (value still in use)
-- Working Example:
--   - TSHIRT-RED-M has 2 rows:
--     Row 1: variant → "Red" (color)
--     Row 2: variant → "Medium" (size)
-- ==========================================
CREATE TABLE variant_combinations (
    id VARCHAR(50) PRIMARY KEY,
    product_variant_id VARCHAR(50) NOT NULL,
    variant_option_value_id VARCHAR(50) NOT NULL,
    
    CONSTRAINT fk_combo_variant FOREIGN KEY (product_variant_id) 
        REFERENCES product_variants(id) ON DELETE CASCADE,
    CONSTRAINT fk_combo_value FOREIGN KEY (variant_option_value_id) 
        REFERENCES variant_option_values(id) ON DELETE RESTRICT
);

-- ==========================================
-- TABLE 7: BARCODES (Central Registry)
-- ==========================================
-- Purpose: Store all barcodes with format validation
-- Formats: UPC-A (12), UPC-E (8), EAN-13 (13), EAN-8 (8), CODE-128 (variable)
-- XOR Constraint: Links to EITHER product OR variant (never both/neither)
-- Cascade: ON DELETE CASCADE → barcode removed with parent entity
-- 
-- POS Scanning Flow:
--   1. Scan barcode "8902190410203"
--   2. Query: SELECT * FROM barcodes WHERE barcode = ?
--   3. Get product_id OR product_variant_id
--   4. Fetch price, stock, details from linked table
--   5. Process transaction
-- 
-- Use Cases:
--   - Simple product: barcode → product (no variants)
--   - Variant product: barcode → specific variant (Red-Small)
-- ==========================================
CREATE TABLE barcodes (
    barcode VARCHAR(100) PRIMARY KEY,
    format VARCHAR(20) NOT NULL,
    product_id VARCHAR(50),
    product_variant_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_barcode_format CHECK (format IN ('UPC_A', 'UPC_E', 'EAN_13', 'EAN_8', 'CODE_128')),
    CONSTRAINT chk_barcode_target CHECK (
        (product_id IS NOT NULL AND product_variant_id IS NULL) OR 
        (product_id IS NULL AND product_variant_id IS NOT NULL)
    ),
    CONSTRAINT fk_barcode_product FOREIGN KEY (product_id) 
        REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_barcode_variant FOREIGN KEY (product_variant_id) 
        REFERENCES product_variants(id) ON DELETE CASCADE
);

-- ==========================================
-- END OF SCHEMA
-- ==========================================
-- Complete Flow Example (Product Creation):
--   1. Create Category: Electronics
--   2. Create Product: T-Shirt (base price $20)
--   3. Create Options: Size, Color
--   4. Create Values: Small/Medium/Large, Red/Blue
--   5. Create Variant: TSHIRT-RED-M (price $22)
--   6. Link Combinations: variant → Red, variant → Medium
--   7. Assign Barcode: 8902190410203 → variant
-- ==========================================