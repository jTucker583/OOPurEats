package com.denverdebuggets.oopureats.service;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;
import com.denverdebuggets.oopureats.model.OrderDetails;
import com.denverdebuggets.oopureats.model.OrderStatus;
import com.denverdebuggets.oopureats.repository.OrderDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrderDetailsService {
    
    private static final Logger log = LoggerFactory.getLogger(OrderDetailsService.class);
    
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderStateMachineService stateMachineService;

    @Autowired
    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository,
                             OrderStateMachineService stateMachineService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.stateMachineService = stateMachineService;
    }
    

    public OrderDetailsDTO createOrder(OrderDetailsDTO orderDTO) {
        log.info("Creating new order");
        OrderDetails order = convertToEntity(orderDTO);
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.ORDERED);
        }

        OrderDetails savedOrder = orderDetailsRepository.save(order);
        log.info("Order created with ID: {} and initial status: {}",
                savedOrder.getId(), savedOrder.getStatus());
        return convertToDTO(savedOrder);
    }

    public Optional<OrderDetailsDTO> getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        return orderDetailsRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<OrderDetailsDTO> getAllOrders() {
        log.info("Fetching all orders");
        return orderDetailsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    

    public List<OrderDetailsDTO> getOrdersByRestaurantType(ObserverEvents restaurantType) {
        log.info("Fetching orders for restaurant type: {}", restaurantType);
        return orderDetailsRepository.findByRestaurantType(restaurantType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDetailsDTO> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        return orderDetailsRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDetailsDTO> updateOrder(Long id, OrderDetailsDTO orderDTO) {
        log.info("Updating order with ID: {}", id);
        
        return orderDetailsRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(orderDTO.getStatus());
                    OrderDetails updatedOrder = orderDetailsRepository.save(existingOrder);
                    log.info("Order updated successfully: {}", id);
                    return convertToDTO(updatedOrder);
                });
    }

    public Optional<OrderDetailsDTO> progressOrder(Long id) {
        log.info("Progressing order with ID: {}", id);

        return orderDetailsRepository.findById(id)
                .map(order -> {
                    try {
                        OrderStatus newStatus = stateMachineService.progressOrder(order);
                        OrderDetails updatedOrder = orderDetailsRepository.save(order);
                        log.info("Order {} progressed successfully to status: {}", id, newStatus);
                        return convertToDTO(updatedOrder);
                    } catch (IllegalStateException e) {
                        log.error("Cannot progress order {}: {}", id, e.getMessage());
                        throw e;
                    }
                });
    }

    public Optional<OrderDetailsDTO> cancelOrder(Long id) {
        log.info("Attempting to cancel order with ID: {}", id);
        return orderDetailsRepository.findById(id)
                .map(order -> {
                    try {
                        stateMachineService.cancelOrder(order);
                        OrderDetails updatedOrder = orderDetailsRepository.save(order);
                        log.info("Order {} canceled successfully. New status: {}", id, order.getStatus());
                        return convertToDTO(updatedOrder);
                    } catch (IllegalStateException e) {
                        log.error("Cannot cancel order {}: {}", id, e.getMessage());
                        throw e;
                    }
                });
    }

    public boolean canProgressOrder(Long id) {
        return orderDetailsRepository.findById(id)
                .map(order -> {
                    boolean canProgress = stateMachineService.canProgressOrder(order);
                    log.debug("Order {} can progress: {}", id, canProgress);
                    return canProgress;
                })
                .orElse(false);
    }

    public boolean canCancelOrder(Long id) {
        return orderDetailsRepository.findById(id)
                .map(order -> {
                    boolean canCancel = stateMachineService.canCancelOrder(order);
                    log.debug("Order {} can cancel: {}", id, canCancel);
                    return canCancel;
                })
                .orElse(false);
    }

    public Optional<String> getOrderStateDescription(Long id) {
        return orderDetailsRepository.findById(id)
                .map(order -> {
                    String description = stateMachineService.getOrderStateDescription(order);
                    log.debug("Order {} state description: {}", id, description);
                    return description;
                });
    }

    public boolean deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        
        if (orderDetailsRepository.existsById(id)) {
            orderDetailsRepository.deleteById(id);
            log.info("Order deleted successfully: {}", id);
            return true;
        }
        log.warn("Order not found for deletion: {}", id);
        return false;
    }

    private OrderDetailsDTO convertToDTO(OrderDetails order) {
        return new OrderDetailsDTO(
                order.getId(),
                order.getRestaurantType(),
                order.getOrderItems(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );
    }

    private OrderDetails convertToEntity(OrderDetailsDTO orderDTO) {
        OrderDetails.Builder builder = OrderDetails.builder()
            .restaurantType(orderDTO.getRestaurantType())
            .orderItems(orderDTO.getOrderItems());
        if (orderDTO.getStatus() != null) {
            builder.status(orderDTO.getStatus());
        }
        if (orderDTO.getOrderDate() != null) {
            builder.orderDate(orderDTO.getOrderDate());
        }
        return builder.build();
    }
}
