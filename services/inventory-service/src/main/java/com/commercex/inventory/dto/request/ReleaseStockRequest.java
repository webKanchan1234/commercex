package com.commercex.inventory.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReleaseStockRequest {

    @Min(1)
    private Integer quantity;

}