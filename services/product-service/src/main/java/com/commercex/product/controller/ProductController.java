package com.commercex.product.controller;

import com.commercex.product.dto.request.CreateProductRequest;
import com.commercex.product.dto.request.ProductSearchRequest;
import com.commercex.product.dto.request.UpdateProductRequest;
import com.commercex.product.dto.response.ProductResponse;
import com.commercex.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(
        name = "Product APIs",
        description = "Manage Products"
)
public class ProductController {


    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Operation(

            summary = "Create Product",

            description = "Creates a new product."

    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "201",
                    description = "Product Created"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Validation Failed"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Brand or Category Not Found"
            )
    })
    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid
            @RequestBody CreateProductRequest request){
        System.out.println("checking");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.create(request));

    }

    @GetMapping("/{id}")
    public ProductResponse getById(
            @PathVariable("id") UUID id) {

        return productService.getById(id);
    }

    @GetMapping
    public Page<ProductResponse> getAll(
            Pageable pageable) {

        log.info(
                "Fetching all products"
        );

        return productService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateProductRequest request) {

        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable("id") UUID id) {

        productService.delete(id);
    }


    @GetMapping("/search")
    public Page<ProductResponse> search(

            ProductSearchRequest request,

            Pageable pageable){

        return productService.search(
                request,
                pageable
        );

    }

}