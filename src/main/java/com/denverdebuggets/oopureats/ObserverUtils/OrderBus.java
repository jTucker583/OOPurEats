package com.denverdebuggets.oopureats.ObserverUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBus implements Observable {
    private static OrderBus instance;
    private final HashMap<ObserverEvents, ArrayList<Observer>> observers;

    private OrderBus() {
        observers = new HashMap<>();
        for(ObserverEvents event : ObserverEvents.values()) {
            observers.put(event,  new ArrayList<>());
        }
    }

    public static OrderBus getInstance() {
        if (instance == null) {
            instance = new OrderBus();
        }
        return instance;
    }

    public void registerObserver(ObserverEvents event, Observer observer) {
        observers.get(event).add(observer);
    }
    public void unregisterObserver(ObserverEvents event, Observer observer) {
        observers.get(event).remove(observer);
    }
    public void notifyObservers(ObserverEvents event, String orderDetails) {
        for (Observer observer : observers.get(event)) {
            observer.update(orderDetails);
        }
    }


}
