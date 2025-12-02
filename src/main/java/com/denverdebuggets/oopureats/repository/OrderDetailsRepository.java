package com.denverdebuggets.oopureats.repository;

import com.denverdebuggets.oopureats.ObserverUtils.ObserverEvents;
import com.denverdebuggets.oopureats.model.OrderDetails;
import com.denverdebuggets.oopureats.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    

    List<OrderDetails> findByRestaurantType(ObserverEvents restaurantType);

    List<OrderDetails> findByStatus(OrderStatus status);

    List<OrderDetails> findByRestaurantTypeAndStatus(ObserverEvents restaurantType, OrderStatus status);

    boolean existsById(Long id);
}

