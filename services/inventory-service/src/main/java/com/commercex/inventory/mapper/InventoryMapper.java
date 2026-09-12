package com.commercex.inventory.mapper;

import com.commercex.inventory.dto.request.CreateInventoryRequest;
import com.commercex.inventory.dto.response.InventoryResponse;
import com.commercex.inventory.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservedQuantity", constant = "0")
    @Mapping(target = "status", expression = "java(com.commercex.inventory.enums.InventoryStatus.ACTIVE)")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Inventory toEntity(CreateInventoryRequest request);

    @Mapping(
            target = "availableQuantity",
            expression = "java(inventory.getQuantity() - inventory.getReservedQuantity())"
    )
    InventoryResponse toResponse(Inventory inventory);

}