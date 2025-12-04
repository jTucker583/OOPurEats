package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveredStateTest {

    private DeliveredState deliveredState;
    private OrderContext mockContext;

    @BeforeEach
    void setUp() {
        deliveredState = new DeliveredState();
        mockContext = mock(OrderContext.class);
        deliveredState.setContext(mockContext);
    }

    @Test
    @DisplayName("Should return DELIVERED status")
    void testGetStatus() {
        assertEquals(OrderStatus.DELIVERED, deliveredState.getStatus());
    }

    @Test
    @DisplayName("Should not allow progression (final state)")
    void testCanProgress() {
        assertFalse(deliveredState.canProgress());
    }

    @Test
    @DisplayName("Should not allow cancellation")
    void testCanCancel() {
        assertFalse(deliveredState.canCancel());
    }

    @Test
    @DisplayName("Progress should throw exception (final state)")
    void testProgress() {
        assertThrows(IllegalStateException.class, () ->
            deliveredState.progress());
    }

    @Test
    @DisplayName("Cancel should throw exception (delivered order)")
    void testCancel() {
        assertThrows(IllegalStateException.class, () ->
            deliveredState.cancel());
    }

    @Test
    @DisplayName("Should return appropriate state description")
    void testGetStateDescription() {
        String description = deliveredState.getStateDescription();

        assertNotNull(description);
        assertTrue(description.toLowerCase().contains("delivered"));
        assertTrue(description.toLowerCase().contains("final"));
    }

    @Test
    @DisplayName("Context should be set and retrieved correctly")
    void testContextSetterGetter() {
        OrderContext newContext = mock(OrderContext.class);
        deliveredState.setContext(newContext);

        assertEquals(newContext, deliveredState.getContext());
    }
}
