package com.commercex.order.client;

import com.commercex.order.dto.client.InventoryReleaseRequest;
import com.commercex.order.dto.client.InventoryReserveRequest;
import com.commercex.order.dto.client.InventoryReserveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class InventoryClientImpl implements InventoryClient {

    private final RestClient restClient;

    @Value("${commercex.services.inventory.url}")
    private String inventoryServiceUrl;

    @Override
    public InventoryReserveResponse reserve(
            InventoryReserveRequest request) {

        return restClient
                .post()
                .uri(
                        inventoryServiceUrl
                                + "/api/v1/inventory/reserve"
                )
                .body(request)
                .retrieve()
                .body(InventoryReserveResponse.class);
    }

    @Override
    public void release(
            InventoryReleaseRequest request) {

        restClient
                .post()
                .uri(
                        inventoryServiceUrl
                                + "/api/v1/inventory/release"
                )
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}