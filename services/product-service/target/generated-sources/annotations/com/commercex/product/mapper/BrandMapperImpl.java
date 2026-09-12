package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateBrandRequest;
import com.commercex.product.dto.request.UpdateBrandRequest;
import com.commercex.product.dto.response.BrandResponse;
import com.commercex.product.entity.Brand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-13T01:19:08+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class BrandMapperImpl implements BrandMapper {

    @Override
    public Brand toEntity(CreateBrandRequest request) {
        if ( request == null ) {
            return null;
        }

        Brand brand = new Brand();

        brand.setDescription( request.getDescription() );
        brand.setName( request.getName() );
        brand.setSlug( request.getSlug() );

        return brand;
    }

    @Override
    public BrandResponse toResponse(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        BrandResponse brandResponse = new BrandResponse();

        brandResponse.setActive( brand.getActive() );
        brandResponse.setDescription( brand.getDescription() );
        brandResponse.setId( brand.getId() );
        brandResponse.setName( brand.getName() );
        brandResponse.setSlug( brand.getSlug() );

        return brandResponse;
    }

    @Override
    public void updateEntity(UpdateBrandRequest request, Brand brand) {
        if ( request == null ) {
            return;
        }

        if ( request.getActive() != null ) {
            brand.setActive( request.getActive() );
        }
        if ( request.getDescription() != null ) {
            brand.setDescription( request.getDescription() );
        }
        if ( request.getName() != null ) {
            brand.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            brand.setSlug( request.getSlug() );
        }
    }
}
