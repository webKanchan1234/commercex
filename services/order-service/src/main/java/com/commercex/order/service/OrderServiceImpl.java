package com.commercex.order.service;

import com.commercex.common.exception.ErrorCode;
import com.commercex.common.exception.ResourceNotFoundException;
import com.commercex.order.client.InventoryClient;
import com.commercex.order.dto.client.InventoryReleaseRequest;
import com.commercex.order.dto.client.InventoryReserveRequest;
import com.commercex.order.dto.client.InventoryReserveResponse;
import com.commercex.order.dto.request.CreateOrderRequest;
import com.commercex.order.dto.request.UpdateOrderStatusRequest;
import com.commercex.order.dto.response.OrderResponse;
import com.commercex.order.entity.Order;
import com.commercex.order.entity.OrderItem;
import com.commercex.order.entity.OrderStatus;
import com.commercex.order.mapper.OrderMapper;
import com.commercex.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryClient inventoryClient;

    // =========================================================
    // CREATE ORDER
    // =========================================================

    @Override
    public OrderResponse create(CreateOrderRequest request) {

        log.info(
                "Creating order for customer {}",
                request.getCustomerId()
        );

        // -----------------------------------------------------
        // 1. Create Order Entity
        // -----------------------------------------------------

        Order order = orderMapper.toEntity(request);

        order.setId(UUID.randomUUID());

        order.setOrderNumber(generateOrderNumber());

        order.setStatus(OrderStatus.PENDING);

        // -----------------------------------------------------
        // 2. Prepare Order Items
        // -----------------------------------------------------

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {

            item.setId(UUID.randomUUID());

            item.setOrder(order);

            BigDecimal subtotal =
                    item.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getQuantity()
                                    )
                            );

            item.setSubtotal(subtotal);

            totalAmount =
                    totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        // -----------------------------------------------------
        // 3. Save Order
        // -----------------------------------------------------

        Order savedOrder =
                orderRepository.save(order);

        log.info(
                "Order created. orderId={}, orderNumber={}",
                savedOrder.getId(),
                savedOrder.getOrderNumber()
        );

        // -----------------------------------------------------
        // 4. Reserve Inventory
        // -----------------------------------------------------

        List<OrderItem> reservedItems =
                new ArrayList<>();

        try {

            for (OrderItem item : savedOrder.getItems()) {

                InventoryReserveRequest reserveRequest =
                        InventoryReserveRequest.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .build();

                log.info(
                        "Reserving inventory. orderId={}, productId={}, quantity={}",
                        savedOrder.getId(),
                        item.getProductId(),
                        item.getQuantity()
                );

                InventoryReserveResponse response =
                        inventoryClient.reserve(
                                reserveRequest
                        );

                // -------------------------------------------------
                // Reservation failed
                // -------------------------------------------------

                if (response == null || !response.isSuccess()) {

                    String message =
                            response != null
                                    ? response.getMessage()
                                    : "Inventory service returned empty response";

                    throw new IllegalStateException(
                            "Inventory reservation failed for product "
                                    + item.getProductId()
                                    + ": "
                                    + message
                    );
                }

                // -------------------------------------------------
                // Reservation successful
                // -------------------------------------------------

                reservedItems.add(item);

                log.info(
                        "Inventory reserved successfully. orderId={}, productId={}, quantity={}",
                        savedOrder.getId(),
                        item.getProductId(),
                        item.getQuantity()
                );
            }

            // -----------------------------------------------------
            // 5. All Inventory Reservations Successful
            // -----------------------------------------------------

            savedOrder.setStatus(
                    OrderStatus.INVENTORY_RESERVED
            );

            savedOrder =
                    orderRepository.save(savedOrder);

            log.info(
                    "Inventory reserved for entire order. orderId={}",
                    savedOrder.getId()
            );

        } catch (Exception ex) {

            // -----------------------------------------------------
            // 6. Reservation Failed
            // -----------------------------------------------------

            log.error(
                    "Inventory reservation failed. orderId={}. Starting compensation.",
                    savedOrder.getId(),
                    ex
            );

            // -----------------------------------------------------
            // 7. Release Previously Reserved Inventory
            // -----------------------------------------------------

            compensateInventory(
                    reservedItems
            );

            // -----------------------------------------------------
            // 8. Cancel Order
            // -----------------------------------------------------

            savedOrder.setStatus(
                    OrderStatus.CANCELLED
            );

            orderRepository.save(savedOrder);

            log.info(
                    "Order cancelled after inventory failure. orderId={}",
                    savedOrder.getId()
            );

            throw new IllegalStateException(
                    "Unable to reserve inventory for order "
                            + savedOrder.getOrderNumber(),
                    ex
            );
        }

        // -----------------------------------------------------
        // 9. Return Successful Order
        // -----------------------------------------------------

        return orderMapper.toResponse(savedOrder);
    }

    // =========================================================
    // INVENTORY COMPENSATION
    // =========================================================

    private void compensateInventory(
            List<OrderItem> reservedItems) {

        if (reservedItems.isEmpty()) {

            log.info(
                    "No inventory compensation required."
            );

            return;
        }

        log.info(
                "Starting inventory compensation. items={}",
                reservedItems.size()
        );

        for (OrderItem item : reservedItems) {

            try {

                InventoryReleaseRequest releaseRequest =
                        InventoryReleaseRequest.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .build();

                log.info(
                        "Releasing inventory. productId={}, quantity={}",
                        item.getProductId(),
                        item.getQuantity()
                );

                inventoryClient.release(
                        releaseRequest
                );

                log.info(
                        "Inventory compensation successful. productId={}, quantity={}",
                        item.getProductId(),
                        item.getQuantity()
                );

            } catch (Exception ex) {

                /*
                 * IMPORTANT:
                 *
                 * Do NOT hide this failure.
                 *
                 * The order is already in a failed state,
                 * but inventory may still remain reserved.
                 *
                 * Later we will replace this with:
                 *
                 * Outbox + Kafka + Retry + DLQ
                 */

                log.error(
                        "CRITICAL: Inventory compensation failed. " +
                                "productId={}, quantity={}",
                        item.getProductId(),
                        item.getQuantity(),
                        ex
                );
            }
        }
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    public OrderResponse getById(UUID id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order not found : " + id
                                )
                        );

        return orderMapper.toResponse(order);
    }

    // =========================================================
    // GET BY ORDER NUMBER
    // =========================================================

    @Override
    public OrderResponse getByOrderNumber(
            String orderNumber) {

        Order order =
                orderRepository.findByOrderNumber(orderNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order not found : "
                                                + orderNumber
                                )
                        );

        return orderMapper.toResponse(order);
    }

    // =========================================================
    // GET CUSTOMER ORDERS
    // =========================================================

    @Override
    public Page<OrderResponse> getCustomerOrders(
            UUID customerId,
            Pageable pageable) {

        return orderRepository
                .findByCustomerId(
                        customerId,
                        pageable
                )
                .map(orderMapper::toResponse);
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    @Override
    public OrderResponse updateStatus(
            UUID id,
            UpdateOrderStatusRequest request) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order not found : " + id
                                )
                        );

        order.setStatus(request.getStatus());

        Order updatedOrder =
                orderRepository.save(order);

        return orderMapper.toResponse(
                updatedOrder
        );
    }

    // =========================================================
    // CANCEL
    // =========================================================

    @Override
    public void cancel(UUID id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "Order not found : " + id
                                )
                        );

        order.setStatus(
                OrderStatus.CANCELLED
        );

        orderRepository.save(order);

        log.info(
                "Order cancelled. orderId={}",
                id
        );
    }

    // =========================================================
    // ORDER NUMBER
    // =========================================================

    private String generateOrderNumber() {

        return "ORD-" + System.currentTimeMillis();
    }

    // =========================================================
    // CALCULATE TOTAL
    // =========================================================

    private BigDecimal calculateTotal(
            Order order) {

        return order.getItems()
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}