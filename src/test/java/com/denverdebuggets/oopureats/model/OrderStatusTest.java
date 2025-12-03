package com.denverdebuggets.oopureats.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {
    @Test
    void testEnumValues() {
        assertEquals(OrderStatus.ORDERED, OrderStatus.valueOf("ORDERED"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.valueOf("DELIVERED"));
        assertEquals(OrderStatus.INPROGRESS, OrderStatus.valueOf("INPROGRESS"));
        assertEquals(OrderStatus.DONE, OrderStatus.valueOf("DONE"));
    }

    @Test
    void testEnumToString() {
        assertEquals("ORDERED", OrderStatus.ORDERED.toString());
        assertEquals("DELIVERED", OrderStatus.DELIVERED.toString());
        assertEquals("INPROGRESS", OrderStatus.INPROGRESS.toString());
        assertEquals("DONE", OrderStatus.DONE.toString());
    }
}
