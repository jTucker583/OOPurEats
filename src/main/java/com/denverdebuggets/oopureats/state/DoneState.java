package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;

/**
 * Concrete State representing an order that has been prepared and is ready for delivery
 */
public class DoneState extends OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DONE;
    }

    @Override
    public void progress() {
        if (!canProgress()) {
            throw new IllegalStateException("Cannot progress from DONE state");
        }
        context.transitionTo(new DeliveredState());
    }

    @Override
    public void cancel() {
        if (!canCancel()) {
            throw new IllegalStateException("Cannot cancel from DONE state");
        }
        // Orders that are done but not yet delivered might be cancellable
        // but would require special handling (food might be wasted, etc.)
        throw new UnsupportedOperationException("Cancellation of completed orders requires special handling");
    }

    @Override
    public boolean canCancel() {
        // Typically, completed orders cannot be cancelled
        // They might be refunded or returned, but that's a different process
        return false;
    }

    @Override
    public boolean canProgress() {
        // Orders can progress from DONE to DELIVERED when they are picked up/delivered
        return true;
    }

    @Override
    public String getStateDescription() {
        return "Order preparation is complete and ready for delivery. Can be moved to delivered.";
    }
}
