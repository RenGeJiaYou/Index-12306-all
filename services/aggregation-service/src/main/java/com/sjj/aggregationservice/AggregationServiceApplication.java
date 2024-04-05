package com.sjj.aggregationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.sjj.userservice",
        "com.sjj.ticketservice",
        "com.sjj.orderservice",
        "com.sjj.payservice",
        "com.sjj.gatewayservice"
})
public class AggregationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregationServiceApplication.class, args);
    }

}
