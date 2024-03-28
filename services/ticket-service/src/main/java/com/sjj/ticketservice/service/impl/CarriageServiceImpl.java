package com.sjj.ticketservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.ticketservice.dao.entity.CarriageDO;
import com.sjj.ticketservice.dao.mapper.CarriageMapper;
import com.sjj.ticketservice.service.CarriageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sjj.ticketservice.common.constant.RedisKeyConstant.TRAIN_CARRIAGE;

/**
 * 列车车厢接口层实现类
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
public class CarriageServiceImpl implements CarriageService {
    private final DistributedCache distributedCache;
    private final CarriageMapper carriageMapper;


    @Override
    public List<String> listCarriageNumber(String trainId, Integer carriageType) {
        var stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        // redis 中的存储形式类似 "1,3,4"
        Object trainCarriageStr = stringRedisTemplate.opsForHash().get(TRAIN_CARRIAGE + trainId, String.valueOf(carriageType));
        // 如果缓存未命中，去数据库查询
        if (trainCarriageStr == null) {
            var wrapper = Wrappers.lambdaQuery(CarriageDO.class)
                    .eq(CarriageDO::getTrainId,trainId)
                    .eq(CarriageDO::getCarriageType,carriageType);
            var carriageDOList = carriageMapper.selectList(wrapper);
            return carriageDOList.stream().map(CarriageDO::getCarriageNumber).collect(Collectors.toList());
        }
        return StrUtil.split(trainCarriageStr.toString(),",");
    }
}
