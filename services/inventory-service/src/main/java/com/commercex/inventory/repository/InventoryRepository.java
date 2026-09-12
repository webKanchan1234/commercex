package com.commercex.inventory.repository;

import com.commercex.inventory.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Inventory> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);

}