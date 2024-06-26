package com.sjj.ticketservice.service;

import java.util.List;

/**
 * 列车车座接口层
 *
 * @author Island_World
 */
public interface SeatService {
    /**
     * 获取列车车厢中可用的座位的座位号的集合
     *
     * @param trainId        列车 ID
     * @param carriageNumber 车厢号
     * @return 指定车厢的可用座位的座位号集合
     */
    List<String> listAvailableSeat(String trainId, String carriageNumber);

    /**
     * 获取列车车厢余票集合
     *
     * @param trainId           列车 ID
     * @param departure         出发站
     * @param arrival           到达站
     * @param trainCarriageList 车厢编号集合
     * @return 指定若干个车厢中，每节车厢的余票数量
     */
    List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList);
}
