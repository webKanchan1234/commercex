package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateCategoryRequest;
import com.commercex.product.dto.request.UpdateCategoryRequest;
import com.commercex.product.dto.response.CategoryResponse;
import com.commercex.product.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-13T01:19:07+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category category = new Category();

        category.setDescription( request.getDescription() );
        category.setName( request.getName() );
        category.setSlug( request.getSlug() );

        return category;
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setActive( category.getActive() );
        categoryResponse.setDescription( category.getDescription() );
        categoryResponse.setId( category.getId() );
        categoryResponse.setName( category.getName() );
        categoryResponse.setSlug( category.getSlug() );

        return categoryResponse;
    }

    @Override
    public void updateEntity(UpdateCategoryRequest request, Category category) {
        if ( request == null ) {
            return;
        }

        if ( request.getActive() != null ) {
            category.setActive( request.getActive() );
        }
        if ( request.getDescription() != null ) {
            category.setDescription( request.getDescription() );
        }
        if ( request.getName() != null ) {
            category.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            category.setSlug( request.getSlug() );
        }
    }
}
