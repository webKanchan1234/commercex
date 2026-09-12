package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateCategoryRequest;
import com.commercex.product.dto.request.UpdateCategoryRequest;
import com.commercex.product.dto.response.CategoryResponse;
import com.commercex.product.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-27T12:55:11+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Homebrew)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category category = new Category();

        category.setName( request.getName() );
        category.setSlug( request.getSlug() );
        category.setDescription( request.getDescription() );

        return category;
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId( category.getId() );
        categoryResponse.setName( category.getName() );
        categoryResponse.setSlug( category.getSlug() );
        categoryResponse.setDescription( category.getDescription() );
        categoryResponse.setActive( category.getActive() );

        return categoryResponse;
    }

    @Override
    public void updateEntity(UpdateCategoryRequest request, Category category) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            category.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            category.setSlug( request.getSlug() );
        }
        if ( request.getDescription() != null ) {
            category.setDescription( request.getDescription() );
        }
        if ( request.getActive() != null ) {
            category.setActive( request.getActive() );
        }
    }
}
