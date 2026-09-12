package com.commercex.product.mapper;

import com.commercex.product.dto.request.CreateProductRequest;
import com.commercex.product.dto.request.UpdateProductRequest;
import com.commercex.product.dto.response.ProductResponse;
import com.commercex.product.entity.Brand;
import com.commercex.product.entity.Category;
import com.commercex.product.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-27T12:55:11+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Homebrew)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(CreateProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product product = new Product();

        product.setName( request.getName() );
        product.setSlug( request.getSlug() );
        product.setDescription( request.getDescription() );
        product.setPrice( request.getPrice() );
        product.setDiscountPrice( request.getDiscountPrice() );
        product.setStatus( request.getStatus() );
        product.setStockTracked( request.getStockTracked() );

        return product;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setBrand( productBrandName( product ) );
        productResponse.setCategory( productCategoryName( product ) );
        productResponse.setId( product.getId() );
        productResponse.setSku( product.getSku() );
        productResponse.setName( product.getName() );
        productResponse.setSlug( product.getSlug() );
        productResponse.setDescription( product.getDescription() );
        productResponse.setPrice( product.getPrice() );
        productResponse.setDiscountPrice( product.getDiscountPrice() );
        productResponse.setStatus( product.getStatus() );

        return productResponse;
    }

    @Override
    public void updateEntity(UpdateProductRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            product.setName( request.getName() );
        }
        if ( request.getSlug() != null ) {
            product.setSlug( request.getSlug() );
        }
        if ( request.getDescription() != null ) {
            product.setDescription( request.getDescription() );
        }
        if ( request.getPrice() != null ) {
            product.setPrice( request.getPrice() );
        }
        if ( request.getDiscountPrice() != null ) {
            product.setDiscountPrice( request.getDiscountPrice() );
        }
        if ( request.getStatus() != null ) {
            product.setStatus( request.getStatus() );
        }
        if ( request.getStockTracked() != null ) {
            product.setStockTracked( request.getStockTracked() );
        }
    }

    private String productBrandName(Product product) {
        Brand brand = product.getBrand();
        if ( brand == null ) {
            return null;
        }
        return brand.getName();
    }

    private String productCategoryName(Product product) {
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }
}
