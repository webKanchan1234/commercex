package com.commercex.inventory.mapper;

import com.commercex.inventory.dto.request.CreateInventoryRequest;
import com.commercex.inventory.dto.response.InventoryResponse;
import com.commercex.inventory.entity.Inventory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-12T23:07:01+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Homebrew)"
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

        inventoryResponse.id( inventory.getId() );
        inventoryResponse.productId( inventory.getProductId() );
        inventoryResponse.quantity( inventory.getQuantity() );
        inventoryResponse.reservedQuantity( inventory.getReservedQuantity() );
        inventoryResponse.warehouse( inventory.getWarehouse() );
        inventoryResponse.status( inventory.getStatus() );
        inventoryResponse.createdAt( inventory.getCreatedAt() );
        inventoryResponse.updatedAt( inventory.getUpdatedAt() );

        inventoryResponse.availableQuantity( inventory.getQuantity() - inventory.getReservedQuantity() );

        return inventoryResponse.build();
    }
}
