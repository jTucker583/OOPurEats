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


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order with ID: {}", id);
        boolean deleted = orderDetailsService.deleteOrder(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
