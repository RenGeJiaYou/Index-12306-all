package com.sjj.ticketservice.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 车票订单详情创建请求参数
 *
 * @author Island_World
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketOrderCreateRemoteReqDTO {
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
    private List<TicketOrderItemCreateRemoteReqDTO> ticketOrderItems;
}
