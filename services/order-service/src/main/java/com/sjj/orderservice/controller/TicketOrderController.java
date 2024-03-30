package com.sjj.orderservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.orderservice.dto.TicketOrderCreateReqDTO;
import com.sjj.orderservice.service.OrderService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制层
 *
 * @author Island_World
 */
@RestController
@RequiredArgsConstructor
public class TicketOrderController {
    private final OrderService orderService;

    /**
     * 车票订单创建，此处和 ticket-service 的 remote 接口定义的 url 相同，方便它调用本接口
     */
    @PostMapping("/api/order-service/order/ticket/create")
    public Result<String> createTicketOrder(@RequestBody TicketOrderCreateReqDTO req){
        return Results.success(orderService.createTicketOrder(req));
    }

}
