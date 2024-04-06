package com.sjj.aggregateservice;

import cn.crane4j.spring.boot.annotation.EnableCrane4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "com.sjj.userservice",
        "com.sjj.ticketservice",
        "com.sjj.orderservice",
        "com.sjj.payservice",
        "com.sjj.gatewayservice"

})
@MapperScan(value = {
        "com.sjj.userservice.dao.mapper",
        "com.sjj.ticketservice.dao.mapper",
        "com.sjj.orderservice.dao.mapper",
        "com.sjj.payservice.dao.mapper",
})
@EnableFeignClients(value = {
        "com.sjj.ticketservice.remote",
        "com.sjj.orderservice.remote",
})
@EnableCrane4j()
public class AggregateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregateServiceApplication.class, args);
    }

}
