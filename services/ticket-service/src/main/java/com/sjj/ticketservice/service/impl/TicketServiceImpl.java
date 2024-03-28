package com.sjj.ticketservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Objects;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.conventionspringbootstarter.page.PageResponse;
import com.sjj.databasespringbootstarter.toolkit.PageUtil;
import com.sjj.ticketservice.dao.entity.TrainDO;
import com.sjj.ticketservice.dao.entity.TrainStationPriceDO;
import com.sjj.ticketservice.dao.entity.TrainStationRelationDO;
import com.sjj.ticketservice.dao.mapper.TrainMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationPriceMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationRelationMapper;
import com.sjj.ticketservice.dto.domain.BulletTrainDTO;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.sjj.ticketservice.dto.resp.TicketPageQueryRespDTO;
import com.sjj.ticketservice.service.TicketService;
import com.sjj.ticketservice.toolkit.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET;

/**
 * 车票服务实现类
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TrainMapper trainMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;
    private final DistributedCache distributedCache;

    @Override
    public PageResponse<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO req) {
        // TODO 责任链模式 验证城市名称是否存在、不存在加载缓存等等
        /**
         * 1.根据查询的「出发城市」和「到达城市」 筛选出 t_train_station_relation 表中的所有条目，包含「出发时间」、「历时」、「到达时间（计算得出）」、出发站点、到达站点
         * 但仅有 t_train_station_relation 表的数据是不完整的，因为它仅包含 train_id(外键，也是 t_train 的主键),
         * 2. 还需要JOIN 操作，根据 t_train_station_relation.train_id 在 t_train 找 train_num(车次号)
         * 3. 根据 t_train_station_price 找出座位的类型和价格
         * */
        // 1. 查询 t_train_station_relation
        LambdaQueryWrapper<TrainStationRelationDO> relationDOLambdaQueryWrapper = Wrappers
                .lambdaQuery(TrainStationRelationDO.class)
                .eq(TrainStationRelationDO::getStartRegion, req.getFromCity())
                .eq(TrainStationRelationDO::getEndRegion, req.getToCity());
        IPage<TrainStationRelationDO> page = trainStationRelationMapper.selectPage(PageUtil.convert(req), relationDOLambdaQueryWrapper);
        return PageUtil.convert(page, each -> {
            var trainQueryWrapper = Wrappers.lambdaQuery(TrainDO.class)
                    .eq(TrainDO::getId, each.getTrainId());
            // 2.依次根据 t_train_station_relation 每条记录中的外键 train_id
            // 去 t_train 里查找车次号，并存到当前 ResponseDTO 实例中
            var trainDO = trainMapper.selectOne(trainQueryWrapper);
            var result = new TicketPageQueryRespDTO();
            result.setTrainNumber(trainDO.getTrainNumber());
            result.setDepartureTime(each.getDepartureTime());
            result.setDuration(DateUtil.calculateHourDifference(each.getDepartureTime(), each.getArrivalTime()));
            result.setArrivalTime(each.getArrivalTime());
            result.setDeparture(each.getDeparture());
            result.setArrival(each.getArrival());
            result.setDepartureFlag(each.getDepartureFlag());
            result.setArrivalFlag(each.getArrivalFlag());

            // 3. 如果是高铁，根据 t_train_station_price 找出座位的类型和价格
            if (Objects.equal(trainDO.getTrainType(), 0)) {
                var bulletTrainDTO = new BulletTrainDTO();
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
                            bulletTrainDTO.setBusinessSeatQuantity(Integer.parseInt(businessClassQuantity));
                            bulletTrainDTO.setBusinessSeatPrice(item.getPrice());
                            // todo 候补逻辑
                            bulletTrainDTO.setBusinessSeatCandidate(false);
                            break;
                        case 1:
                            String firstClassQuantity = (String) redisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + redisKeySuffix, "1");
                            bulletTrainDTO.setBusinessSeatQuantity(Integer.parseInt(firstClassQuantity));
                            bulletTrainDTO.setFirstSeatPrice(item.getPrice());
                            // todo 候补逻辑
                            bulletTrainDTO.setFirstSeatCandidate(false);
                            break;
                        case 2:
                            String secondClassQuantity = (String) redisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + redisKeySuffix, "2");
                            bulletTrainDTO.setBusinessSeatQuantity(Integer.parseInt(secondClassQuantity));
                            bulletTrainDTO.setSecondSeatPrice(item.getPrice());
                            // todo 候补逻辑
                            bulletTrainDTO.setSecondSeatCandidate(false);
                            break;
                        default:
                            break;
                    }
                    result.setBulletTrain(bulletTrainDTO);
                });
            }
            return result;
        });
    }


    @Override
    public String purchaseTickets(PurchaseTicketReqDTO req) {
        return null;
    }
}
