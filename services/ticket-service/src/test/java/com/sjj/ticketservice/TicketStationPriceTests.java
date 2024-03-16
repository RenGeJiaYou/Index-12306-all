package com.sjj.ticketservice;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.ticketservice.dao.entity.TrainStationDO;
import com.sjj.ticketservice.dao.entity.TrainStationPriceDO;
import com.sjj.ticketservice.dao.mapper.TrainStationMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationPriceMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Island_World
 */
@Slf4j
@SpringBootTest
class TicketStationPriceTests {
    @Autowired
    private TrainStationMapper trainStationMapper;

    @Autowired
    private TrainStationPriceMapper trainStationPriceMapper;

    @Test
    void testInitData() {
        String trainId = "1";
        List<TrainStationDO> trainStations = selectTrainStations(trainId);
        List<TrainStationPriceDO> trainStationPrices = buildTrainStationPrices(trainStations);
        trainStationPrices.forEach(each -> trainStationPriceMapper.insert(each));
    }
      /**
     * 获取每个车次的所有站序
     * @param trainId 当前车次
     * @return 当前车次若途径1-2-3-4-5，返回 List:{1-2,2-3,3-4,4-5,5-null}
     */
    private List<TrainStationDO> selectTrainStations(String trainId){
        var trainStationWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId,trainId);
        return trainStationMapper.selectList(trainStationWrapper);
    }

    /** 根据 TrainStation 的数据生成 TrainStationPrice 的数据
     *
     * @param trainStations
     * @return
     */
    private List<TrainStationPriceDO> buildTrainStationPrices(List<TrainStationDO> trainStations) {
        List<Integer> seats = ListUtil.of(0, 1, 2); // 商务、一等、二等
        List<TrainStationPriceDO> result = new ArrayList<>();
        for (int i = 0; i < trainStations.size() - 1; i++) {
            TrainStationDO trainStationDO = trainStations.get(i);
            for (int j = i + 1; j < trainStations.size(); j++) {
                for (Integer seat : seats) {
                    TrainStationPriceDO actual = new TrainStationPriceDO();
                    actual.setTrainId(trainStationDO.getTrainId());
                    actual.setDeparture(trainStations.get(i).getDeparture());
                    actual.setArrival(trainStations.get(j).getDeparture());
                    actual.setPrice(0);
                    actual.setSeatType(seat);
                    actual.setCreateTime(new Date());
                    actual.setUpdateTime(new Date());
                    actual.setDelFlag(0);
                    result.add(actual);
                }
            }
        }
        return result;
    }


}
