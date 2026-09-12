package com.commercex.product.dto.request;

import com.commercex.product.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSearchRequest {

    private String keyword;

    private String brand;

    private String category;

    private ProductStatus status;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}