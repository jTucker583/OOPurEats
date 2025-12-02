package com.denverdebuggets.oopureats.dto;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.model.OrderStatus;
import java.time.LocalDateTime;

public class OrderDetailsDTO {
    
    private ObserverEvents restaurantType;
    private String orderItems;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;

    public OrderDetailsDTO(ObserverEvents restaurantType, String orderItems, 
                          Double totalAmount, OrderStatus status, 
                          LocalDateTime orderDate) {
        this.restaurantType = restaurantType;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public ObserverEvents getRestaurantType() {
        return restaurantType;
    }
    
    public void setRestaurantType(ObserverEvents restaurantType) {
        this.restaurantType = restaurantType;
    }
    
    public String getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
                "restaurantType=" + restaurantType +
                ", orderItems='" + orderItems + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                '}';
    }
}

