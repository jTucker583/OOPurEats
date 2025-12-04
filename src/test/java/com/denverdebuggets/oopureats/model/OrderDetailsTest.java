package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderDetailsTest {
    @Test
    void testConstructorAndGetters() {
        OrderItem item1 = new OrderItem(null, "Burger", 10.0, "cooked cow with a bun and optional cheese", 2);
        OrderItem item2 = new OrderItem(null, "Fries", 5.0, "super awesome french fries", 1);
        OrderDetails order = OrderDetails.builder()
            .restaurantType(ObserverEvents.PIZZA_RESTAURANT)
            .orderItems(List.of(item1, item2))
            .build();
        assertEquals(ObserverEvents.PIZZA_RESTAURANT, order.getRestaurantType());
        assertEquals(2, order.getOrderItems().size());
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testSetters() {
        OrderDetails order = new OrderDetails();
        order.setId(123L);
        order.setStatus(OrderStatus.DELIVERED);
        LocalDateTime now = LocalDateTime.now();
        // restaurantType and orderDate are final and set via builder only
        assertEquals(123L, order.getId());
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void testGetTotalAmount() {
        OrderItem item1 = new OrderItem(null, "Burger", 10.0, "cooked cow with a bun and optional cheese", 2);
        OrderItem item2 = new OrderItem(null, "Fries", 5.0, "super awesome french fries", 1);
        OrderDetails order = OrderDetails.builder()
            .restaurantType(ObserverEvents.PIZZA_RESTAURANT)
            .orderItems(List.of(item1, item2))
            .build();
        assertEquals(15.0, order.getTotalAmount());
    }

    @Test
    void testToString() {
        OrderDetails order = new OrderDetails();
        System.out.println(order);
        assertTrue(order.toString().contains("OrderDetails"));
    }
}
