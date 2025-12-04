package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoneStateTest {

    private DoneState doneState;
    private OrderContext mockContext;

    @BeforeEach
    void setUp() {
        doneState = new DoneState();
        mockContext = mock(OrderContext.class);
        doneState.setContext(mockContext);
    }

    @Test
    @DisplayName("Should return DONE status")
    void testGetStatus() {
        assertEquals(OrderStatus.DONE, doneState.getStatus());
    }

    @Test
    @DisplayName("Should allow progression")
    void testCanProgress() {
        assertTrue(doneState.canProgress());
    }

    @Test
    @DisplayName("Should not allow cancellation")
    void testCanCancel() {
        assertFalse(doneState.canCancel());
    }

    @Test
    @DisplayName("Progress should transition to DeliveredState")
    void testProgress() {
        doneState.progress();

        verify(mockContext).transitionTo(any(DeliveredState.class));
    }

    @Test
    @DisplayName("Progress should throw exception when cannot progress")
    void testProgressWhenNotAllowed() {
        DoneState spyState = spy(doneState);
        when(spyState.canProgress()).thenReturn(false);

        assertThrows(IllegalStateException.class, () ->
            spyState.progress());
    }

    @Test
    @DisplayName("Cancel should throw UnsupportedOperationException")
    void testCancel() {
        assertThrows(UnsupportedOperationException.class, () ->
            doneState.cancel());
    }

    @Test
    @DisplayName("Cancel should throw exception when cannot cancel")
    void testCancelWhenNotAllowed() {
        // Since canCancel() returns false by default, this should throw IllegalStateException
        assertThrows(IllegalStateException.class, () ->
            doneState.cancel());
    }

    @Test
    @DisplayName("Should return appropriate state description")
    void testGetStateDescription() {
        String description = doneState.getStateDescription();

        assertNotNull(description);
        assertTrue(description.toLowerCase().contains("complete") ||
                  description.toLowerCase().contains("ready"));
        assertTrue(description.toLowerCase().contains("deliver"));
    }

    @Test
    @DisplayName("Context should be set and retrieved correctly")
    void testContextSetterGetter() {
        OrderContext newContext = mock(OrderContext.class);
        doneState.setContext(newContext);

        assertEquals(newContext, doneState.getContext());
    }
}
