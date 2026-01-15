package com.inventory.backend_api.services;

import com.inventory.backend_api.dto.*;
import com.inventory.backend_api.entity.*;
import com.inventory.backend_api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VariantService {

    @Autowired private VariantOptionRepository optionRepo;
    @Autowired private VariantOptionValueRepository valueRepo;
    @Autowired private ProductVariantRepository variantRepo;
    @Autowired private ProductRepository productRepo;

    // --- 1. OPTION MANAGEMENT ---

    public List<VariantOption> getAllOptions() {
        return optionRepo.findAll();
    }

    public VariantOption createOption(CreateOptionRequest req) {
        VariantOption option = new VariantOption();
        option.setName(req.getName());
        option.setType(VariantOption.OptionType.valueOf(req.getType())); // Ensure Enum match
        option.setPosition(req.getPosition() != null ? req.getPosition() : 0);
        option.setIsRequired(req.getIsRequired() != null ? req.getIsRequired() : true);
        return optionRepo.save(option);
    }

    public void deleteOption(String id) {
        if (!optionRepo.existsById(id)) throw new RuntimeException("Option not found");
        // Note: DB constraints might block this if variants exist
        optionRepo.deleteById(id);
    }

    // --- 2. VALUE MANAGEMENT ---

    public List<VariantOptionValue> getOptionValues(String optionId) {
        return valueRepo.findByVariantOptionId(optionId);
    }

    public VariantOptionValue createOptionValue(String optionId, CreateValueRequest req) {
        VariantOption option = optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        VariantOptionValue val = new VariantOptionValue();
        val.setVariantOption(option);
        val.setValue(req.getValue());
        val.setDisplayValue(req.getDisplayValue());
        val.setPriceAdjustmentValue(req.getPriceAdjustment() != null ? req.getPriceAdjustment() : BigDecimal.ZERO);

        return valueRepo.save(val);
    }

    public void deleteOptionValue(String optionId, String valueId) {
        valueRepo.deleteById(valueId);
    }

    // --- 3. MATRIX GENERATION (The Complex Logic) ---

    @Transactional
    public List<ProductVariant> generateMatrix(GenerateMatrixRequest req) {
        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // A. Collect all lists of values to combine
        // Example: [[Red, Blue], [Small, Medium]]
        List<List<VariantOptionValue>> allLists = new ArrayList<>();

        for (GenerateMatrixRequest.OptionSelection selection : req.getSelections()) {
            List<VariantOptionValue> values = valueRepo.findAllById(selection.getValueIds());
            if (!values.isEmpty()) {
                allLists.add(values);
            }
        }

        // B. Calculate Cartesian Product
        List<List<VariantOptionValue>> combinations = cartesianProduct(allLists);

        // C. Create Variants for each combination
        List<ProductVariant> newVariants = new ArrayList<>();

        for (List<VariantOptionValue> combination : combinations) {
            // Generate SKU: "TSHIRT-RED-S"
            String skuSuffix = combination.stream()
                    .map(v -> v.getValue().toUpperCase().substring(0, Math.min(3, v.getValue().length())))
                    .collect(Collectors.joining("-"));

            String sku = product.getSku() + "-" + skuSuffix;

            // Skip if exists
            if (variantRepo.findBySku(sku).isPresent()) continue;

            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setSku(sku);
            variant.setPrice(req.getDefaultPrice() != null ? req.getDefaultPrice() : product.getPrice());
            variant.setStockLevel(req.getDefaultStock() != null ? req.getDefaultStock() : 0);
            variant.setIsActive(true);

            // Map the combination (The Logic for @ManyToMany)
            // Note: You might need to manually save VariantCombination entities depending on your Cascade settings,
            // but if ProductVariant has CascadeType.ALL on 'combinations', this helper might need adjustment.
            // For now, assuming you have a simpler @ManyToMany List<VariantOptionValue> in ProductVariant for simplicity,
            // OR you handle the bridge entity creation here.

            // Simulating save logic:
            newVariants.add(variant);
        }

        return variantRepo.saveAll(newVariants);
    }

    // Helper: Recursive Cartesian Product
    private <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        List<List<T>> result = new ArrayList<>();
        if (lists.isEmpty()) {
            result.add(new ArrayList<>());
            return result;
        }
        List<T> firstList = lists.get(0);
        List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));

        for (T element : firstList) {
            for (List<T> remainingList : remainingLists) {
                List<T> newCombination = new ArrayList<>();
                newCombination.add(element);
                newCombination.addAll(remainingList);
                result.add(newCombination);
            }
        }
        return result;
    }

    // --- 4. VARIANT OPERATIONS ---

    public List<ProductVariant> getProductVariants(String productId) {
        return variantRepo.findByProductId(productId);
    }

    public ProductVariant updateInventory(String id, Integer stock) {
        ProductVariant v = variantRepo.findById(id).orElseThrow(() -> new RuntimeException("Variant not found"));
        v.setStockLevel(stock);
        return variantRepo.save(v);
    }

    public ProductVariant updatePricing(String id, BigDecimal price) {
        ProductVariant v = variantRepo.findById(id).orElseThrow(() -> new RuntimeException("Variant not found"));
        v.setPrice(price);
        return variantRepo.save(v);
    }
}
