package com.commercex.product.service;

import com.commercex.common.exception.DuplicateProductException;
import com.commercex.common.exception.ErrorCode;
import com.commercex.common.exception.ResourceNotFoundException;
import com.commercex.common.redis.RedisLockService;
import com.commercex.product.cache.ProductCacheService;
import com.commercex.product.dto.request.CreateProductRequest;
import com.commercex.product.dto.request.ProductSearchRequest;
import com.commercex.product.dto.request.UpdateProductRequest;
import com.commercex.product.dto.response.ProductResponse;
import com.commercex.product.entity.Brand;
import com.commercex.product.entity.Category;
import com.commercex.product.entity.Product;
import com.commercex.product.mapper.ProductMapper;
import com.commercex.product.repository.BrandRepository;
import com.commercex.product.repository.CategoryRepository;
import com.commercex.product.repository.ProductRepository;
import com.commercex.product.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    private final ProductCacheService productCacheService;

    private final RedisLockService redisLockService;

    private static final Logger log =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
//    @CachePut(value = "products", key = "#result.id")
    public ProductResponse create(CreateProductRequest request) {

        if (productRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateProductException("Slug already exists");
        }

        Product product = productMapper.toEntity(request);

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"request.getBrandId()"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"request.getCategoryId()"));

        product.setBrand(brand);
        product.setCategory(category);

        // Temporary SKU generation
        product.setSku(generateSku());

        Product savedProduct = productRepository.save(product);

        ProductResponse response = productMapper.toResponse(savedProduct);

        productCacheService.save(response);

        return response;
    }






    @Override
//    @CachePut(value = "products", key = "#id")
    public ProductResponse update(UUID id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"id"));

        productMapper.updateEntity(request, product);

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"request.getBrandId()"));

            product.setBrand(brand);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"request.getCategoryId()"));

            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);

        ProductResponse response = productMapper.toResponse(updatedProduct);

        productCacheService.save(response);

        return response;
    }




//    redis caching without locking
//    @Override
//    @Transactional(readOnly = true)
//    public ProductResponse getById(UUID id) {
//
//        String key = "product:" + id;
//
//        ProductResponse cached = productCacheService.get(id);
//
//        if (cached != null) {
//            log.info("Redis HIT for product {}", id);
//            return cached;
//        }
//
//        log.info("Redis MISS for product {}", id);
//
//        Product product = productRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                ErrorCode.RESOURCE_NOT_FOUND,
//                                "Product not found with id: " + id));
//
//        ProductResponse response = productMapper.toResponse(product);
//
//        productCacheService.save(response);
//
//        return response;
//    }


//   redis cacj=hing with locking
@Override
@Transactional(readOnly = true)
public ProductResponse getById(UUID id) {

    ProductResponse cached = productCacheService.get(id);

    if (cached != null) {
        log.info("Redis HIT {}", id);
        return cached;
    }

    log.info("Redis MISS {}", id);

    String lockKey = "lock:product:" + id;

    boolean lockAcquired =
            redisLockService.acquireLock(lockKey);

    if (lockAcquired) {

        try {

            Product product = productRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    ErrorCode.RESOURCE_NOT_FOUND,
                                    "Product not found"));

            ProductResponse response =
                    productMapper.toResponse(product);

            productCacheService.save(response);

            return response;

        } finally {

            redisLockService.releaseLock(lockKey);

        }

    }

    try {

        Thread.sleep(100);

    } catch (InterruptedException e) {

        Thread.currentThread().interrupt();

    }

    ProductResponse retry = productCacheService.get(id);

    if (retry != null) {

        log.info("Redis HIT after retry {}", id);

        return retry;

    }

    Product product = productRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Product not found"));

    ProductResponse response =
            productMapper.toResponse(product);

    productCacheService.save(response);

    return response;
}


    @Override
    @Transactional(readOnly = true)
    public ProductResponse getBySlug(String slug) {

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,slug));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }




    @Override
//    @CacheEvict(value = "products", key = "#id")
    public void delete(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,"id"));
        productCacheService.delete(id);

        productRepository.delete(product);
    }

    /**
     * Temporary SKU Generator.
     * We will replace this with a production implementation later.
     */
    private String generateSku() {
        return "SKU-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(
            ProductSearchRequest request,
            Pageable pageable) {

        return productRepository.findAll(

                        ProductSpecification.search(request),

                        pageable
                )

                .map(productMapper::toResponse);

    }
}