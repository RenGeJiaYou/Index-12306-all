package com.sjj.ticketservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Objects;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.conventionspringbootstarter.exception.ServiceException;
import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.designpatternspringbootstarter.strategy.AbstractStrategyChoose;
import com.sjj.ticketservice.common.enums.TicketStatusEnum;
import com.sjj.ticketservice.common.enums.TrainTagEnum;
import com.sjj.ticketservice.common.enums.VehicleSeatTypeEnum;
import com.sjj.ticketservice.common.enums.VehicleTypeEnum;
import com.sjj.ticketservice.dao.entity.TicketDO;
import com.sjj.ticketservice.dao.entity.TrainDO;
import com.sjj.ticketservice.dao.entity.TrainStationPriceDO;
import com.sjj.ticketservice.dao.entity.TrainStationRelationDO;
import com.sjj.ticketservice.dao.mapper.TicketMapper;
import com.sjj.ticketservice.dao.mapper.TrainMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationPriceMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationRelationMapper;
import com.sjj.ticketservice.dto.domain.HighSpeedTrainDTO;
import com.sjj.ticketservice.dto.domain.PassengerInfoDTO;
import com.sjj.ticketservice.dto.domain.SeatClassDTO;
import com.sjj.ticketservice.dto.domain.TicketListDTO;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.sjj.ticketservice.dto.resp.TicketPageQueryRespDTO;
import com.sjj.ticketservice.remote.TicketOrderRemoteService;
import com.sjj.ticketservice.remote.dto.TicketOrderCreateRemoteReqDTO;
import com.sjj.ticketservice.remote.dto.TicketOrderItemCreateRemoteReqDTO;
import com.sjj.ticketservice.service.TicketService;
import com.sjj.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;
import com.sjj.ticketservice.toolkit.DateUtil;
import com.sjj.userspringbootstarter.core.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sjj.ticketservice.common.constant.Index13206Constant.ADVANCE_TICKET_DAY;
import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_INFO;
import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET;
import static com.sjj.ticketservice.toolkit.DateUtil.convertDateToLocalTime;

