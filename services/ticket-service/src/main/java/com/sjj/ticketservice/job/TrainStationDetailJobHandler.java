package com.sjj.ticketservice.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.ticketservice.dao.entity.TrainDO;
import com.sjj.ticketservice.dao.entity.TrainStationRelationDO;
import com.sjj.ticketservice.dao.mapper.TrainStationRelationMapper;
import com.sjj.ticketservice.job.base.AbstractTrainStationJobHandlerTemplate;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MINUTE_FORMAT;
import static com.sjj.ticketservice.common.constant.Index13206Constant.ADVANCE_TICKET_DAY;
import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_DETAIL;

/**
 * 站点详细信息定时任务
 *
 * @author Island_World
 */

@RestController
@RequiredArgsConstructor
public class TrainStationDetailJobHandler extends AbstractTrainStationJobHandlerTemplate {
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final DistributedCache distributedCache;

    @XxlJob("trainStationDetailJobHandler")
    @GetMapping("/api/ticket-service/train-station-detail/job/cache-init/execute")
    @Override
    public void execute() {
        super.execute();
    }

    /**
     *
     * @param trainDOPageRecords 特定日期启程的所有车次
     */
    @Override
    protected void actualExecute(List<TrainDO> trainDOPageRecords) {
        for (TrainDO trainDO : trainDOPageRecords) {
            // 某趟车次的所有区间组合
            var wrapper = Wrappers.lambdaQuery(TrainStationRelationDO.class)
                    .eq(TrainStationRelationDO::getTrainId, trainDO.getId());
            var sameTrainIdRecords = trainStationRelationMapper.selectList(wrapper);
            if (CollUtil.isEmpty(sameTrainIdRecords)) {
                return;
            }
            for (TrainStationRelationDO item:sameTrainIdRecords){
                Map<String, String> actualCacheHashValue = MapUtil.builder("trainNumber",trainDO.getTrainNumber())
                        .put("departureFlag", BooleanUtil.toStringTrueFalse(item.getDepartureFlag()))
                        .put("arrivalFlag", BooleanUtil.toStringTrueFalse(item.getArrivalFlag()))
                        .put("departureTime", DateUtil.format(item.getDepartureTime(),"HH:mm"))
                        .put("arrivalTime", DateUtil.format(item.getArrivalTime(),"HH:mm"))
                        .put("saleTime",DateUtil.format(trainDO.getSaleTime(),NORM_DATETIME_MINUTE_FORMAT))
                        .put("trainBrand",trainDO.getTrainBrand())
                        .build();

                StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
                String buildCacheKey = TRAIN_STATION_DETAIL + StrUtil.join("_",trainDO.getId(),item.getDeparture(),item.getArrival());
                stringRedisTemplate.opsForHash().putAll(buildCacheKey,actualCacheHashValue);
                stringRedisTemplate.expire(buildCacheKey,ADVANCE_TICKET_DAY, TimeUnit.DAYS);
            }
        }
    }
}
