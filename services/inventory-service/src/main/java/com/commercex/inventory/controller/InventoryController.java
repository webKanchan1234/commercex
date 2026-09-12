package com.commercex.inventory.controller;

import com.commercex.inventory.dto.request.*;
import com.commercex.inventory.dto.response.InventoryResponse;
import com.commercex.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory Management APIs")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Create Inventory")
    public ResponseEntity<InventoryResponse> create(
            @Valid @RequestBody CreateInventoryRequest request) {

        InventoryResponse response = inventoryService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get Inventory By Product Id")
    public ResponseEntity<InventoryResponse> getByProductId(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                inventoryService.getByProductId(productId)
        );
    }

    @GetMapping
    @Operation(summary = "Get All Inventory")
    public ResponseEntity<List<InventoryResponse>> getAll() {

        return ResponseEntity.ok(
                inventoryService.getAll()
        );
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update Inventory")
    public ResponseEntity<InventoryResponse> update(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateInventoryRequest request) {

        return ResponseEntity.ok(
                inventoryService.update(productId, request)
        );
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete Inventory")
    public ResponseEntity<Void> delete(
            @PathVariable UUID productId) {

        inventoryService.delete(productId);

        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{productId}/reserve")
    public ResponseEntity<InventoryResponse> reserveStock(
            @PathVariable UUID productId,
            @Valid @RequestBody ReserveStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.reserveStock(productId, request)
        );
    }


    @PostMapping("/{productId}/release")
    public ResponseEntity<InventoryResponse> releaseStock(
            @PathVariable UUID productId,
            @Valid @RequestBody ReleaseStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.releaseStock(productId, request)
        );
    }

    @PostMapping("/{productId}/deduct")
    public ResponseEntity<InventoryResponse> deductStock(
            @PathVariable UUID productId,
            @Valid @RequestBody DeductStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.deductStock(productId, request)
        );
    }

    @PostMapping("/{productId}/restore")
    public ResponseEntity<InventoryResponse> restoreStock(
            @PathVariable UUID productId,
            @Valid @RequestBody RestoreStockRequest request) {

        return ResponseEntity.ok(
                inventoryService.restoreStock(productId, request)
        );

    }

    @PostMapping("/release")
    public ResponseEntity<Void> release(
            @Valid @RequestBody InventoryReleaseRequest request) {

        inventoryService.release(
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.noContent().build();
    }

}