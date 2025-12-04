package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderedStateTest {

    private OrderedState orderedState;
    private OrderContext mockContext;

    @BeforeEach
    void setUp() {
        orderedState = new OrderedState();
        mockContext = mock(OrderContext.class);
        orderedState.setContext(mockContext);
    }

    @Test
    @DisplayName("Should return ORDERED status")
    void testGetStatus() {
        assertEquals(OrderStatus.ORDERED, orderedState.getStatus());
    }

    @Test
    @DisplayName("Should allow progression")
    void testCanProgress() {
        assertTrue(orderedState.canProgress());
    }

    @Test
    @DisplayName("Should allow cancellation")
    void testCanCancel() {
        assertTrue(orderedState.canCancel());
    }

    @Test
    @DisplayName("Progress should transition to InProgressState")
    void testProgress() {
        orderedState.progress();

        verify(mockContext).transitionTo(any(InProgressState.class));
    }

    @Test
    @DisplayName("Progress should throw exception when cannot progress")
    void testProgressWhenNotAllowed() {
        // Mock canProgress to return false
        OrderedState spyState = spy(orderedState);
        when(spyState.canProgress()).thenReturn(false);

        assertThrows(IllegalStateException.class, () ->
            spyState.progress());
    }

    @Test
    @DisplayName("Cancel should throw UnsupportedOperationException")
    void testCancel() {
        assertThrows(UnsupportedOperationException.class, () ->
            orderedState.cancel());
    }

    @Test
    @DisplayName("Cancel should throw exception when cannot cancel")
    void testCancelWhenNotAllowed() {
        OrderedState spyState = spy(orderedState);
        when(spyState.canCancel()).thenReturn(false);

        assertThrows(IllegalStateException.class, () ->
            spyState.cancel());
    }

    @Test
    @DisplayName("Should return appropriate state description")
    void testGetStateDescription() {
        String description = orderedState.getStateDescription();

        assertNotNull(description);
        assertTrue(description.toLowerCase().contains("placed"));
        assertTrue(description.toLowerCase().contains("cancel"));
    }

    @Test
    @DisplayName("Context should be set and retrieved correctly")
    void testContextSetterGetter() {
        OrderContext newContext = mock(OrderContext.class);
        orderedState.setContext(newContext);

        assertEquals(newContext, orderedState.getContext());
    }
}
