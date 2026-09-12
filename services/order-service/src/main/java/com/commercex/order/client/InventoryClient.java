package com.commercex.order.client;

import com.commercex.order.dto.client.InventoryReleaseRequest;
import com.commercex.order.dto.client.InventoryReserveRequest;
import com.commercex.order.dto.client.InventoryReserveResponse;

public interface InventoryClient {

    InventoryReserveResponse reserve(
            InventoryReserveRequest request
    );

    void release(
            InventoryReleaseRequest request
    );
}