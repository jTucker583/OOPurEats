package com.denverdebuggets.oopureats.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    @Test
    void testConstructorAndGetters() {
        OrderItem item = new OrderItem(null, "Burger", 10.0, "Super yummy burger", 2);
        assertEquals("Burger", item.getName());
        assertEquals(10.0, item.getPrice());
        assertEquals("Super yummy burger", item.getDescription());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void testSetters() {
        OrderItem item = new OrderItem();
        item.setName("Fries");
        item.setPrice(5.0);
        item.setDescription("sliced potatoes fried to perfection");
        item.setQuantity(3);
        assertEquals("Fries", item.getName());
        assertEquals(5.0, item.getPrice());
        assertEquals("sliced potatoes fried to perfection", item.getDescription());
        assertEquals(3, item.getQuantity());
    }
}
