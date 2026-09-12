package com.commercex.inventory.dto.response;

import com.commercex.inventory.enums.InventoryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InventoryResponse {

    private UUID id;

    private UUID productId;

    private Integer quantity;

    private Integer reservedQuantity;

    private Integer availableQuantity;

    private String warehouse;

    private InventoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}