package com.sjj.orderservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 车票订单详情创建请求参数，和 {@link com.sjj.ticketservice.remote.dto.TicketOrderCreateRemoteReqDTO TicketOrderCreateRemoteReqDTO} 保持一致
 *
 * @author Island_World
 */

@Data
public class TicketOrderCreateReqDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 车次 ID
     */
    private Long trainId;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 订单来源
     */
    private Integer source;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单明细
     */
    private List<TicketOrderItemCreateReqDTO> ticketOrderItems;
}
