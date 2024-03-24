package com.sjj.ticketservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.ticketservice.dto.resp.TrainStationQueryRespDTO;
import com.sjj.ticketservice.service.TrainStationService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 列车途径车站查找控制层
 *
 * @author Island_World
 */
@RestController
@RequiredArgsConstructor
public class TrainStationController {
    private final TrainStationService trainStationService;

    /**
     * 根据列车 ID 查询站点信息
     */
    @GetMapping("/api/ticket-service/train-station/query")
    public Result<List<TrainStationQueryRespDTO>> listTrainStationQuery(String trainId){
        return Results.success(trainStationService.listTrainStationQuery(trainId));
    }
}
