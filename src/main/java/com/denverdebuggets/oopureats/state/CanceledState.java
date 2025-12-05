
package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;

/**
 * Concrete State representing an order that has been delivered to the customer
 */
public class CanceledState extends OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CANCELED;
    }

    @Override
    public void progress() {
        // This is the final state - no further progression possible
        throw new IllegalStateException("Cannot progress from DELIVERED state - this is the final state");
    }

    @Override
    public void cancel() {
        throw new IllegalStateException("Order is already canceled.");
    }

    @Override
    public boolean canCancel() {
        return false;
    }

    @Override
    public boolean canProgress() {
        // This is the final state
        return false;
    }

    @Override
    public String getStateDescription() {
        return "Order has been canceled. This is the final state.";
    }
}
