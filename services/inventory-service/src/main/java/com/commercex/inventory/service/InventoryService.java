package com.commercex.inventory.service;

import com.commercex.inventory.dto.request.*;
import com.commercex.inventory.dto.response.InventoryResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse create(CreateInventoryRequest request);

    InventoryResponse getByProductId(UUID productId);

    List<InventoryResponse> getAll();

    InventoryResponse update(UUID productId,
                             UpdateInventoryRequest request);

    void delete(UUID productId);
    InventoryResponse reserveStock(UUID productId,
                                   ReserveStockRequest request);
    InventoryResponse releaseStock(UUID productId,
                                   ReleaseStockRequest request);
    InventoryResponse deductStock(UUID productId,
                                  DeductStockRequest request);
    InventoryResponse restoreStock(UUID productId,
                                   RestoreStockRequest request);
    void release(UUID productId, Integer quantity);
}
