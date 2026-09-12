package com.commercex.inventory.entity;

import com.commercex.inventory.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "inventory",
        indexes = {
                @Index(
                        name = "idx_inventory_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(
            name = "product_id",
            nullable = false,
            unique = true
    )
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(
            name = "reserved_quantity",
            nullable = false
    )
    @Builder.Default
    private Integer reservedQuantity = 0;

    @Column(nullable = false)
    private String warehouse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;

    @Version
    private Long version;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

}