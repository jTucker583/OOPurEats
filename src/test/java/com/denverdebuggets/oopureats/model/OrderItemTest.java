package com.denverdebuggets.oopureats.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    @Test
    void testConstructorAndGetters() {
        OrderItem item = new OrderItem(null, "Burger", 10.0, "Tasty", 2);
        assertEquals("Burger", item.getName());
        assertEquals(10.0, item.getPrice());
        assertEquals("Tasty", item.getDescription());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void testSetters() {
        OrderItem item = new OrderItem();
        item.setName("Fries");
        item.setPrice(5.0);
        item.setDescription("Crispy");
        item.setQuantity(3);
        assertEquals("Fries", item.getName());
        assertEquals(5.0, item.getPrice());
        assertEquals("Crispy", item.getDescription());
        assertEquals(3, item.getQuantity());
    }
}
