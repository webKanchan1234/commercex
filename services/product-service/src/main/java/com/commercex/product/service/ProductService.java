package com.commercex.product.service;

import com.commercex.product.dto.request.CreateProductRequest;
import com.commercex.product.dto.request.ProductSearchRequest;
import com.commercex.product.dto.request.UpdateProductRequest;
import com.commercex.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse update(UUID id,
                           UpdateProductRequest request);

    ProductResponse getById(UUID id);

    ProductResponse getBySlug(String slug);

    Page<ProductResponse> getAll(Pageable pageable);

    void delete(UUID id);
    Page<ProductResponse> search(
            ProductSearchRequest request,
            Pageable pageable
    );

}