package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class OrderContextTest {

    private OrderContext orderContext;
    private OrderState mockState;
    private Long orderId;

    @BeforeEach
    void setUp() {
        orderId = 12345L;
        mockState = new OrderedState();
        orderContext = new OrderContext(orderId, mockState);
    }

    @Test
    @DisplayName("Constructor should initialize with provided state and order ID")
    void testConstructor() {
        assertEquals(orderId, orderContext.getOrderId());
        assertEquals(OrderStatus.ORDERED, orderContext.getCurrentStatus());
        assertNotNull(orderContext.getCurrentState());
    }

    @Test
    @DisplayName("Constructor should throw exception for null state")
    void testConstructorWithNullState() {
        assertThrows(IllegalArgumentException.class, () ->
            new OrderContext(orderId, null));
    }

    @Test
    @DisplayName("TransitionTo should update current state")
    void testTransitionTo() {
        InProgressState newState = new InProgressState();
        orderContext.transitionTo(newState);

        assertEquals(OrderStatus.INPROGRESS, orderContext.getCurrentStatus());
        assertEquals(newState, orderContext.getCurrentState());
        assertEquals(orderContext, newState.getContext());
    }

    @Test
    @DisplayName("TransitionTo should throw exception for null state")
    void testTransitionToWithNull() {
        assertThrows(IllegalArgumentException.class, () ->
            orderContext.transitionTo(null));
    }

    @Test
    @DisplayName("Progress should delegate to current state when allowed")
    void testProgressSuccess() {
        // Start with ORDERED state, should progress to IN_PROGRESS
        assertTrue(orderContext.canProgress());
        orderContext.progress();
        assertEquals(OrderStatus.INPROGRESS, orderContext.getCurrentStatus());
    }

    @Test
    @DisplayName("Progress should throw exception when not allowed")
    void testProgressNotAllowed() {
        // Move to DELIVERED state (final state)
        orderContext.transitionTo(new InProgressState());
        orderContext.progress(); // INPROGRESS -> DONE
        orderContext.progress(); // DONE -> DELIVERED

        assertFalse(orderContext.canProgress());
        assertThrows(IllegalStateException.class, () ->
            orderContext.progress());
    }

    @Test
    @DisplayName("Should return correct state description")
    void testGetStateDescription() {
        String description = orderContext.getStateDescription();
        assertNotNull(description);
        assertTrue(description.contains("Order has been placed"));
    }

    @Test
    @DisplayName("Should complete full state progression")
    void testCompleteStateProgression() {
        // Start: ORDERED
        assertEquals(OrderStatus.ORDERED, orderContext.getCurrentStatus());
        assertTrue(orderContext.canProgress());

        // ORDERED -> IN_PROGRESS
        orderContext.progress();
        assertEquals(OrderStatus.INPROGRESS, orderContext.getCurrentStatus());
        assertTrue(orderContext.canProgress());

        // IN_PROGRESS -> DONE
        orderContext.progress();
        assertEquals(OrderStatus.DONE, orderContext.getCurrentStatus());
        assertTrue(orderContext.canProgress());

        // DONE -> DELIVERED
        orderContext.progress();
        assertEquals(OrderStatus.DELIVERED, orderContext.getCurrentStatus());
        assertFalse(orderContext.canProgress());
    }
}
