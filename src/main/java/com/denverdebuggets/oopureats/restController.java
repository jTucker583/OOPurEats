package com.denverdebuggets.oopureats;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class restController {
    private static final Logger log = LoggerFactory.getLogger(restController.class);

    @GetMapping("/getOrder")
    public void  getOrder(){
        log.info("getOrder");
    }

    @GetMapping("/getAllOrders")
    public void  getAllOrders(){
        log.info("getAllOrders");
    }

    @PostMapping("/createOrder")
    public void createOrder(){
        log.info("createOrder");
    }

    @DeleteMapping("/deleteOrder")
    public void deleteOrder(){
        log.info("deleteOrder");
    }

}
