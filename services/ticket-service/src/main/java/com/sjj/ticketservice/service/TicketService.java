package com.sjj.ticketservice.service;

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
    TicketPageQueryRespDTO pageListTicketQuery(TicketPageQueryReqDTO req);

    /**
     * 1.购买车票，根据策略模式选特定等级的车座 2.新增车票记录 3.远程调用 Order 服务创建订单
     *
     * @param req 车票购买请求参数
     * @return 订单号
     */
    String purchaseTickets(@RequestBody PurchaseTicketReqDTO req);
}
