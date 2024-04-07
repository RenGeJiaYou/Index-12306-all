package com.sjj.ticketservice.dto.domain;

import java.util.List;

/**
 * 高铁座位基础信息
 *
 * @author Island_World
 */

public class TrainSeatBaseDTO {
    /**
     * 高铁列车 ID
     */
    private String trainId;

    /**
     * 列车起始站点
     */
    private String departure;

    /**
     * 列车到达站点
     */
    private String arrival;

    /**
     * 乘客信息
     */
    private List<PurchaseTicketPassengerDetailDTO> passengerSeatDetails;

    /**
     * 选择座位信息
     */
    private List<String> chooseSeatList;

}
