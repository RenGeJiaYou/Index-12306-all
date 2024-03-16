package com.sjj.ticketservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Objects;
import com.sjj.ticketservice.dao.entity.TrainDO;
import com.sjj.ticketservice.dao.entity.TrainStationPriceDO;
import com.sjj.ticketservice.dao.entity.TrainStationRelationDO;
import com.sjj.ticketservice.dao.mapper.TrainMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationPriceMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationRelationMapper;
import com.sjj.ticketservice.dto.domain.BulletTrainDTO;
import com.sjj.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.sjj.ticketservice.dto.resp.TicketPageQueryRespDTO;
import com.sjj.ticketservice.service.TicketService;
import com.sjj.ticketservice.toolkit.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 车票服务实现类
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TrainMapper trainMapper;
    private final TrainStationMapper trainStationMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;

    @Override
    public IPage<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO req) {
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
        IPage<TrainStationRelationDO> page = trainStationRelationMapper.selectPage(req, relationDOLambdaQueryWrapper);
        return page.convert(each -> {
            var trainQueryWrapper = Wrappers
                    .lambdaQuery(TrainDO.class)
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

            // 3. 如果是高铁，根据 t_train_station_price 找出座位的类型和价格
            if (Objects.equal(trainDO.getTrainType(), 0)) {
                var bulletTrainDTO = new BulletTrainDTO();
                var trainStationPriceQueryWrapper = Wrappers.lambdaQuery(TrainStationPriceDO.class)
                        .eq(TrainStationPriceDO::getDeparture, each.getDeparture())
                        .eq(TrainStationPriceDO::getArrival, each.getArrival())
                        .eq(TrainStationPriceDO::getTrainId, each.getTrainId());

                // 该列表表示 {train_id} 这一趟车上从 {departure}站到 {arrival}站的所有座位
                var trainStationPriceList = trainStationPriceMapper.selectList(trainStationPriceQueryWrapper);
                trainStationPriceList.forEach(item -> {
                    switch(item.getSeatType()){
                        case 0:
                            bulletTrainDTO.setBusinessSeatPrice(item.getPrice());
                            break;
                        case 1:
                            bulletTrainDTO.setFirstSeatPrice(item.getPrice());
                            break;
                        case 2:
                            bulletTrainDTO.setSecondSeatPrice(item.getPrice());
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
}
