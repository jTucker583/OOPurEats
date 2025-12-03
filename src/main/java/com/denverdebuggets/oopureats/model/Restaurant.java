package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.Observer;
import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;
import com.denverdebuggets.oopureats.repository.OrderDetailsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Restaurant implements Observer {
    protected Long id;
    protected String name;
    protected String cuisineType;
    protected OrderDetailsRepository orderDetailsRepository;
    
    public Restaurant(Long id, String name, String cuisineType, OrderDetailsRepository orderDetailsRepository) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.orderDetailsRepository = orderDetailsRepository;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCuisineType() {
        return cuisineType;
    }
    
    public abstract ObserverEvents getRestaurantType();
    
    protected abstract OrderItem createUniqueItem();
    
    public OrderDetails createOrder(List<OrderItem> orderItems) {
        List<OrderItem> itemsWithUnique = new ArrayList<>(orderItems);
        
 
        OrderItem uniqueItem = createUniqueItem();
        itemsWithUnique.add(uniqueItem);
        
        OrderDetails order = OrderDetails.builder()
                .restaurantType(getRestaurantType())
                .orderItems(itemsWithUnique)
                .status(OrderStatus.ORDERED)
                .orderDate(LocalDateTime.now())
                .build();
        
        if (orderDetailsRepository != null) {
            return orderDetailsRepository.save(order);
        }
        
        return order;
    }
    

    @Override
    public void update(OrderDetailsDTO orderDTO) {
        if (orderDTO.getRestaurantType() == getRestaurantType()) {
            createOrder(orderDTO.getOrderItems());
        }
    }
}
