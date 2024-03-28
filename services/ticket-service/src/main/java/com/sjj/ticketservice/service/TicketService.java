package com.sjj.ticketservice.service;

import com.sjj.conventionspringbootstarter.page.PageResponse;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.sjj.ticketservice.dto.resp.TicketPageQueryRespDTO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 车票服务接口
 *
 * @author Island_World
 */

public interface TicketService {
    /**
     * 按照条件分页查询余票
     *
     * @param req 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    PageResponse<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO req);

    /**
     * 购买车票
     *
     * @param req 车票购买请求参数
     * @return 订单号
     */
    String purchaseTickets(@RequestBody PurchaseTicketReqDTO req);
}
