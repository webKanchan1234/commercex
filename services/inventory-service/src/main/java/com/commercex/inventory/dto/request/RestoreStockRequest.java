package com.commercex.inventory.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RestoreStockRequest {

    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;

}