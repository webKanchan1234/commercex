package com.commercex.product.controller;

import com.commercex.product.dto.request.CreateBrandRequest;
import com.commercex.product.dto.request.UpdateBrandRequest;
import com.commercex.product.dto.response.BrandResponse;
import com.commercex.product.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> create(
            @Valid @RequestBody CreateBrandRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(brandService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                brandService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAll() {

        return ResponseEntity.ok(
                brandService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBrandRequest request) {

        return ResponseEntity.ok(
                brandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {

        brandService.delete(id);
    }
}