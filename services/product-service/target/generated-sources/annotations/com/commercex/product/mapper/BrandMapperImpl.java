package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateBrandRequest;
import com.commercex.product.dto.request.UpdateBrandRequest;
import com.commercex.product.dto.response.BrandResponse;
import com.commercex.product.entity.Brand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-27T12:55:11+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Homebrew)"
)
@Component
public class BrandMapperImpl implements BrandMapper {

    @Override
    public Brand toEntity(CreateBrandRequest request) {
        if ( request == null ) {
            return null;
        }

        Brand brand = new Brand();

        brand.setName( request.getName() );
        brand.setSlug( request.getSlug() );
        brand.setDescription( request.getDescription() );

        return brand;
    }

    @Override
    public BrandResponse toResponse(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setId( brand.getId() );
        brandResponse.setName( brand.getName() );
        brandResponse.setSlug( brand.getSlug() );
        brandResponse.setDescription( brand.getDescription() );
        brandResponse.setActive( brand.getActive() );

        return brandResponse;
    }

    @Override
    public void updateEntity(UpdateBrandRequest request, Brand brand) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            brand.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            brand.setSlug( request.getSlug() );
        }
        if ( request.getDescription() != null ) {
            brand.setDescription( request.getDescription() );
        }
        if ( request.getActive() != null ) {
            brand.setActive( request.getActive() );
        }
    }
}
