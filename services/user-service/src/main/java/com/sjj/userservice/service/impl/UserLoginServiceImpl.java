package com.sjj.userservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.commonspringbootstarter.toolkit.BeanUtil;
import com.sjj.conventionspringbootstarter.exception.ClientException;
import com.sjj.conventionspringbootstarter.exception.ServiceException;
import com.sjj.userservice.dao.entity.UserDO;
import com.sjj.userservice.dao.mapper.UserMapper;
import com.sjj.userservice.dto.req.UserLoginReqDTO;
import com.sjj.userservice.dto.req.UserRegisterReqDTO;
import com.sjj.userservice.dto.resp.UserLoginRespDTO;
import com.sjj.userservice.dto.resp.UserRegisterRespDTO;
import com.sjj.userservice.service.UserLoginService;
import com.sjj.userservice.toolkit.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户登录接口实现
 *
 * @author Island_World
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserMapper userMapper;
    private final DistributedCache distributedCache;

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO req) {
        var wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, req.getUsername())
                .eq(UserDO::getPassword, req.getPassword());
        UserDO userDO = userMapper.selectOne(wrapper);
        if (userDO != null){
            String accessToken = JWTUtil.generateAccessToken(req);
            UserLoginRespDTO actual = new UserLoginRespDTO(req.getUsername(), userDO.getRealName(), accessToken);
            distributedCache.put(accessToken, JSON.toJSONString(actual),30, TimeUnit.MINUTES);
            return actual;
        }
        throw new ServiceException("用户名不存在或密码错误");
    }

    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return distributedCache.get(accessToken,UserLoginRespDTO.class);

    }

    @Override
    public void logout(String accessToken) {
        if (StrUtil.isNotEmpty(accessToken)){
            distributedCache.delete(accessToken);
        }
    }

    @Override
    public Boolean hasUsername(String username) {
        // todo 后续将采用 布隆过滤器 防止缓存穿透
        var wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        return userMapper.selectOne(wrapper) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO registerReqDTO) {
        // todo 责任链模式校验用户名、身份证、手机号格式
        if (!hasUsername(registerReqDTO.getUsername())) {
            throw new ClientException("用户名已经存在");
        }
        int inserted = userMapper.insert(BeanUtil.convert(registerReqDTO, UserDO.class));
        if (inserted < 1) {
            throw new ServiceException("用户注册失败");
        }
        return BeanUtil.convert(registerReqDTO, UserRegisterRespDTO.class);
    }
}
