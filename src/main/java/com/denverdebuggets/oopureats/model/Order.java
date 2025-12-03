package com.denverdebuggets.oopureats.model;

import java.time.LocalDateTime;
import java.util.List;

public interface Order {
    Long getId();
    List<OrderItem> getOrderItems();
    double getTotalAmount();
    LocalDateTime getOrderDate();
    OrderStatus getStatus();
}
