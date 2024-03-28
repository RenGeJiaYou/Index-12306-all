package com.sjj.ticketservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.ticketservice.common.enums.SeatsStatusEnum;
import com.sjj.ticketservice.dao.entity.SeatDO;
import com.sjj.ticketservice.dao.mapper.SeatMapper;
import com.sjj.ticketservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_CARRIAGE_REMAINING_TICKET;
import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET;

/**
 * 座位接口层实现
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    public final SeatMapper seatMapper;
    public final DistributedCache distributedCache;

    @Override
    public List<String> listAvailableSeat(String trainId, String carriageNumber) {
        var wrapper = Wrappers.lambdaQuery(SeatDO.class)
                .eq(SeatDO::getTrainId, trainId)
                .eq(SeatDO::getCarriageNumber, carriageNumber)
                .eq(SeatDO::getSeatStatus, SeatsStatusEnum.AVAILABLE.getCode());
        List<SeatDO> availableSeats = seatMapper.selectList(wrapper);
        return availableSeats.stream().map(SeatDO::getSeatNumber).collect(Collectors.toList());
    }

    @Override
    public List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList) {
        String keySuffix = StrUtil.join("_", trainId, departure, arrival);
        if (distributedCache.hasKey(TRAIN_STATION_REMAINING_TICKET + keySuffix)){
            var stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            var remainingTicketList = stringRedisTemplate.opsForHash().multiGet(TRAIN_STATION_CARRIAGE_REMAINING_TICKET+keySuffix, Arrays.asList(trainCarriageList.toArray()));
            if (CollUtil.isNotEmpty(remainingTicketList)){
                return remainingTicketList.stream()
                        .map(each -> Integer.parseInt(each.toString()))
                        .collect(Collectors.toList());
            }
        }
            return seatMapper.listSeatRemainingTicket(SeatDO.builder().trainId(Long.valueOf(trainId)).startStation(departure).endStation(arrival).build(), trainCarriageList);
    }


}
