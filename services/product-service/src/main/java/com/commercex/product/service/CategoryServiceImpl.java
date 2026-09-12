package com.commercex.product.service;

import com.commercex.common.exception.DuplicateProductException;
import com.commercex.common.exception.ErrorCode;
import com.commercex.common.exception.ResourceNotFoundException;
import com.commercex.product.dto.request.CreateCategoryRequest;
import com.commercex.product.dto.request.UpdateCategoryRequest;
import com.commercex.product.dto.response.CategoryResponse;
import com.commercex.product.entity.Category;
import com.commercex.product.mapper.CategoryMapper;
import com.commercex.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateProductException("Category slug already exists");
        }

        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateProductException("Category name already exists");
        }

        Category category = categoryMapper.toEntity(request);

        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse update(UUID id, UpdateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Category not found"));

        categoryMapper.updateEntity(request, category);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Category not found"));

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Category not found"));

        categoryRepository.delete(category);
    }
}