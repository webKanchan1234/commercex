package com.commercex.product.dto.request;

import com.commercex.product.entity.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class UpdateProductRequest {

    @Size(max = 200)
    private String name;

    @Size(max = 250)
    private String slug;

    @Size(max = 5000)
    private String description;

    @DecimalMin("0.01")
    private BigDecimal price;

    @DecimalMin("0.00")
    private BigDecimal discountPrice;

    private UUID brandId;

    private UUID categoryId;

    private ProductStatus status;

    private Boolean stockTracked;

}