package com.commercex.inventory.dto.request;

import com.commercex.inventory.enums.InventoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateInventoryRequest {

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotBlank
    private String warehouse;

    @NotNull
    private InventoryStatus status;

}