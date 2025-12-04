package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import com.denverdebuggets.oopureats.state.OrderContext;

// abstract class all states will extend
public abstract class OrderState {

    protected OrderContext context;

    public void setContext(OrderContext context) {
        this.context = context;
    }

    public OrderContext getContext() {
        return context;
    }

    public abstract OrderStatus getStatus();

    public abstract void progress();

    public abstract void cancel();

    public abstract boolean canCancel();

    public abstract boolean canProgress();

    public abstract String getStateDescription();
}
