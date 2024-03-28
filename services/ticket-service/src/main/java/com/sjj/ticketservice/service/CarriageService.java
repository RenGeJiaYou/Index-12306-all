package com.sjj.ticketservice.service;

import java.util.List;

/**
 * 列车车厢接口层
 *
 * @author Island_World
 */

public interface CarriageService {
    /**
     * 依次从缓存 或 数据库中寻找指定车厢号集合
     *
     * @param trainId      列车 ID
     * @param carriageType 车厢类型
     * @return 指定车次的指定类型车厢的车厢号集合
     */
    List<String> listCarriageNumber(String trainId,Integer carriageType);
}
