package com.sjj.userservice.service;

import com.sjj.userservice.dto.resp.UserQueryRespDTO;
import jakarta.validation.constraints.NotEmpty;

/**
 * 用户信息接口层
 *
 * @author Island_World
 */

public interface UserService {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */
    UserQueryRespDTO queryUserByUsername(@NotEmpty String username);
}
