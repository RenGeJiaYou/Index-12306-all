package com.sjj.ticketservice.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.commonspringbootstarter.toolkit.EnvironmentUtil;
import com.sjj.ticketservice.dao.entity.RegionDO;
import com.sjj.ticketservice.dao.entity.TrainStationRelationDO;
import com.sjj.ticketservice.dao.mapper.RegionMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationRelationMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.sjj.ticketservice.common.constant.Index13206Constant.ADVANCE_TICKET_DAY;
import static com.sjj.ticketservice.common.constant.RedisKeyConstant.REGION_TRAIN_STATION;

/**
 * 地区站点查询定时任务
 *
 * @author Island_World
 */
@RestController
@RequiredArgsConstructor
public class RegionTrainStationJobHandler extends IJobHandler {
    private final RegionMapper regionMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final DistributedCache distributedCache;

    private String getJobRequestParam() {
        return EnvironmentUtil.isDevEnvironment()
                ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("requestParam")
                : XxlJobHelper.getJobParam();
    }

    /**
     * 将 Region 所有城市名称（n个）取出，再遍历出任意两站间的所有车次(遍历(n-1)*(n-1)趟，每趟有若干条车次记录)
     * 将这些车次记录，按发车时间有序地缓存到 redis
     */
    @XxlJob(value = "regionTrainStationJobHandler")
    @GetMapping("/api/ticket-service/region-train-station/job/cache-init/execute")
    @Override
    public void execute() {
        // 整个 t_region 表不是很大，直接读取
        var regionList = regionMapper.selectList(Wrappers.emptyWrapper())
                .stream()
                .map(RegionDO::getName)
                .toList();
        String jobRequestParam = getJobRequestParam();
        var dateTime = StrUtil.isNotBlank(jobRequestParam)
                ? jobRequestParam
                : DateUtil.tomorrow().toDateStr();
        for (int i = 0; i < regionList.size(); i++) {
            for (int j = 0; j < regionList.size(); j++) {
                if (i != j) {
                    String startRegion = regionList.get(i);
                    String endRegion = regionList.get(j);
                    var wrapper = Wrappers.lambdaQuery(TrainStationRelationDO.class)
                            .eq(TrainStationRelationDO::getStartRegion, startRegion)
                            .eq(TrainStationRelationDO::getEndRegion, endRegion);
                    var allStartToEndRegionList = trainStationRelationMapper.selectList(wrapper);
                    if (CollUtil.isEmpty(allStartToEndRegionList)) {
                        continue;
                    }
                    // ZSet 收集不重复的 String 元素。String 值和对应的 Double 分数共同组成一个 TypedTuple
                    Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                    for (TrainStationRelationDO item : allStartToEndRegionList) {
                        String zSetKey = StrUtil.join("_", item.getTrainId(), item.getStartRegion(), item.getEndRegion());
                        var tuple = ZSetOperations.TypedTuple.of(zSetKey, Double.valueOf(item.getDepartureTime().getTime()));
                        tuples.add(tuple);
                    }
                    var stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
                    var buildCacheKey = REGION_TRAIN_STATION + StrUtil.join("_",startRegion,endRegion,dateTime);
                    stringRedisTemplate.opsForZSet().add(buildCacheKey,tuples);
                    stringRedisTemplate.expire(buildCacheKey,ADVANCE_TICKET_DAY, TimeUnit.DAYS);
                }
            }
        }

    }
}
