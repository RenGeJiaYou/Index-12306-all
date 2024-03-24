package com.sjj.userservice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.commonspringbootstarter.toolkit.BeanUtil;
import com.sjj.conventionspringbootstarter.exception.ClientException;
import com.sjj.userservice.dao.entity.UserDO;
import com.sjj.userservice.dao.mapper.UserMapper;
import com.sjj.userservice.dto.resp.UserQueryRespDTO;
import com.sjj.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户信息接口实现层
 *
 * @author Island_World
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    @Override
    public UserQueryRespDTO queryUserByUsername(String username) {
        var wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername,username);
        UserDO userDO = userMapper.selectOne(wrapper);
        if (userDO==null){
            throw new ClientException("用户不存在,请检查用户名");
        }
        return BeanUtil.convert(userDO, UserQueryRespDTO.class);
    }

}
