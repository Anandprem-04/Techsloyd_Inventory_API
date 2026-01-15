package com.inventory.backend_api.controller;

import com.inventory.backend_api.dto.ScanResponse;
import com.inventory.backend_api.entity.Barcode;
import com.inventory.backend_api.dto.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/barcode")
@CrossOrigin("*")
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    // 1. POST /api/barcode/scan - Process Scan
    // Body: { "barcode": "1234567890123" }
    @PostMapping("/scan")
    public ResponseEntity<ScanResponse> scanBarcode(@RequestBody Map<String, String> payload) {
        String code = payload.get("barcode");
        if (code == null) return ResponseEntity.badRequest().build();

        ScanResponse response = barcodeService.scanBarcode(code);

        if (!response.isFound()) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // 2. GET /api/barcode/lookup/:barcode - Lookup (Same logic, different method)
    @GetMapping("/lookup/{barcode}")
    public ResponseEntity<ScanResponse> lookupBarcode(@PathVariable String barcode) {
        ScanResponse response = barcodeService.scanBarcode(barcode);
        return response.isFound() ? ResponseEntity.ok(response) : ResponseEntity.status(404).body(response);
    }

    // 3. POST /api/barcode/assign - Link Barcode (Validation built-in)
    // Body: { "barcode": "...", "format": "EAN_13", "targetId": "uuid", "type": "VARIANT" }
    @PostMapping("/assign")
    public ResponseEntity<?> assignBarcode(@RequestBody Map<String, String> req) {
        try {
            Barcode b = barcodeService.assignBarcode(
                    req.get("barcode"),
                    req.get("format"),
                    req.get("targetId"),
                    req.get("type") // "PRODUCT" or "VARIANT"
            );
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. POST /api/barcode/validate - Check Format Only
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateBarcode(@RequestBody Map<String, String> req) {
        boolean isValid = barcodeService.validateChecksum(req.get("barcode"), req.get("format"));
        return ResponseEntity.ok(Map.of(
                "barcode", req.get("barcode"),
                "valid", isValid,
                "format", req.get("format")
        ));
    }
}