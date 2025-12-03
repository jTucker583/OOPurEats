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
    
    @Autowired
    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }
    

    public OrderDetailsDTO createOrder(OrderDetailsDTO orderDTO) {
        log.info("Creating new order");
        
        OrderDetails order = convertToEntity(orderDTO);
        OrderDetails savedOrder = orderDetailsRepository.save(order);
        
        log.info("Order created with ID: {}", savedOrder.getId());
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
                    existingOrder.setRestaurantType(orderDTO.getRestaurantType());
                    existingOrder.setOrderItems(orderDTO.getOrderItems());
                    existingOrder.setStatus(orderDTO.getStatus());
                    
                    OrderDetails updatedOrder = orderDetailsRepository.save(existingOrder);
                    log.info("Order updated successfully: {}", id);
                    return convertToDTO(updatedOrder);
                });
    }


    public Optional<OrderDetailsDTO> updateOrderStatus(Long id, OrderStatus status) {
        log.info("Updating order status for ID: {} to status: {}", id, status);
        
        return orderDetailsRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    OrderDetails updatedOrder = orderDetailsRepository.save(order);
                    return convertToDTO(updatedOrder);
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
                order.getRestaurantType(),
                order.getOrderItems(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );
    }

    private OrderDetails convertToEntity(OrderDetailsDTO orderDTO) {
        OrderDetails order = new OrderDetails(
                orderDTO.getRestaurantType(),
                orderDTO.getOrderItems()
        );
        
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }
        if (orderDTO.getOrderDate() != null) {
            order.setOrderDate(orderDTO.getOrderDate());
        }
        
        return order;
    }
}

