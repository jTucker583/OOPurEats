package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InProgressStateTest {

    private InProgressState inProgressState;
    private OrderContext mockContext;

    @BeforeEach
    void setUp() {
        inProgressState = new InProgressState();
        mockContext = mock(OrderContext.class);
        inProgressState.setContext(mockContext);
    }

    @Test
    @DisplayName("Should return INPROGRESS status")
    void testGetStatus() {
        assertEquals(OrderStatus.INPROGRESS, inProgressState.getStatus());
    }

    @Test
    @DisplayName("Should allow progression")
    void testCanProgress() {
        assertTrue(inProgressState.canProgress());
    }

    @Test
    @DisplayName("Should not allow cancellation")
    void testCanCancel() {
        assertFalse(inProgressState.canCancel());
    }

    @Test
    @DisplayName("Progress should transition to DoneState")
    void testProgress() {
        inProgressState.progress();

        verify(mockContext).transitionTo(any(DoneState.class));
    }

    @Test
    @DisplayName("Progress should throw exception when cannot progress")
    void testProgressWhenNotAllowed() {
        InProgressState spyState = spy(inProgressState);
        when(spyState.canProgress()).thenReturn(false);

        assertThrows(IllegalStateException.class, () ->
            spyState.progress());
    }

    @Test
    @DisplayName("Cancel should throw UnsupportedOperationException")
    void testCancel() {
        assertThrows(UnsupportedOperationException.class, () ->
            inProgressState.cancel());
    }

    @Test
    @DisplayName("Cancel should throw exception when cannot cancel")
    void testCancelWhenNotAllowed() {
        // Since canCancel() returns false by default, this should throw IllegalStateException
        assertThrows(IllegalStateException.class, () ->
            inProgressState.cancel());
    }

    @Test
    @DisplayName("Should return appropriate state description")
    void testGetStateDescription() {
        String description = inProgressState.getStateDescription();

        assertNotNull(description);
        assertTrue(description.toLowerCase().contains("prepared") ||
                  description.toLowerCase().contains("preparing"));
        assertTrue(description.toLowerCase().contains("done"));
    }

    @Test
    @DisplayName("Context should be set and retrieved correctly")
    void testContextSetterGetter() {
        OrderContext newContext = mock(OrderContext.class);
        inProgressState.setContext(newContext);

        assertEquals(newContext, inProgressState.getContext());
    }
}
