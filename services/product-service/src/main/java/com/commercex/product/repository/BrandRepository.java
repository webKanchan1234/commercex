package com.commercex.product.repository;

import com.commercex.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Optional<Brand> findBySlug(String slug);

    Optional<Brand> findByName(String name);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);

}