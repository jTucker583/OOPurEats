package com.denverdebuggets.oopureats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OoPurEatsApplication {


    public static void main(String[] args) {
        SpringApplication.run(OoPurEatsApplication.class, args);
    }

}