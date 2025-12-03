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
    private final ObserverEvents restaurantType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private final List<OrderItem> orderItems;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private final LocalDateTime orderDate;

    // JPA requires a no-args constructor
    public OrderDetails() {
        this.restaurantType = null;
        this.orderItems = null;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.ORDERED;
    }

    private OrderDetails(Builder builder) {
        this.id = builder.id;
        this.restaurantType = builder.restaurantType;
        this.orderItems = builder.orderItems;
        this.status = builder.status != null ? builder.status : OrderStatus.ORDERED;
        this.orderDate = builder.orderDate != null ? builder.orderDate : LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    // Setter for id (needed for JPA)
    public void setId(Long id) {
        this.id = id;
    }

    public ObserverEvents getRestaurantType() {
        return restaurantType;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
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

    // Setter for status (needed for JPA and status changes)
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getOrderDate() {
        return orderDate;
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

    public static class Builder {
        private Long id;
        private ObserverEvents restaurantType;
        private List<OrderItem> orderItems;
        private OrderStatus status;
        private LocalDateTime orderDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder restaurantType(ObserverEvents restaurantType) {
            this.restaurantType = restaurantType;
            return this;
        }
        public Builder orderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }
        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }
        public Builder orderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }
        public OrderDetails build() {
            return new OrderDetails(this);
        }
    }
}
