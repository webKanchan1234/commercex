package com.commercex.order.dto.response;

import com.commercex.order.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private UUID id;

    private String orderNumber;

    private UUID customerId;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private String currency;

    private List<OrderItemResponse> items;

    private Instant createdAt;

}
