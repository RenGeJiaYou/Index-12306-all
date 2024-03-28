package com.sjj.ticketservice.service.handler.ticket;

import com.sjj.ticketservice.common.enums.VehicleSeatTypeEnum;
import com.sjj.ticketservice.common.enums.VehicleTypeEnum;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.service.handler.ticket.base.AbstractTrainPurchaseTicketTemplate;
import com.sjj.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 高铁商务座购票组件
 *
 * @author Island_World
 */

public class TrainBusinessClassPurchaseTicketHandler extends AbstractTrainPurchaseTicketTemplate {
    @Override
    public String mark() {
        return VehicleTypeEnum.HIGH_SPEED_RAIN.getName() + VehicleSeatTypeEnum.BUSINESS_CLASS.getName();
    }

    @Override
    protected List<TrainPurchaseTicketRespDTO> selectSeats(PurchaseTicketReqDTO req) {
        return null;
    }

    private static List<String> selectSeats(List<String> availableSeats, int requirePassengers) {
        List<String> selectedSeats = new ArrayList<>();
        for (String seat : availableSeats) {
            if (selectedSeats.size() >= requirePassengers) {
                break;
            }
            selectedSeats.add(seat);
        }
        return selectedSeats;
    }
}
