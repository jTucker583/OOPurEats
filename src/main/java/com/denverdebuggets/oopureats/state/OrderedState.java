package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;

/**
 * Concrete State representing an order that has been placed but not yet started
 */
public class OrderedState extends OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.ORDERED;
    }

    @Override
    public void progress() {
        if (!canProgress()) {
            throw new IllegalStateException("Cannot progress from ORDERED state");
        }
        context.transitionTo(new InProgressState());
    }

    @Override
    public void cancel() {
        if (!canCancel()) {
            throw new IllegalStateException("Cannot cancel from ORDERED state");
        }
        context.transitionTo(new CanceledState());
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public boolean canProgress() {
        // Orders can progress from ORDERED to IN_PROGRESS or be cancelled
        return true;
    }

    @Override
    public String getStateDescription() {
        return "Order has been placed and is waiting to be started. Can be cancelled or moved to in-progress.";
    }
}
