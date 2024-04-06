package com.sjj.ticketservice.remote;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.ticketservice.remote.dto.TicketOrderCreateRemoteReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 车票订单远程服务调用
 * <p>
 * 等价于一个 Controller, 将由 FeignClient 动态生成代理类，并远程调用“index12306-order-service”订单服务
 *
 * @author Island_World
 */
@FeignClient(value = "order-service",url = "")
public interface TicketOrderRemoteService {
    @PostMapping("/api/order-service/order/ticket/create")
    Result<String> createTicketOrder(@RequestBody TicketOrderCreateRemoteReqDTO req);

}
