package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds an order's current state and allows state transitions
 */
public class OrderContext {

    private static final Logger logger = LoggerFactory.getLogger(OrderContext.class);

    private OrderState state;
    private final Long orderId;


    public OrderContext(Long orderId, OrderState initialState) {
        this.orderId = orderId;
        this.transitionTo(initialState);
    }

    // Transitions the order to a new state
    protected void transitionTo(OrderState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }

        OrderStatus previousStatus = this.state != null ? this.state.getStatus() : null;
        logger.info("Order {}: Transition from {} to {}",
                   orderId,
                   previousStatus,
                   state.getStatus());

        this.state = state;
        this.state.setContext(this);
    }

    public OrderStatus getCurrentStatus() {
        return state.getStatus();
    }

    public OrderState getCurrentState() {
        return state;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void progress() {
        if (state.canProgress()) {
            state.progress();
        } else {
            logger.warn("Order {}: Cannot progress from status {}", orderId, state.getStatus());
            throw new IllegalStateException("Cannot progress order from current state: " + state.getStatus());
        }
    }

    public void cancel() {
        if (state.canCancel()) {
            state.cancel();
        } else {
            logger.warn("Order {}: Cannot cancel from status {}", orderId, state.getStatus());
            throw new IllegalStateException("Cannot cancel order from current state: " + state.getStatus());
        }
    }

    public boolean canCancel() {
        return state.canCancel();
    }

    public boolean canProgress() {
        return state.canProgress();
    }

    public String getStateDescription() {
        return state.getStateDescription();
    }
}
