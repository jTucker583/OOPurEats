package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class OrderDetails implements Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObserverEvents restaurantType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    public OrderDetails() {
        this.orderDate = LocalDateTime.now();
    }

    public OrderDetails(ObserverEvents restaurantType, List<OrderItem> orderItems) {
        this.restaurantType = restaurantType;
        this.orderItems = orderItems;
        this.orderDate = LocalDateTime.now();
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public double getTotalAmount() {
        if (orderItems == null) return 0.0;
        return orderItems.stream().mapToDouble(Item::getPrice).sum();
    }

    @Override
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        String itemsStr = orderItems != null ? orderItems.toString() : "[]";
        return "OrderDetails{" +
                "id=" + id +
                ", restaurantType=" + restaurantType +
                ", orderItems=" + itemsStr +
                ", totalAmount=" + getTotalAmount() +
                ", status=" + status +
                ", orderDate=" + orderDate +
                '}';
    }
}
