package com.denverdebuggets.oopureats.ObserverUtils;

public interface Observable {
    void registerObserver(ObserverEvents event,Observer observer);
    void unregisterObserver(ObserverEvents event, Observer observer);
    void notifyObservers(ObserverEvents event, String orderDetails);
}
