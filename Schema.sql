-- ==========================================
-- POS SYSTEM SCHEMA (7 Tables)
-- Fully Normalized for Hibernate & Barcode Scanning
-- ==========================================

-- 1. CATEGORIES TABLE
-- Handles the hierarchical tree (parent-child) structure.
CREATE TABLE categories (
    id VARCHAR(50) PRIMARY KEY,                 -- Mapped to String ID
    parent_id VARCHAR(50),                      -- Self-referencing FK for tree structure
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

-- 2. PRODUCTS TABLE
-- The main product definition.
CREATE TABLE products (
    id VARCHAR(50) PRIMARY KEY,
    category_id VARCHAR(50) NOT NULL,           -- Link to Category
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,           -- Unique Stock Keeping Unit
    
    -- NOTE: 'barcode' column removed (Moved to 'barcodes' table)
    
    description TEXT,
    image VARCHAR(255),
    unit VARCHAR(50),                           
    
    -- Pricing & Inventory
    price DECIMAL(10, 2) NOT NULL,              -- Base selling price
    cost_price DECIMAL(10, 2),                  -- Cost price
    tax_rate DECIMAL(5, 2) DEFAULT 0.0,
    stock_level INTEGER DEFAULT 0,              -- Aggregate stock
    reorder_level INTEGER DEFAULT 10,
    
    status VARCHAR(20) DEFAULT 'active' 
        CHECK (status IN ('active', 'inactive')),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE RESTRICT
);

-- 3. VARIANT OPTIONS TABLE
-- Global definitions (e.g., "Size", "Color").
CREATE TABLE variant_options (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,                 
    position INTEGER DEFAULT 0,
    is_required BOOLEAN DEFAULT TRUE,
    
    type VARCHAR(20) NOT NULL 
        CHECK (type IN ('BUTTON', 'DROPDOWN', 'SWATCH'))
);

-- 4. VARIANT OPTION VALUES TABLE
-- Specific values (e.g., "Red", "Small").
CREATE TABLE variant_option_values (
    id VARCHAR(50) PRIMARY KEY,
    variant_option_id VARCHAR(50) NOT NULL,     -- Link to Parent Option
    value VARCHAR(100) NOT NULL,                -- e.g., "Red"
    display_value VARCHAR(100),                 -- e.g., "#FF0000"
    position INTEGER DEFAULT 0,
    
    price_adjustment_type VARCHAR(20) DEFAULT 'FIXED' 
        CHECK (price_adjustment_type IN ('FIXED', 'PERCENTAGE')),
    price_adjustment_value DECIMAL(10, 2) DEFAULT 0.0,

    CONSTRAINT fk_value_option FOREIGN KEY (variant_option_id) 
        REFERENCES variant_options(id) ON DELETE CASCADE
);

-- 5. PRODUCT VARIANTS TABLE
-- The physical item (SKU). 
CREATE TABLE product_variants (
    id VARCHAR(50) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,            -- Link to parent Product
    sku VARCHAR(100) UNIQUE NOT NULL,           
    
    -- NOTE: 'barcode' column removed (Moved to 'barcodes' table)
    
    price DECIMAL(10, 2) NOT NULL,              -- Specific price for this variant
    cost DECIMAL(10, 2),
    stock_level INTEGER DEFAULT 0,              
    is_active BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) 
        REFERENCES products(id) ON DELETE CASCADE
);

-- 6. VARIANT COMBINATIONS TABLE 
-- The Bridge Table: Links a SKU to its defining values.
-- Example: Row 1 links SKU-101 to "Red". Row 2 links SKU-101 to "Small".
CREATE TABLE variant_combinations (
    id VARCHAR(50) PRIMARY KEY,
    product_variant_id VARCHAR(50) NOT NULL,      -- The SKU
    variant_option_value_id VARCHAR(50) NOT NULL, -- The Value (Red/Small)
    
    CONSTRAINT fk_combo_variant FOREIGN KEY (product_variant_id) 
        REFERENCES product_variants(id) ON DELETE CASCADE,
    CONSTRAINT fk_combo_value FOREIGN KEY (variant_option_value_id) 
        REFERENCES variant_option_values(id) ON DELETE RESTRICT
);

-- 7. BARCODES TABLE (Central Registry)
-- Implements the BarcodeScanner model requirements.
CREATE TABLE barcodes (
    barcode VARCHAR(100) PRIMARY KEY,           -- The Code itself is the ID
    format VARCHAR(20) NOT NULL,                -- 'UPC_A', 'EAN_13', etc.
    
    product_id VARCHAR(50),                     -- Link if it's a simple product
    product_variant_id VARCHAR(50),             -- Link if it's a variant (Size/Color)
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- VALIDATION: Format must be valid
    CONSTRAINT chk_barcode_format CHECK (format IN ('UPC_A', 'UPC_E', 'EAN_13', 'EAN_8', 'CODE_128')),

    -- VALIDATION: Must link to EITHER a Product OR a Variant, never both, never neither.
    CONSTRAINT chk_barcode_target CHECK (
        (product_id IS NOT NULL AND product_variant_id IS NULL) OR 
        (product_id IS NULL AND product_variant_id IS NOT NULL)
    ),

    CONSTRAINT fk_barcode_product FOREIGN KEY (product_id) 
        REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_barcode_variant FOREIGN KEY (product_variant_id) 
        REFERENCES product_variants(id) ON DELETE CASCADE
);