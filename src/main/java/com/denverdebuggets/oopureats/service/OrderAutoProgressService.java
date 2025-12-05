package com.denverdebuggets.oopureats.service;

import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderAutoProgressService {

    private static final Logger log = LoggerFactory.getLogger(OrderAutoProgressService.class);
    
    private final OrderDetailsService orderDetailsService;

    @Autowired
    public OrderAutoProgressService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @Scheduled(fixedRate = 10000) 
    public void autoProgressOrders() {
        try {
            List<OrderDetailsDTO> allOrders = orderDetailsService.getAllOrders();
            int progressedCount = 0;

            for (OrderDetailsDTO order : allOrders) {
                try {
                    if (orderDetailsService.canProgressOrder(order.getId())) {
                        orderDetailsService.progressOrder(order.getId());
                        progressedCount++;
                        log.debug("Auto-progressed order {} from {} to next state", 
                                order.getId(), order.getStatus());
                    }
                } catch (IllegalStateException e) {
                   
                    log.trace("Order {} cannot be progressed: {}", order.getId(), e.getMessage());
                } catch (Exception e) {
                    log.error("Error auto-progressing order {}: {}", order.getId(), e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Error in auto-progress service: {}", e.getMessage(), e);
        }
    }
}

