package com.sjj.ticketservice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.commonspringbootstarter.toolkit.BeanUtil;
import com.sjj.ticketservice.dao.entity.TrainStationDO;
import com.sjj.ticketservice.dao.mapper.TrainStationMapper;
import com.sjj.ticketservice.dto.resp.TrainStationQueryRespDTO;
import com.sjj.ticketservice.service.TrainStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 列车经停站点查询实现层
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
public class TrainStationServiceImpl implements TrainStationService {
    private final TrainStationMapper trainStationMapper;

    @Override
    public List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId) {
        var wrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, trainId);
        List<TrainStationDO> trainStationList = trainStationMapper.selectList(wrapper);
        return BeanUtil.convert(trainStationList,TrainStationQueryRespDTO.class);
    }
}
