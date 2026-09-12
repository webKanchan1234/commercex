package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateProductRequest;
import com.commercex.product.dto.request.UpdateProductRequest;
import com.commercex.product.dto.response.ProductResponse;
import com.commercex.product.entity.Product;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    Product toEntity(CreateProductRequest request);

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "category", source = "category.name")
    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            UpdateProductRequest request,
            @MappingTarget Product product
    );

}