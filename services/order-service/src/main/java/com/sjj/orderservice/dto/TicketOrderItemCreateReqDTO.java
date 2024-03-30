package com.sjj.orderservice.dto;

import lombok.Data;

/**
 * 车票订单创建请求参数，和 {@link com.sjj.ticketservice.remote.dto.TicketOrderItemCreateRemoteReqDTO }保持一致
 *
 * @author Island_World
 */

@Data
public class TicketOrderItemCreateReqDTO {

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 订单金额
     */
    private Integer amount;
}
