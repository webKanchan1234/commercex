package com.commercex.product.service;

import com.commercex.common.exception.DuplicateProductException;
import com.commercex.common.exception.ErrorCode;
import com.commercex.common.exception.ResourceNotFoundException;
import com.commercex.product.dto.request.CreateBrandRequest;
import com.commercex.product.dto.request.UpdateBrandRequest;
import com.commercex.product.dto.response.BrandResponse;
import com.commercex.product.entity.Brand;
import com.commercex.product.mapper.BrandMapper;
import com.commercex.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandResponse create(CreateBrandRequest request) {

        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateProductException("Brand slug already exists");
        }

        if (brandRepository.existsByName(request.getName())) {
            throw new DuplicateProductException("Brand name already exists");
        }

        Brand brand = brandMapper.toEntity(request);

        Brand saved = brandRepository.save(brand);

        return brandMapper.toResponse(saved);
    }

    @Override
    public BrandResponse update(UUID id, UpdateBrandRequest request) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"Brand not found"));

        brandMapper.updateEntity(request, brand);

        return brandMapper.toResponse(
                brandRepository.save(brand)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getById(UUID id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"Brand not found"));

        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAll() {

        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"Brand not found"));

        brandRepository.delete(brand);
    }
}