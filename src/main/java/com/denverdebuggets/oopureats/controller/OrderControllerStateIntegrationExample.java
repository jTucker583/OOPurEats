package com.denverdebuggets.oopureats.controller;

import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;
import com.denverdebuggets.oopureats.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Example controller methods showing how to use the integrated state machine functionality
 * Add these methods to your existing controller
 */
public class OrderControllerStateIntegrationExample {

    @Autowired
    private OrderDetailsService orderDetailsService;

    /**
     * Progress an order to the next state
     */
    @PutMapping("/orders/{id}/progress")
    public ResponseEntity<OrderDetailsDTO> progressOrder(@PathVariable Long id) {
        return orderDetailsService.progressOrder(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cancel an order
     */
    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<OrderDetailsDTO> cancelOrder(@PathVariable Long id) {
        try {
            return orderDetailsService.cancelOrder(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Check if an order can be progressed
     */
    @GetMapping("/orders/{id}/can-progress")
    public ResponseEntity<Boolean> canProgressOrder(@PathVariable Long id) {
        boolean canProgress = orderDetailsService.canProgressOrder(id);
        return ResponseEntity.ok(canProgress);
    }

    /**
     * Check if an order can be canceled
     */
    @GetMapping("/orders/{id}/can-cancel")
    public ResponseEntity<Boolean> canCancelOrder(@PathVariable Long id) {
        boolean canCancel = orderDetailsService.canCancelOrder(id);
        return ResponseEntity.ok(canCancel);
    }

    /**
     * Get order state information
     */
    @GetMapping("/orders/{id}/state")
    public ResponseEntity<String> getOrderStateDescription(@PathVariable Long id) {
        return orderDetailsService.getOrderStateDescription(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
