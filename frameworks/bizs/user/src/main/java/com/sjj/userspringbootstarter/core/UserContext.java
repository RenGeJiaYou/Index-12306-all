package com.sjj.userspringbootstarter.core;

/*
 * 用户上下文，用来存储一个线程局部变量：用户DTO
 *
 * @Author Island_World
 */

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

public class UserContext {
    private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
    * 设置用户 Context
    * @param user 用户详情信息
    */
    public static void setUser(UserInfoDTO user){USER_THREAD_LOCAL.set(user);}

    /**
     * 清理用户上下文
    * */
    public static void removeUser(){USER_THREAD_LOCAL.remove();}

    /**
     * 获取上下文中用户 ID
     * @return 用户 ID
     * */
    public static String getUserId(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }
    /**
     * 获取上下文中用户名
     * @return 用户 ID
     * */
    public static String getUserName(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUsername).orElse(null);
    }
    /**
     * 获取上下文中用户 ID
     * @return 用户 ID
     * */
    public static String getUserRealName(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getRealName).orElse(null);
    }
    /**
     * 获取上下文中用户 ID
     * @return 用户 ID
     * */
    public static String getToken(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getToken).orElse(null);
    }
}
