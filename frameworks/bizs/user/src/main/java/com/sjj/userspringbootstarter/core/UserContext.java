/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjj.userspringbootstarter.core;

/*
 * 用户上下文，用来存储一个线程局部变量：用户DTO
 *
 * @author Island_World
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
