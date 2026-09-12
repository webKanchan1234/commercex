package com.commercex.inventory.mapper;

import com.commercex.inventory.dto.request.CreateInventoryRequest;
import com.commercex.inventory.dto.response.InventoryResponse;
import com.commercex.inventory.entity.Inventory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-13T01:19:03+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public Inventory toEntity(CreateInventoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Inventory.InventoryBuilder inventory = Inventory.builder();

        inventory.productId( request.getProductId() );
        inventory.quantity( request.getQuantity() );
        inventory.warehouse( request.getWarehouse() );

        inventory.reservedQuantity( 0 );
        inventory.status( com.commercex.inventory.enums.InventoryStatus.ACTIVE );

        return inventory.build();
    }

    @Override
    public InventoryResponse toResponse(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        InventoryResponse.InventoryResponseBuilder inventoryResponse = InventoryResponse.builder();

        inventoryResponse.createdAt( inventory.getCreatedAt() );
        inventoryResponse.id( inventory.getId() );
        inventoryResponse.productId( inventory.getProductId() );
        inventoryResponse.quantity( inventory.getQuantity() );
        inventoryResponse.reservedQuantity( inventory.getReservedQuantity() );
        inventoryResponse.status( inventory.getStatus() );
        inventoryResponse.updatedAt( inventory.getUpdatedAt() );
        inventoryResponse.warehouse( inventory.getWarehouse() );

        inventoryResponse.availableQuantity( inventory.getQuantity() - inventory.getReservedQuantity() );

        return inventoryResponse.build();
    }
}
