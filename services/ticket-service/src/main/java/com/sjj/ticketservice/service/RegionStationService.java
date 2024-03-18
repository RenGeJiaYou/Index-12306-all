package com.sjj.ticketservice.service;

import com.sjj.ticketservice.dto.req.RegionStationQueryReqDTO;
import com.sjj.ticketservice.dto.resp.RegionStationQueryRespDTO;

import java.util.List;

/**
 * 地区&车站接口层
 *
 * @author Island_World
 */

public interface RegionStationService {
    /**
     * 查询车站&城市站点集合信息
     *
     * @param req
     * @return
     */
    List<RegionStationQueryRespDTO> regionStationQueryList(RegionStationQueryReqDTO req);
}
