package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateBrandRequest;
import com.commercex.product.dto.request.UpdateBrandRequest;
import com.commercex.product.dto.response.BrandResponse;
import com.commercex.product.entity.Brand;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BrandMapper {

    Brand toEntity(CreateBrandRequest request);

    BrandResponse toResponse(Brand brand);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            UpdateBrandRequest request,
            @MappingTarget Brand brand
    );
}