package com.sjj.ticketservice.service;

import com.sjj.ticketservice.dto.resp.TrainStationQueryRespDTO;

import java.util.List;

/**
 * 列车经停站点接口层
 *
 * @author Island_World
 */

public interface TrainStationService {
    /**
     * 根据列车 ID 查询站点信息
     *
     * @param trainId 列车 ID
     * @return 列车经停站信息
     */
    List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId);
}
