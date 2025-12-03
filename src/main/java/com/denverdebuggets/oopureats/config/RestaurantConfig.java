package com.denverdebuggets.oopureats.config;

import com.denverdebuggets.oopureats.ObserverUtils.OrderBus;
import com.denverdebuggets.oopureats.model.PastaRestaurant;
import com.denverdebuggets.oopureats.model.PizzaRestaurant;
import com.denverdebuggets.oopureats.model.SteakRestaurant;
import com.denverdebuggets.oopureats.repository.OrderDetailsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfig {
    
    private static final Logger log = LoggerFactory.getLogger(RestaurantConfig.class);
    
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderBus orderBus;
    
    public RestaurantConfig(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderBus = OrderBus.getInstance();
    }
    
    @PostConstruct
    public void registerRestaurantsWithOrderBus() {
        
        PizzaRestaurant pizzaRestaurant = new PizzaRestaurant(orderDetailsRepository);
        orderBus.registerObserver(pizzaRestaurant.getRestaurantType(), pizzaRestaurant);
        
        SteakRestaurant steakRestaurant = new SteakRestaurant(orderDetailsRepository);
        orderBus.registerObserver(steakRestaurant.getRestaurantType(), steakRestaurant);

        PastaRestaurant pastaRestaurant = new PastaRestaurant(orderDetailsRepository);
        orderBus.registerObserver(pastaRestaurant.getRestaurantType(), pastaRestaurant);
    }
}

