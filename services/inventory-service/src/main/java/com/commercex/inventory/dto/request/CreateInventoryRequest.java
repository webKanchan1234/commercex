package com.commercex.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateInventoryRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotBlank
    private String warehouse;

}