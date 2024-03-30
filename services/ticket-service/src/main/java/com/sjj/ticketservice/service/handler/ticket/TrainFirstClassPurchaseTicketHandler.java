package com.sjj.ticketservice.service.handler.ticket;

import com.sjj.ticketservice.common.enums.VehicleSeatTypeEnum;
import com.sjj.ticketservice.common.enums.VehicleTypeEnum;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.service.handler.ticket.base.AbstractTrainPurchaseTicketTemplate;
import com.sjj.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;

import java.util.List;

/**
 * 一等座
 *
 * @author Island_World
 */

public class TrainFirstClassPurchaseTicketHandler extends AbstractTrainPurchaseTicketTemplate {
    @Override
    public String mark() {
        return VehicleTypeEnum.HIGH_SPEED_RAIN.getName() + VehicleSeatTypeEnum.FIRST_CLASS.getName();
    }

    @Override
    protected List<TrainPurchaseTicketRespDTO> selectSeats(PurchaseTicketReqDTO req) {
        return null;
    }
}
