package com.sjj.ticketservice.service.handler.ticket.dto;

import com.sjj.ticketservice.dto.domain.PassengerInfoDTO;
import lombok.Data;

/**
 * 列车购票出参
 *
 * @author Island_World
 */
@Data
public class TrainPurchaseTicketRespDTO {
    /**
     * 乘车人信息
     */
    private PassengerInfoDTO passengerInfo;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 座位金额
     */
    private Integer amount;
}
