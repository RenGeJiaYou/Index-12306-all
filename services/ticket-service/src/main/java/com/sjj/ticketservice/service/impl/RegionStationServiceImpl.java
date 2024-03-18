package com.sjj.ticketservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.commonspringbootstarter.enums.FlagEnum;
import com.sjj.commonspringbootstarter.toolkit.BeanUtil;
import com.sjj.conventionspringbootstarter.exception.ClientException;
import com.sjj.ticketservice.common.enums.RegionStationQueryTypeEnum;
import com.sjj.ticketservice.dao.entity.RegionDO;
import com.sjj.ticketservice.dao.entity.StationDO;
import com.sjj.ticketservice.dao.mapper.RegionMapper;
import com.sjj.ticketservice.dao.mapper.StationMapper;
import com.sjj.ticketservice.dto.req.RegionStationQueryReqDTO;
import com.sjj.ticketservice.dto.resp.RegionStationQueryRespDTO;
import com.sjj.ticketservice.service.RegionStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区&车站实现层
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
public class RegionStationServiceImpl implements RegionStationService {
    private final RegionMapper regionMapper;
    private final StationMapper stationMapper;
    @Override
    public List<RegionStationQueryRespDTO> regionStationQueryList(RegionStationQueryReqDTO req) {
        // todo 请求缓存
        if (StrUtil.isNotBlank(req.getName())){
            var wrapper = Wrappers.lambdaQuery(StationDO.class)
                    .likeRight(StationDO::getName,req.getName())
                    .or()
                    .likeRight(StationDO::getSpell,req.getName());
            List<StationDO> stationDOList = stationMapper.selectList(wrapper);
            return BeanUtil.convert(stationDOList,RegionStationQueryRespDTO.class);
        }

        //todo 请求缓存
        LambdaQueryWrapper<RegionDO> regionWrapper;
        switch (req.getQueryTYpe()){
            case 0 -> regionWrapper = Wrappers.lambdaQuery(RegionDO.class)
                        .eq(RegionDO::getPopularFlag, FlagEnum.TRUE.code());
            case 1 -> regionWrapper = Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.A_E.getSpells());
            case 2 -> regionWrapper = Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.F_J.getSpells());
            case 3 -> regionWrapper = Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.K_O.getSpells());
            case 4 -> regionWrapper = Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.P_T.getSpells());
            case 5 -> regionWrapper = Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.U_Z.getSpells());
            default -> throw new ClientException("查询失败，请检查查询参数是否正确");
        }
        List<RegionDO> regionList = regionMapper.selectList(regionWrapper);
        return BeanUtil.convert(regionList,RegionStationQueryRespDTO.class);
    }
}
