package com.denverdebuggets.oopureats.service;

import com.denverdebuggets.oopureats.model.OrderDetails;
import com.denverdebuggets.oopureats.model.OrderStatus;
import com.denverdebuggets.oopureats.state.OrderContext;
import com.denverdebuggets.oopureats.state.OrderStateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Service class that manages order state machines for OrderDetails entities
 */
@Service
public class OrderStateMachineService {

    private static final Logger log = LoggerFactory.getLogger(OrderStateMachineService.class);
    private final Map<Long, OrderContext> orderContexts = new ConcurrentHashMap<>();

    public OrderContext getOrderContext(OrderDetails orderDetails) {
        if (orderDetails == null || orderDetails.getId() == null) {
            throw new IllegalArgumentException("OrderDetails and its ID cannot be null");
        }

        return orderContexts.computeIfAbsent(orderDetails.getId(), 
            id -> OrderStateFactory.createOrderContext(id, orderDetails.getStatus()));
    }

    public OrderStatus progressOrder(OrderDetails orderDetails) {
        log.info("Progressing order ID {} from status {}",
            orderDetails.getId(), orderDetails.getStatus());
        OrderContext context = getOrderContext(orderDetails);
        context.progress();
        
        // Update the OrderDetails entity with the new status
        OrderStatus newStatus = context.getCurrentStatus();
        orderDetails.setStatus(newStatus);
        
        return newStatus;
    }

    public void cancelOrder(OrderDetails orderDetails) {
        log.info("Attempting to cancel order ID {} from status {}",
            orderDetails.getId(), orderDetails.getStatus());
        OrderContext context = getOrderContext(orderDetails);
        context.cancel();
        // Update the OrderDetails entity with the new status
        OrderStatus newStatus = context.getCurrentStatus();
        orderDetails.setStatus(newStatus);
    }

    public boolean canProgressOrder(OrderDetails orderDetails) {
        OrderContext context = getOrderContext(orderDetails);
        return context.canProgress();
    }

    public boolean canCancelOrder(OrderDetails orderDetails) {
        OrderContext context = getOrderContext(orderDetails);
        return context.canCancel();
    }

    public String getOrderStateDescription(OrderDetails orderDetails) {
        OrderContext context = getOrderContext(orderDetails);
        return context.getStateDescription();
    }

    public void removeOrderContext(OrderDetails orderDetails) {
        if (orderDetails != null && orderDetails.getId() != null) {
            orderContexts.remove(orderDetails.getId());
        }
    }

    // if the OrderDetails entity status has changed externally, synchronize the state machine context
    public void synchronizeState(OrderDetails orderDetails) {
        if (orderDetails == null || orderDetails.getId() == null) {
            return;
        }

        OrderContext existingContext = orderContexts.get(orderDetails.getId());
        if (existingContext == null || !existingContext.getCurrentStatus().equals(orderDetails.getStatus())) {
            // Recreate the context with the current status from the entity
            orderContexts.put(orderDetails.getId(), 
                OrderStateFactory.createOrderContext(orderDetails.getId(), orderDetails.getStatus()));
        }
    }
}
