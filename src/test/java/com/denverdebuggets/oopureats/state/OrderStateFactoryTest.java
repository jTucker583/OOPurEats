package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class OrderStateFactoryTest {

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("Should create correct state for each OrderStatus")
    void testCreateStateForAllStatuses(OrderStatus status) {
        OrderState state = OrderStateFactory.createState(status);

        assertNotNull(state);
        assertEquals(status, state.getStatus());
    }

    @Test
    @DisplayName("Should create InProgressState for INPROGRESS status")
    void testCreateInProgressState() {
        OrderState state = OrderStateFactory.createState(OrderStatus.INPROGRESS);

        assertInstanceOf(InProgressState.class, state);
        assertEquals(OrderStatus.INPROGRESS, state.getStatus());
    }

    @Test
    @DisplayName("Should throw exception for null status")
    void testCreateStateWithNull() {
        assertThrows(IllegalArgumentException.class, () ->
            OrderStateFactory.createState(null));
    }

    @Test
    @DisplayName("Should create OrderContext with correct initial state")
    void testCreateOrderContext() {
        Long orderId = 12345L;
        OrderStatus initialStatus = OrderStatus.INPROGRESS;

        OrderContext context = OrderStateFactory.createOrderContext(orderId, initialStatus);

        assertNotNull(context);
        assertEquals(orderId, context.getOrderId());
        assertEquals(initialStatus, context.getCurrentStatus());
        assertInstanceOf(InProgressState.class, context.getCurrentState());
    }

    @Test
    @DisplayName("Should create new OrderContext with ORDERED status")
    void testCreateNewOrderContext() {
        Long orderId = 67890L;

        OrderContext context = OrderStateFactory.createNewOrderContext(orderId);

        assertNotNull(context);
        assertEquals(orderId, context.getOrderId());
        assertEquals(OrderStatus.ORDERED, context.getCurrentStatus());
        assertInstanceOf(OrderedState.class, context.getCurrentState());
    }

    @Test
    @DisplayName("Should throw exception when creating context with null status")
    void testCreateOrderContextWithNullStatus() {
        Long orderId = 12345L;

        assertThrows(IllegalArgumentException.class, () ->
            OrderStateFactory.createOrderContext(orderId, null));
    }
}
