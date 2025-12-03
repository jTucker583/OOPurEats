package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderDetailsTest {
    @Test
    void testConstructorAndGetters() {
        OrderItem item1 = new OrderItem(null, "Burger", 10.0, "Tasty", 2);
        OrderItem item2 = new OrderItem(null, "Fries", 5.0, "Crispy", 1);
        OrderDetails order = new OrderDetails(ObserverEvents.PIZZA_RESTAURANT, List.of(item1, item2));
        assertEquals(ObserverEvents.PIZZA_RESTAURANT, order.getRestaurantType());
        assertEquals(2, order.getOrderItems().size());
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testSetters() {
        OrderDetails order = new OrderDetails();
        order.setId(123L);
        order.setRestaurantType(ObserverEvents.STEAK_RESTAURANT);
        order.setStatus(OrderStatus.DELIVERED);
        LocalDateTime now = LocalDateTime.now();
        order.setOrderDate(now);
        assertEquals(123L, order.getId());
        assertEquals(ObserverEvents.STEAK_RESTAURANT, order.getRestaurantType());
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertEquals(now, order.getOrderDate());
    }

    @Test
    void testGetTotalAmount() {
        OrderItem item1 = new OrderItem(null, "Burger", 10.0, "Tasty", 2);
        OrderItem item2 = new OrderItem(null, "Fries", 5.0, "Crispy", 1);
        OrderDetails order = new OrderDetails(ObserverEvents.PIZZA_RESTAURANT, List.of(item1, item2));
        assertEquals(15.0, order.getTotalAmount());
    }

    @Test
    void testToString() {
        OrderDetails order = new OrderDetails();
        System.out.println(order);
        assertTrue(order.toString().contains("OrderDetails"));
    }
}
