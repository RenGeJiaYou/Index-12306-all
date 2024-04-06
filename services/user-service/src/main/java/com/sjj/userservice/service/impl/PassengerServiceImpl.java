package com.sjj.userservice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.commonspringbootstarter.toolkit.BeanUtil;
import com.sjj.userservice.dao.entity.PassengerDO;
import com.sjj.userservice.dao.mapper.PassengerMapper;
import com.sjj.userservice.dto.req.PassengerRemoveReqDTO;
import com.sjj.userservice.dto.req.PassengerReqDTO;
import com.sjj.userservice.dto.resp.PassengerRespDTO;
import com.sjj.userservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 乘车人 Service 层实现类
 *
 * @author Island_World
 */

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerMapper passengerMapper;
    @Override
    public List<PassengerRespDTO> listPassengerQueryByUsername(String username) {
        var wrapper= Wrappers.lambdaQuery(PassengerDO.class)
                .eq(PassengerDO::getUsername,username);
        var passengerDOList = passengerMapper.selectList(wrapper);
        return BeanUtil.convert(passengerDOList, PassengerRespDTO.class);
    }

    @Override
    public List<PassengerRespDTO> listPassengerQueryByIds(String username, List<Long> ids) {
        var wrapper = Wrappers.lambdaQuery(PassengerDO.class)
                .eq(PassengerDO::getUsername,username)
                .eq(PassengerDO::getId,ids);    // Wrappers 的 eq() 可以传列表
       var passengerList = passengerMapper.selectList(wrapper);
        return BeanUtil.convert(passengerList,PassengerRespDTO.class);
    }

    @Override
    public void savePassenger(PassengerReqDTO requestParam) {
        PassengerDO passenger = BeanUtil.convert(requestParam, PassengerDO.class);
        passenger.setCreateDate(new Date());
        passenger.setVerifyStatus(0);
        // todo 乘客名和当前用户比对
        passengerMapper.insert(passenger);
    }

    @Override
    public void updatePassenger(PassengerReqDTO requestParam) {
        PassengerDO passenger = BeanUtil.convert(requestParam, PassengerDO.class);
        var wrapper = Wrappers.lambdaUpdate(PassengerDO.class)
                // todo 乘客名和当前用户比对
                .eq(PassengerDO::getUsername,passenger.getUsername())
                .eq(PassengerDO::getId,passenger.getId());
        passengerMapper.update(passenger,wrapper);
    }

    @Override
    public void removePassenger(PassengerRemoveReqDTO requestParam) {
        var delWrapper = Wrappers.lambdaQuery(PassengerDO.class)
                .eq(PassengerDO::getId,requestParam.getId())
                .eq(PassengerDO::getUsername,requestParam.getUsername());
        passengerMapper.delete(delWrapper);
    }
}
