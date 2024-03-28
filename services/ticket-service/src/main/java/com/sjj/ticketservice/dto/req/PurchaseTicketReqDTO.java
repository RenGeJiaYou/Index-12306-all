package com.sjj.ticketservice.dto.req;

import lombok.Data;

import java.util.List;

/**
 * 购票请求参数
 * <p>
 * 业务逻辑：
 * 「哪些乘车人」想买「哪个起止站点」的「哪趟车次」的「哪种座位类型」的「哪些车位」
 *
 * @author Island_World
 */
@Data
public class PurchaseTicketReqDTO {
    /**
     * 车次 ID
     */
    private String trainId;

    /**
     * 乘车人
     */
    private List<String> passengerIds;

    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 选择座位
     */
    private List<String> chooseSeats;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;
}
