package com.sjj.ticketservice.service.handler.ticket;

import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.ticketservice.common.enums.VehicleSeatTypeEnum;
import com.sjj.ticketservice.common.enums.VehicleTypeEnum;
import com.sjj.ticketservice.dto.domain.PassengerInfoDTO;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.service.CarriageService;
import com.sjj.ticketservice.service.SeatService;
import com.sjj.ticketservice.service.handler.ticket.base.AbstractTrainPurchaseTicketTemplate;
import com.sjj.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 高铁商务座购票组件
 *
 * @author Island_World
 */
@Component
@RequiredArgsConstructor
public class TrainBusinessClassPurchaseTicketHandler extends AbstractTrainPurchaseTicketTemplate {
    private final CarriageService carriageService;
    private final SeatService seatService;
    private final DistributedCache distributedCache;

    @Override
    public String mark() {
        return VehicleTypeEnum.HIGH_SPEED_RAIN.getName() + VehicleSeatTypeEnum.BUSINESS_CLASS.getName();
    }

    @Override
    protected List<TrainPurchaseTicketRespDTO> selectSeats(PurchaseTicketReqDTO req) {
        List<String> passengerIds = req.getPassengerIds();
        // 获取指定车次的指定类型座位所属的所有车厢号
        List<String> carriageNumberList = carriageService.listCarriageNumber(req.getTrainId(), req.getSeatType());
        // 根据获取的车厢号，查询每节车厢的余票数
        List<Integer> allCarriageRemainingTicket = seatService.listSeatRemainingTicket(req.getTrainId(), req.getDeparture(), req.getArrival(), carriageNumberList);
        // 尽量让一起买票的人在一个车厢(同车厢邻座 > 同车厢不邻座 > 不同车厢座位)
        String carriagesNumber;
        List<TrainPurchaseTicketRespDTO> actualResult = new ArrayList<>();
        // 遍历每一节车厢
        for (int i = 0; i < allCarriageRemainingTicket.size(); i++) {
            int currentCarriageRemainingTicketCount = allCarriageRemainingTicket.get(i);
            // 当前车厢装得下所有乘车人
            if (currentCarriageRemainingTicketCount > req.getPassengerIds().size()) {
                carriagesNumber = carriageNumberList.get(i);
                List<String> currentCarriageAvailableSeats = seatService.listAvailableSeat(req.getTrainId(), carriagesNumber);
                // selectedSeats.size() == passengerIds.size() 已选座位的数量和乘车人数量是一致的
                List<String> selectedSeats = selectSeats(currentCarriageAvailableSeats, req.getPassengerIds().size());
                for (int j = 0; j < selectedSeats.size(); j++) {
                    var result = new TrainPurchaseTicketRespDTO();
                    result.setSeatNumber(selectedSeats.get(j));
                    result.setCarriageNumber(carriagesNumber);
                    result.setPassengerInfo(new PassengerInfoDTO().setPassengerId(passengerIds.get(j)));
                    actualResult.add(result);
                }
                break;
            }
        }

        // TODO 如果一个车厢不满足乘客人数，需要进行拆分
        // 扣减车厢余票缓存，扣减站点余票缓存
        return actualResult;
    }

    /**
     * 当前车厢装得下所有乘车人时，为所有乘车人选座。
     *
     * @param availableSeats 当前车厢所有空座的座位号
     * @param passengerCount 乘车人的数量
     * @return 为所有乘客选择的座位列表
     */
    private static List<String> selectSeats(List<String> availableSeats, int passengerCount) {
        List<String> selectedSeats = new ArrayList<>();
        for (String seat : availableSeats) {
            if (selectedSeats.size() >= passengerCount) {
                break;
            }
            selectedSeats.add(seat);
        }
        return selectedSeats;
    }
}
