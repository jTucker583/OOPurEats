package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;

/**
 * Concrete State representing an order that is currently being prepared
 */
public class InProgressState extends OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.INPROGRESS;
    }

    @Override
    public void progress() {
        if (!canProgress()) {
            throw new IllegalStateException("Cannot progress from IN_PROGRESS state");
        }
        context.transitionTo(new DoneState());
    }

    @Override
    public void cancel() {
        if (!canCancel()) {
            throw new IllegalStateException("Cannot cancel from IN_PROGRESS state");
        }
        context.transitionTo(new CanceledState());
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public boolean canProgress() {
        // Orders can progress from IN_PROGRESS to DONE when preparation is complete, or to CANCELED if the user cancels
        return true;
    }

    @Override
    public String getStateDescription() {
        return "Order is currently being prepared. Can be moved to done when preparation is complete.";
    }
}
