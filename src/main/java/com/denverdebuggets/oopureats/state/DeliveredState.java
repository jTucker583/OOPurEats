package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;

/**
 * Concrete State representing an order that has been delivered to the customer
 */
public class DeliveredState extends OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DELIVERED;
    }

    @Override
    public void progress() {
        // This is the final state - no further progression possible
        throw new IllegalStateException("Cannot progress from DELIVERED state - this is the final state");
    }

    @Override
    public void cancel() {
        // Cannot cancel a delivered order - would need to be handled as a return/refund
        throw new IllegalStateException("Cannot cancel a delivered order - use return/refund process instead");
    }

    @Override
    public boolean canCancel() {
        // Delivered orders cannot be cancelled
        return false;
    }

    @Override
    public boolean canProgress() {
        // This is the final state
        return false;
    }

    @Override
    public String getStateDescription() {
        return "Order has been delivered to the customer. This is the final state.";
    }
}
