package com.commercex.product.service;

import com.commercex.product.dto.request.CreateBrandRequest;
import com.commercex.product.dto.request.UpdateBrandRequest;
import com.commercex.product.dto.response.BrandResponse;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    BrandResponse create(CreateBrandRequest request);

    BrandResponse update(UUID id,
                         UpdateBrandRequest request);

    BrandResponse getById(UUID id);

    List<BrandResponse> getAll();

    void delete(UUID id);
}