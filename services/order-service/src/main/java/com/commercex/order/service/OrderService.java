package com.commercex.order.service;

import com.commercex.order.dto.request.CreateOrderRequest;
import com.commercex.order.dto.request.UpdateOrderStatusRequest;
import com.commercex.order.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    OrderResponse create(CreateOrderRequest request);

    OrderResponse getById(UUID id);

    OrderResponse getByOrderNumber(String orderNumber);

    Page<OrderResponse> getCustomerOrders(
            UUID customerId,
            Pageable pageable);

    OrderResponse updateStatus(
            UUID id,
            UpdateOrderStatusRequest request);

    void cancel(UUID id);

}
