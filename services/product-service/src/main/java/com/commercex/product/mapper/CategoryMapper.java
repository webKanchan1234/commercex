package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateCategoryRequest;
import com.commercex.product.dto.request.UpdateCategoryRequest;
import com.commercex.product.dto.response.CategoryResponse;
import com.commercex.product.entity.Category;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequest request);

    CategoryResponse toResponse(Category category);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            UpdateCategoryRequest request,
            @MappingTarget Category category
    );
}