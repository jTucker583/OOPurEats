package com.denverdebuggets.oopureats.state;

import com.denverdebuggets.oopureats.model.OrderStatus;

// factory for order state instances
public class OrderStateFactory {

    public static OrderState createState(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("OrderStatus cannot be null");
        }

        return switch (status) {
            case ORDERED -> new OrderedState();
            case INPROGRESS -> new InProgressState();
            case DONE -> new DoneState();
            case DELIVERED -> new DeliveredState();
            case CANCELED-> new CanceledState();
            default -> throw new IllegalArgumentException("Unsupported OrderStatus: " + status);
        };
    }

    public static OrderContext createOrderContext(Long orderId, OrderStatus initialStatus) {
        OrderState initialState = createState(initialStatus);
        return new OrderContext(orderId, initialState);
    }

    public static OrderContext createNewOrderContext(Long orderId) {
        return createOrderContext(orderId, OrderStatus.ORDERED);
    }
}
