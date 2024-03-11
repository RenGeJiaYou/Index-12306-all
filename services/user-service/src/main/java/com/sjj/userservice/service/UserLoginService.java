package com.sjj.userservice.service;

import com.sjj.userservice.dto.req.UserLoginReqDTO;
import com.sjj.userservice.dto.req.UserRegisterReqDTO;
import com.sjj.userservice.dto.resp.UserLoginRespDTO;
import com.sjj.userservice.dto.resp.UserRegisterRespDTO;

/**
 * 用户登录接口
 *
 * @author Island_World
 */


public interface UserLoginService {

    /**
     * 用户登录
     *
     * @param req 用户登录请求参数
     * @return 用户登录响应参数
     */
    UserLoginRespDTO login(UserLoginReqDTO req);

    /**
     * 通过 Token 检查用户是否登录
     *
     * @param accessToken 用户登录 Token 凭证
     * @return 用户登录响应参数
     */
    UserLoginRespDTO checkLogin(String accessToken);

    /**
     * 用户退出登录，通过删除 Redis 中的 token 实现
     *
     * @param accessToken 用户登录 Token 凭证
     */
    void logout(String accessToken);

    /**
     * 用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在返回结果
     */
    Boolean hasUsername(String username);

    /**
     * 用户注册
     *
     * @param registerReqDTO 用户注册入参
     * @return 用户注册返回结果
     */
    UserRegisterRespDTO register(UserRegisterReqDTO registerReqDTO);
}
