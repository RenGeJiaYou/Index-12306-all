package com.sjj.aggregateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.sjj.userservice",
        "com.sjj.ticketservice",
        "com.sjj.orderservice",
        "com.sjj.payservice",
        "com.sjj.gatewayservice"

})
public class AggregateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregateServiceApplication.class, args);
    }

}
