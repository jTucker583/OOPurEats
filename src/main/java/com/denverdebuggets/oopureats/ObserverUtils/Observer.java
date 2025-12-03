package com.denverdebuggets.oopureats.ObserverUtils;

import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;

public interface Observer {
    void update(OrderDetailsDTO orderDTO);
}
