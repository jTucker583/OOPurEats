package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.repository.OrderDetailsRepository;

public class PizzaRestaurant extends Restaurant {
    
    public PizzaRestaurant(OrderDetailsRepository orderDetailsRepository) {
        super(1L, "Mario's Pizzeria", "Italian", orderDetailsRepository);
    }
    
    @Override
    public ObserverEvents getRestaurantType() {
        return ObserverEvents.PIZZA_RESTAURANT;
    }
    
    @Override
    protected OrderItem createUniqueItem() {
        // Pizza restaurants add free garlic bread
        return new OrderItem(null, "Free Garlic Bread", 0.0, "Complimentary garlic bread with your pizza order", 1);
    }
}

