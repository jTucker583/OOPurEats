package com.denverdebuggets.oopureats;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.ObserverUtils.OrderBus;
import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;
import com.denverdebuggets.oopureats.model.OrderStatus;
import com.denverdebuggets.oopureats.service.OrderDetailsService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class restController {
    private static final Logger log = LoggerFactory.getLogger(restController.class);
    
    private final OrderDetailsService orderDetailsService;
    private final OrderBus orderBus;
    
    @Autowired
    public restController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
        this.orderBus = OrderBus.getInstance();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable Long id) {
        log.info("Getting order with ID: {}", id);
        Optional<OrderDetailsDTO> order = orderDetailsService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailsDTO>> getAllOrders() {
        log.info("Getting all orders");
        List<OrderDetailsDTO> orders = orderDetailsService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    

    @GetMapping("/restaurant/{restaurantType}")
    public ResponseEntity<List<OrderDetailsDTO>> getOrdersByRestaurant(@PathVariable String restaurantType) {
        log.info("Getting orders for restaurant type: {}", restaurantType);
        try {
            ObserverEvents restaurantEvent = ObserverEvents.valueOf(restaurantType.toUpperCase());
            List<OrderDetailsDTO> orders = orderDetailsService.getOrdersByRestaurantType(restaurantEvent);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid restaurant type: {}", restaurantType);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDetailsDTO>> getOrdersByStatus(@PathVariable String status) {
        log.info("Getting orders with status: {}", status);
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<OrderDetailsDTO> orders = orderDetailsService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status: {}", status);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderDetailsDTO> createOrder(@RequestBody OrderDetailsDTO orderDTO) {
        log.info("Creating new order via OrderBus for restaurant type: {}", orderDTO.getRestaurantType());
        
        // Notify OrderBus which will notify the appropriate restaurant to create the order
        // The restaurant creates the order and returns it
       orderBus.notifyObservers(orderDTO.getRestaurantType(), orderDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailsDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDetailsDTO orderDTO) {
        log.info("Updating order with ID: {}", id);
        Optional<OrderDetailsDTO> updatedOrder = orderDetailsService.updateOrder(id, orderDTO);
        return updatedOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // State Machine Integration Methods

    /**
     * Progress an order to the next state using the state machine
     */
    @PutMapping("/{id}/progress")
    public ResponseEntity<OrderDetailsDTO> progressOrder(@PathVariable Long id) {
        log.info("Progressing order with ID: {}", id);
        try {
            return orderDetailsService.progressOrder(id)
                    .map(order -> {
                        log.info("Order {} progressed successfully to status: {}", id, order.getStatus());
                        return ResponseEntity.ok(order);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            log.warn("Cannot progress order {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancel an order using the state machine
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDetailsDTO> cancelOrder(@PathVariable Long id) {
        log.info("Attempting to cancel order with ID: {}", id);
        try {
            return orderDetailsService.cancelOrder(id)
                    .map(order -> {
                        log.info("Order {} canceled successfully. New status: {}", id, order.getStatus());
                        return ResponseEntity.ok(order);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            log.warn("Cannot cancel order {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Check if an order can be progressed to the next state
     */
    @GetMapping("/{id}/can-progress")
    public ResponseEntity<Boolean> canProgressOrder(@PathVariable Long id) {
        log.debug("Checking if order {} can be progressed", id);
        boolean canProgress = orderDetailsService.canProgressOrder(id);
        return ResponseEntity.ok(canProgress);
    }

    /**
     * Check if an order can be canceled
     */
    @GetMapping("/{id}/can-cancel")
    public ResponseEntity<Boolean> canCancelOrder(@PathVariable Long id) {
        log.debug("Checking if order {} can be canceled", id);
        boolean canCancel = orderDetailsService.canCancelOrder(id);
        return ResponseEntity.ok(canCancel);
    }

    /**
     * Get the current state description and available actions for an order
     */
    @GetMapping("/{id}/state")
    public ResponseEntity<String> getOrderStateDescription(@PathVariable Long id) {
        log.debug("Getting state description for order {}", id);
        return orderDetailsService.getOrderStateDescription(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update order status directly (bypasses state machine validation)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDetailsDTO> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        // to be implemented if needed
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order with ID: {}", id);
        boolean deleted = orderDetailsService.deleteOrder(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
