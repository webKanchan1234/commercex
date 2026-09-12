package com.commercex.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull
    private UUID productId;

    @NotBlank
    private String productName;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;
}