/**
 * 车票服务实现类
 *
 * @author Island_World
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TrainMapper trainMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;
    private final TicketMapper ticketMapper;
    private final DistributedCache distributedCache;
    private final AbstractStrategyChoose abstractStrategyChoose;
    private final TicketOrderRemoteService ticketOrderRemoteService;

    @Override
    public TicketPageQueryRespDTO pageListTicketQuery(TicketPageQueryReqDTO req) {
        // TODO 责任链模式 验证城市名称是否存在、不存在加载缓存等等
        /*
         * 1.根据查询的「出发城市」和「到达城市」 筛选出 t_train_station_relation 表中的所有条目，包含「出发时间」、「历时」、「到达时间（计算得出）」、出发站点、到达站点
         * 但仅有 t_train_station_relation 表的数据是不完整的，因为它仅包含 train_id(外键，也是 t_train 的主键),
         * 2. 还需要 JOIN 操作，根据 t_train_station_relation.train_id 在 t_train 找 train_num(车次号)
         * 3. 根据 t_train_station_price 找出座位的类型和价格
         * */
        // 1. 查询 t_train_station_relation
        LambdaQueryWrapper<TrainStationRelationDO> relationDOLambdaQueryWrapper = Wrappers
                .lambdaQuery(TrainStationRelationDO.class)
                .eq(TrainStationRelationDO::getStartRegion, req.getFromCity())
                .eq(TrainStationRelationDO::getEndRegion, req.getToCity());
        List<TrainStationRelationDO> trainStationRelationList = trainStationRelationMapper.selectList(relationDOLambdaQueryWrapper);
        List<TicketListDTO> seatResults = new ArrayList<>();
        List<String> trainBrandList = new ArrayList<>();
        trainStationRelationList.forEach(each -> {
            var trainQueryWrapper = Wrappers.lambdaQuery(TrainDO.class)
                    .eq(TrainDO::getId, each.getTrainId());
            // 2.依次根据 t_train_station_relation 每条记录中的外键 train_id
            // 去 t_train 里查找车次号，并存到当前 ResponseDTO 实例中
            var trainDO = trainMapper.selectOne(trainQueryWrapper);
            var result = new TicketListDTO();
            result.setTrainId(trainDO.getId().toString());
            result.setTrainNumber(trainDO.getTrainNumber());
            result.setDepartureTime(convertDateToLocalTime(each.getDepartureTime(),"HH:mm"));
            result.setDuration(DateUtil.calculateHourDifference(each.getDepartureTime(), each.getArrivalTime()));
            result.setArrivalTime(convertDateToLocalTime(each.getArrivalTime(),"HH:mm"));
            result.setDeparture(each.getDeparture());
            result.setArrival(each.getArrival());
            result.setDepartureFlag(each.getDepartureFlag());
            result.setArrivalFlag(each.getArrivalFlag());
            result.setTrainType(trainDO.getTrainType());
            result.setTrainBrand(trainDO.getTrainBrand());
            if(StrUtil.isNotBlank(trainDO.getTrainBrand())){
                trainBrandList.addAll(TrainTagEnum.findNameByCode(StrUtil.split(trainDO.getTrainBrand(),",")));
            }

            // 3. 如果是高铁，根据 t_train_station_price 找出座位的类型和价格
            if (Objects.equal(trainDO.getTrainType(), 0)) {
                var highSpeedTrainDTO = new HighSpeedTrainDTO();
                var trainStationPriceQueryWrapper = Wrappers.lambdaQuery(TrainStationPriceDO.class)
                        .eq(TrainStationPriceDO::getDeparture, each.getDeparture())
                        .eq(TrainStationPriceDO::getArrival, each.getArrival())
                        .eq(TrainStationPriceDO::getTrainId, each.getTrainId());

                // 该列表表示 {train_id} 这一趟车上从 {departure}站到 {arrival}站的所有座位
                var trainStationPriceList = trainStationPriceMapper.selectList(trainStationPriceQueryWrapper);
                var redisTemplate = (StringRedisTemplate) distributedCache.getInstance();
                trainStationPriceList.forEach(item -> {
                    var redisKeySuffix = StrUtil.join("_", each.getTrainId(), item.getDeparture(), item.getArrival());
                    switch (item.getSeatType()) {
                        case 0:
                            String businessClassQuantity = (String) redisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + redisKeySuffix, "0");
                            highSpeedTrainDTO.setBusinessClassQuantity(Integer.parseInt(businessClassQuantity));
                            highSpeedTrainDTO.setBusinessClassPrice(item.getPrice());
                            // todo 候补逻辑
                            highSpeedTrainDTO.setBusinessClassCandidate(false);
                            break;
                        case 1:
                            String firstClassQuantity = (String) redisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + redisKeySuffix, "1");
                            highSpeedTrainDTO.setFirstClassQuantity(Integer.parseInt(firstClassQuantity));
                            highSpeedTrainDTO.setFirstClassPrice(item.getPrice());
                            // todo 候补逻辑
                            highSpeedTrainDTO.setFirstClassCandidate(false);
                            break;
                        case 2:
                            String secondClassQuantity = (String) redisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + redisKeySuffix, "2");
                            highSpeedTrainDTO.setBusinessClassQuantity(Integer.parseInt(secondClassQuantity));
                            highSpeedTrainDTO.setSecondClassPrice(item.getPrice());
                            // todo 候补逻辑
                            highSpeedTrainDTO.setSecondClassCandidate(false);
                            break;
                        default:
                            break;
                    }
                });
