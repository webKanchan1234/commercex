package com.commercex.product.service;

import com.commercex.product.dto.request.CreateCategoryRequest;
import com.commercex.product.dto.request.UpdateCategoryRequest;
import com.commercex.product.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest request);

    CategoryResponse update(UUID id,
                            UpdateCategoryRequest request);

    CategoryResponse getById(UUID id);

    List<CategoryResponse> getAll();

    void delete(UUID id);
}