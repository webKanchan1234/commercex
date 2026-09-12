package com.commercex.order.dto.client;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReserveResponse {

    private UUID productId;

    private Integer quantity;

    private Integer remainingStock;

    private boolean success;

    private String message;
}