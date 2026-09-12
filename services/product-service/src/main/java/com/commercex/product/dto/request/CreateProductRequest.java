package com.commercex.product.dto.request;

import com.commercex.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Create Product Request")
public class CreateProductRequest {

    @NotBlank(message = "Product name is required.")
    @Size(max = 200)
    @Schema(
            example = "Apple iPhone 17 Pro Max",
            description = "Product Name"
    )
    private String name;

    @NotBlank(message = "Slug is required.")
    @Size(max = 250)
    @Schema(
            example = "iphone-17-pro-max"
    )
    private String slug;

    @Size(max = 5000)
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01")
    @Schema(
            example = "120000"
    )
    private BigDecimal price;

    @DecimalMin(value = "0.00")
    private BigDecimal discountPrice;

    @NotNull(message = "Brand is required.")
    private UUID brandId;

    @NotNull(message = "Category is required.")
    private UUID categoryId;

    @NotNull(message = "Status is required.")
    private ProductStatus status;

    @NotNull
    private Boolean stockTracked;

}