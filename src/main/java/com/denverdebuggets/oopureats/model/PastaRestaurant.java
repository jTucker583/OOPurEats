package com.denverdebuggets.oopureats.model;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.repository.OrderDetailsRepository;

public class PastaRestaurant extends Restaurant {
    
    public PastaRestaurant(OrderDetailsRepository orderDetailsRepository) {
        super(3L, "Pasta House", "Italian", orderDetailsRepository);
    }
    
    @Override
    public ObserverEvents getRestaurantType() {
        return ObserverEvents.PASTA_RESTAURANT;
    }
    
    @Override
    protected OrderItem createUniqueItem() {
        return new OrderItem(null, "Free Breadsticks", 0.0, "Complimentary breadsticks with your pasta order", 1);
    }
}