//            result.setHighSpeedTrain(highSpeedTrainDTO); // 最终的 TicketListDTO result 没有这个字段
            seatResults.add(result);                
            }
        });
        return TicketPageQueryRespDTO.builder()
                .trainList(seatResults)
                .departureStationList(buildDepartureStationList(seatResults))
                .arrivalStationList(buildArrivalStationList(seatResults))
                .trainBrandList(buildTrainBrandList(seatResults))
                .seatClassTypeList(buildSeatClassTypeList(seatResults))
                .build();
    }

    private List<String> buildDepartureStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getDeparture).distinct().toList();
    }

    private List<String> buildArrivalStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getArrival).distinct().toList();

    }

    private List<Integer> buildTrainBrandList(List<TicketListDTO> seatResults) {
        HashSet<Integer> trainBrandSet = new HashSet<>();
        for(TicketListDTO each: seatResults){
            for(SeatClassDTO item:each.getSeatClassList()){
                trainBrandSet.add(item.getType());
            }
        }
        return trainBrandSet.stream().toList();
    }

    private List<Integer> buildSeatClassTypeList(List<TicketListDTO> seatResults) {
        HashSet<Integer> SeatClassType = new HashSet<>();
        for(TicketListDTO each: seatResults){
            if(StrUtil.isNotBlank(each.getTrainBrand())){
                SeatClassType.addAll(StrUtil.split(each.getTrainBrand(),",").stream().map(Integer::valueOf).toList());
            }
        }
        return SeatClassType.stream().toList();
    }





    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String purchaseTickets(PurchaseTicketReqDTO req) {
        String trainId = req.getTrainId();
        TrainDO trainDO = distributedCache.get(
                TRAIN_INFO + trainId,
                TrainDO.class,
                () -> trainMapper.selectById(trainId),
                ADVANCE_TICKET_DAY,
                TimeUnit.DAYS);
        // 根据"交通工具类型"+"座位类型"选择对应的策略,传入 购票请求参数 并返回 List<购票响应参数>，即已为乘车人选择的[商务/一等/二等]座集合
        List<TrainPurchaseTicketRespDTO> selectedSeatsAsTickets = abstractStrategyChoose.chooseAndExecuteResp(
                VehicleTypeEnum.findNameByCode(trainDO.getTrainType()) + VehicleSeatTypeEnum.findNameByCode(req.getSeatType()), req);
        // todo 批量插入
        // 返回了 List<TrainPurchaseTicketRespDTO> 说明 t_seat 已经锁定了这么多座位；接下来就是依次创建 t_ticket 记录
        selectedSeatsAsTickets.forEach(each -> {
            PassengerInfoDTO passengerInfo = each.getPassengerInfo();
            TicketDO ticketDO = TicketDO.builder()
                    .username(UserContext.getUserName()) // todo 了解 UserContext
                    .trainId(Long.valueOf(req.getTrainId()))
                    .carriageNumber(each.getCarriageNumber())
                    .seatNumber(each.getSeatNumber())
                    .passengerId(passengerInfo.getPassengerId())
                    .ticketStatus(TicketStatusEnum.UNPAID.getCode())
                    .build();
            ticketMapper.insert(ticketDO);
        });
        Result<String> ticketOrderResult;
        try {
            // 订单明细请求DTO列表
            List<TicketOrderItemCreateRemoteReqDTO> orderItemList = new ArrayList<>();
            selectedSeatsAsTickets.forEach(each -> {
                PassengerInfoDTO passengerInfo = each.getPassengerInfo();
                var orderItemCreateRemoteReqDTO = TicketOrderItemCreateRemoteReqDTO.builder()
                        .carriageNumber(each.getCarriageNumber())
                        .seatNumber(each.getSeatNumber())
                        .realName(passengerInfo.getRealName())
                        .idType(passengerInfo.getIdType())
                        .idCard(passengerInfo.getIdCard())
                        .phone(passengerInfo.getPhone())
                        .amount(each.getAmount())
                        .build();
                orderItemList.add(orderItemCreateRemoteReqDTO);
            });
            // 订单请求 DTO
            var orderCreateRemoteReqDTO = TicketOrderCreateRemoteReqDTO.builder()
                    .username(UserContext.getUserName())
                    .trainId(Long.valueOf(req.getTrainId()))
                    .departure(req.getDeparture())
                    .arrival(req.getArrival())
                    //.source()
                    .orderTime(new Date())
                    .ticketOrderItems(orderItemList)
                    .build();
            ticketOrderResult = ticketOrderRemoteService.createTicketOrder(orderCreateRemoteReqDTO);

        } catch (Throwable ex) {
            log.error("远程调用订单服务创建错误，请求参数：{}", JSON.toJSONString(req));
            // todo 回退锁定的车票
            throw ex;
        }
        if (ticketOrderResult == null || !ticketOrderResult.isSuccess()) {
            log.error("远程调用订单服务创建错误，请求参数：{}", JSON.toJSONString(req));
            // todo 回退锁定的车票
            throw new ServiceException(ticketOrderResult.getMessage());
        }
        return ticketOrderResult.getData();
    }
    
    
}
