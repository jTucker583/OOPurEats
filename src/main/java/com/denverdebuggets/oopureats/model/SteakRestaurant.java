package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.repository.OrderDetailsRepository;

public class SteakRestaurant extends Restaurant {
    
    public SteakRestaurant(OrderDetailsRepository orderDetailsRepository) {
        super(2L, "The Prime Cut", "Steakhouse", orderDetailsRepository);
    }
    
    @Override
    public ObserverEvents getRestaurantType() {
        return ObserverEvents.STEAK_RESTAURANT;
    }
    
    @Override
    protected OrderItem createUniqueItem() {
        return new OrderItem(null, "Free Side Dish", 0.0, "Complimentary side dish with your steak order", 1);
    }
}

