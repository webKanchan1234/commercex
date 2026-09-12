package com.commercex.order.mapper;

import com.commercex.order.dto.request.CreateOrderRequest;
import com.commercex.order.dto.request.OrderItemRequest;
import com.commercex.order.dto.response.OrderItemResponse;
import com.commercex.order.dto.response.OrderResponse;
import com.commercex.order.entity.Order;
import com.commercex.order.entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-13T01:19:04+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(CreateOrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.currency( request.getCurrency() );
        order.customerId( request.getCustomerId() );
        order.items( orderItemRequestListToOrderItemList( request.getItems() ) );

        return order.build();
    }

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.createdAt( order.getCreatedAt() );
        orderResponse.currency( order.getCurrency() );
        orderResponse.customerId( order.getCustomerId() );
        orderResponse.id( order.getId() );
        orderResponse.items( orderItemListToOrderItemResponseList( order.getItems() ) );
        orderResponse.orderNumber( order.getOrderNumber() );
        orderResponse.status( order.getStatus() );
        orderResponse.totalAmount( order.getTotalAmount() );

        return orderResponse.build();
    }

    @Override
    public OrderItem toEntity(OrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.price( request.getPrice() );
        orderItem.productId( request.getProductId() );
        orderItem.productName( request.getProductName() );
        orderItem.quantity( request.getQuantity() );

        return orderItem.build();
    }

    @Override
    public OrderItemResponse toResponse(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderItemResponse.builder();

        orderItemResponse.id( item.getId() );
        orderItemResponse.price( item.getPrice() );
        orderItemResponse.productId( item.getProductId() );
        orderItemResponse.productName( item.getProductName() );
        orderItemResponse.quantity( item.getQuantity() );
        orderItemResponse.subtotal( item.getSubtotal() );

        return orderItemResponse.build();
    }

    protected List<OrderItem> orderItemRequestListToOrderItemList(List<OrderItemRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemRequest orderItemRequest : list ) {
            list1.add( toEntity( orderItemRequest ) );
        }

        return list1;
    }

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toResponse( orderItem ) );
        }

        return list1;
    }
}
