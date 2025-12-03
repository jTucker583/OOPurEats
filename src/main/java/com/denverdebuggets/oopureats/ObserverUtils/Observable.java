package com.denverdebuggets.oopureats.ObserverUtils;

import com.denverdebuggets.oopureats.dto.OrderDetailsDTO;

public interface Observable {
    void registerObserver(ObserverEvents event,Observer observer);
    void unregisterObserver(ObserverEvents event, Observer observer);
    void notifyObservers(ObserverEvents event, OrderDetailsDTO orderDTO);
}
