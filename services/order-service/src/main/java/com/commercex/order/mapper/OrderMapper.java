package com.commercex.order.mapper;

import com.commercex.order.dto.request.CreateOrderRequest;
import com.commercex.order.dto.request.OrderItemRequest;
import com.commercex.order.dto.response.OrderItemResponse;
import com.commercex.order.dto.response.OrderResponse;
import com.commercex.order.entity.Order;
import com.commercex.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(CreateOrderRequest request);

    OrderResponse toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem toEntity(OrderItemRequest request);

    OrderItemResponse toResponse(OrderItem item);
}
