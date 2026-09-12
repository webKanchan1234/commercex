package com.commercex.inventory.service;

import com.commercex.inventory.dto.response.InventoryResponse;

import java.util.UUID;

public interface InventoryCacheService {

    InventoryResponse get(UUID productId);

    void save(InventoryResponse response);

    void delete(UUID productId);

}
