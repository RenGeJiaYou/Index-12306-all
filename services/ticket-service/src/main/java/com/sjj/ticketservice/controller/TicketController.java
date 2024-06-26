package com.sjj.ticketservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.sjj.ticketservice.dto.resp.TicketPageQueryRespDTO;
import com.sjj.ticketservice.service.TicketService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 车票控制层
 *
 * @author Island_World
 */

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    /**
     * 根据条件查询车票
     */
    @GetMapping("/api/ticket-service/ticket/query")
    public Result<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO req){
        return Results.success(ticketService.pageListTicketQuery(req));
    }

    /**
     * 购买车票
     */
    @PostMapping("api/ticket-service/ticket/purchase")
    public Result<String>purchaseTickets(@RequestBody PurchaseTicketReqDTO req){
        return Results.success(ticketService.purchaseTickets(req));
    }
}
