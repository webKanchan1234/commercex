package com.commercex.inventory.service;

import com.commercex.common.constants.RedisConstants;
import com.commercex.common.exception.BusinessException;
import com.commercex.common.exception.DuplicateProductException;
import com.commercex.common.exception.ErrorCode;
import com.commercex.common.exception.ResourceNotFoundException;
import com.commercex.common.redis.RedisLockService;
import com.commercex.inventory.cache.InventoryCacheService;
import com.commercex.inventory.dto.request.*;
import com.commercex.inventory.dto.response.InventoryResponse;
import com.commercex.inventory.entity.Inventory;
import com.commercex.inventory.enums.InventoryStatus;
import com.commercex.inventory.mapper.InventoryMapper;
import com.commercex.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryCacheService inventoryCacheService;
    private final RedisLockService redisLockService;

    @Override
    @Transactional
    public InventoryResponse create(CreateInventoryRequest request) {

        log.info("Creating inventory for product {}", request.getProductId());

        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new DuplicateProductException(
                    "Inventory already exists for product : " + request.getProductId()
            );
        }

        Inventory inventory = inventoryMapper.toEntity(request);

        Inventory saved = inventoryRepository.save(inventory);

        InventoryResponse response =
                inventoryMapper.toResponse(saved);
        log.info("Inventory created successfully for product {}", saved.getProductId());

        inventoryCacheService.save(response);

        return response;

    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(UUID productId) {

        // Step 1 : Cache lookup
        InventoryResponse cached = inventoryCacheService.get(productId);

        if (cached != null) {

            log.info("Inventory Cache HIT : {}", productId);

            return cached;
        }

        log.info("Inventory Cache MISS : {}", productId);

        String lockKey = RedisConstants.INVENTORY_LOCK_KEY + productId;

        boolean lockAcquired = redisLockService.acquireLock(lockKey);

        if (lockAcquired) {

            try {

                log.info("Redis lock acquired for {}", productId);

                Inventory inventory = inventoryRepository.findByProductId(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Inventory not found : " + productId
                                ));

                InventoryResponse response =
                        inventoryMapper.toResponse(inventory);

                inventoryCacheService.save(response);

                return response;

            } finally {

                redisLockService.releaseLock(lockKey);

                log.info("Redis lock released for {}", productId);
            }
        }

        /*
         * Another thread is already loading data.
         * Wait briefly and retry the cache.
         */
        try {
//            Thread.sleep(100);
            for (int i = 0; i < 5; i++) {

                cached = inventoryCacheService.get(productId);

                if (cached != null) {
                    return cached;
                }

                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        InventoryResponse retry = inventoryCacheService.get(productId);

        if (retry != null) {

            log.info("Inventory Cache HIT after retry : {}", productId);

            return retry;
        }

        /*
         * Fallback if cache is still empty.
         */
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found : " + productId
                        ));

        InventoryResponse response =
                inventoryMapper.toResponse(inventory);

        inventoryCacheService.save(response);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAll() {

        log.info("Fetching all inventory");

        return inventoryRepository.findAll()
                .stream()
                .map(inventoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryResponse update(UUID productId,
                                    UpdateInventoryRequest request) {

        log.info("Updating inventory for product {}", productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found for product : " + productId
                        ));

        inventory.setQuantity(request.getQuantity());
        inventory.setWarehouse(request.getWarehouse());
        inventory.setStatus(request.getStatus());

        Inventory updated =
                inventoryRepository.save(inventory);
        log.info("Inventory updated successfully for product {}", productId);

        InventoryResponse response =
                inventoryMapper.toResponse(updated);

        inventoryCacheService.save(response);

        return response;
    }

    @Override
    @Transactional
    public void delete(UUID productId) {

        log.info("Deleting inventory for product {}", productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found for product : " + productId
                        ));

        inventoryRepository.delete(inventory);

        inventoryCacheService.delete(productId);

        log.info("Inventory deleted successfully for product {}", productId);
    }

    @Override
    @Transactional
    public InventoryResponse reserveStock(UUID productId,
                                          ReserveStockRequest request) {

        log.info("Reserving {} units for product {}",
                request.getQuantity(),
                productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found for product : " + productId
                        ));

        validateInventoryForReservation(inventory, request.getQuantity());

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + request.getQuantity()
        );

        Inventory saved =
                inventoryRepository.save(inventory);

        InventoryResponse response =
                inventoryMapper.toResponse(saved);
        log.info("Reserved {} units for product {}",
                request.getQuantity(),
                productId);

        inventoryCacheService.save(response);

        return response;
    }


    private void validateInventoryForReservation(Inventory inventory,
                                                 Integer quantity) {

        if (inventory.getStatus() != InventoryStatus.ACTIVE) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }

        int available =
                inventory.getQuantity() -
                        inventory.getReservedQuantity();

        if (available < quantity) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }

    }


    @Override
    @Transactional
    public InventoryResponse releaseStock(UUID productId,
                                          ReleaseStockRequest request) {

        log.info("Releasing {} units for product {}",
                request.getQuantity(),
                productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found for product : " + productId
                        ));

        validateInventoryForRelease(inventory, request.getQuantity());

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - request.getQuantity()
        );

        Inventory saved =
                inventoryRepository.save(inventory);

        InventoryResponse response =
                inventoryMapper.toResponse(saved);
        log.info("Released {} units for product {}",
                request.getQuantity(),
                productId);

        inventoryCacheService.save(response);

        return response;

    }


    private void validateInventoryForRelease(Inventory inventory,
                                             Integer quantity) {

        if (inventory.getStatus() != InventoryStatus.ACTIVE) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }

        if (inventory.getReservedQuantity() < quantity) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }
    }


    @Override
    @Transactional
    public InventoryResponse deductStock(UUID productId,
                                         DeductStockRequest request) {

        log.info("Deducting {} units for product {}",
                request.getQuantity(),
                productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found for product : " + productId
                        ));

        validateInventoryForDeduction(inventory, request.getQuantity());

        inventory.setQuantity(
                inventory.getQuantity() - request.getQuantity()
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - request.getQuantity()
        );

        Inventory saved =
                inventoryRepository.save(inventory);
        log.info("Deducted {} units for product {}",
                request.getQuantity(),
                productId);

        InventoryResponse response =
                inventoryMapper.toResponse(saved);

        inventoryCacheService.save(response);

        return response;
    }

    private void validateInventoryForDeduction(Inventory inventory,
                                               Integer quantity) {

        if (inventory.getStatus() != InventoryStatus.ACTIVE) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }

        if (inventory.getReservedQuantity() < quantity) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }
    }


    @Override
    @Transactional
    public InventoryResponse restoreStock(UUID productId,
                                          RestoreStockRequest request) {

        log.info("Restoring {} units for product {}",
                request.getQuantity(),
                productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Inventory not found for product : " + productId
                        ));

        validateActiveInventory(inventory);

        inventory.setQuantity(
                inventory.getQuantity() + request.getQuantity()
        );

        Inventory saved =
                inventoryRepository.save(inventory);
        log.info("Successfully restored {} units for product {}",
                request.getQuantity(),
                productId);

        InventoryResponse response =
                inventoryMapper.toResponse(saved);

        inventoryCacheService.save(response);

        return response;
    }

    private void validateActiveInventory(Inventory inventory) {

        if (inventory.getStatus() != InventoryStatus.ACTIVE) {

            throw new BusinessException(
                    ErrorCode.BUSINESS_VALIDATION_FAILED, HttpStatus.MULTI_STATUS,
                    "Inventory is not active"
            );
        }
    }

    @Override
    @Transactional
    public void release(UUID productId, Integer quantity) {

        ReleaseStockRequest request =
                new ReleaseStockRequest();

        request.setQuantity(quantity);

        releaseStock(productId, request);
    }

}