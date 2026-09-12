package com.commercex.product.dto.response;

import com.commercex.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Product Response")
public class ProductResponse {

    @Schema(example = "3dcd87...")
    private UUID id;

    @Schema(example = "SKU-AB123456")
    private String sku;

    private String name;

    private String slug;

    private String description;

    private BigDecimal price;

    private BigDecimal discountPrice;

    @Schema(example = "Apple")
    private String brand;

    private String category;

    private ProductStatus status;

}
