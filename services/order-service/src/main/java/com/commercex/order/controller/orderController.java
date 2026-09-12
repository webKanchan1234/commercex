package com.commercex.order.controller;

import com.commercex.order.dto.request.CreateOrderRequest;
import com.commercex.order.dto.request.UpdateOrderStatusRequest;
import com.commercex.order.dto.response.OrderResponse;
import com.commercex.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
class OrderController {

    private final OrderService orderService;

    // =========================================================
    // CREATE ORDER
    // =========================================================

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.create(request));
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                orderService.getById(id)
        );
    }

    // =========================================================
    // GET BY ORDER NUMBER
    // =========================================================

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getByOrderNumber(
            @PathVariable String orderNumber) {

        return ResponseEntity.ok(
                orderService.getByOrderNumber(orderNumber)
        );
    }

    // =========================================================
    // GET CUSTOMER ORDERS
    // =========================================================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderResponse>> getCustomerOrders(
            @PathVariable UUID customerId,
            Pageable pageable) {

        return ResponseEntity.ok(
                orderService.getCustomerOrders(
                        customerId,
                        pageable
                )
        );
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        return ResponseEntity.ok(
                orderService.updateStatus(
                        id,
                        request
                )
        );
    }

    // =========================================================
    // CANCEL
    // =========================================================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(
            @PathVariable UUID id) {

        orderService.cancel(id);
    }
}