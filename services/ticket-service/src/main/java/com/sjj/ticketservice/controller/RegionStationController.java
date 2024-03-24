package com.sjj.ticketservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.ticketservice.dto.req.RegionStationQueryReqDTO;
import com.sjj.ticketservice.dto.resp.RegionStationQueryRespDTO;
import com.sjj.ticketservice.service.RegionStationService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区&车站查找控制层
 *
 * @author Island_World
 */

@RestController
@RequiredArgsConstructor
public class RegionStationController {
    private final RegionStationService regionStationService;

    /**
     * 查询车站&城市站点集合信息
     */
    @GetMapping("/api/ticket-service/region-station/query")
    public Result<List<RegionStationQueryRespDTO>> listTrainStationQuery(RegionStationQueryReqDTO req){
        return Results.success(regionStationService.regionStationQueryList(req));
    }
}
