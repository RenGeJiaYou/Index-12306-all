package com.sjj.orderservice.service;

import com.sjj.orderservice.dto.TicketOrderCreateReqDTO;

/**
 * 订单服务层
 *
 * @author Island_World
 */

public interface OrderService {
    /**
     * 创建车票订单
     *
     * @param req 商品订单入参
     * @return 订单号
     */
    String createTicketOrder(TicketOrderCreateReqDTO req);
}
