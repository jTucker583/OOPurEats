package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "order_details")
public class OrderDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObserverEvents restaurantType;
    
    @Column(nullable = false)
    private String orderItems; // JSON string or comma-separated items
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @Column(nullable = false)
    private LocalDateTime orderDate;

    public OrderDetails() {
        this.orderDate = LocalDateTime.now();
    }

    public OrderDetails(ObserverEvents restaurantType, String orderItems, 
                       Double totalAmount) {
        this();
        this.restaurantType = restaurantType;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.ORDERED;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
        return "OrderDetails{" +
                "id=" + id +
                ", restaurantType=" + restaurantType +
                ", orderItems='" + orderItems + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                '}';
    }
}

