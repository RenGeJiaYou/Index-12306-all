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

package com.sjj.userspringbootstarter.toolkit;

/*
 * JWT 工具类
 *
 * @author Island_World
 */

import com.alibaba.fastjson2.JSON;
import com.sjj.userspringbootstarter.core.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

@Slf4j
public final class JWTUtil {
    private final static String SECERT = "SecretKey039245678901232039487623493984682379954387615468277623456783";
    private final static String ISSUER = "index12306";
    private final static long EXPIRATION = 86400L;
    private final static String TOKEN_PREFIX = "Bearer ";

    /**
     * 生成 token
     *
     * @param userInfo 用户信息
     * @return token 字符串
     */
    public static String generateToken(UserInfoDTO userInfo) {
        // 拿到 userinfo 中的数据
        HashMap<String, Object> customerUserMap = new HashMap<>();
        customerUserMap.put("USER_ID_KEY", userInfo.getUserId());
        customerUserMap.put("USER_REAL_NAME_KEY", userInfo.getRealName());
        customerUserMap.put("USERNAME_KEY", userInfo.getUsername());

        // 设置加密算法、过期时间、密钥并生成 JWT token
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS512, SECERT)
                .setIssuedAt(new Date())
                .setIssuer(ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * EXPIRATION))
                .setSubject(JSON.toJSONString(customerUserMap))
                .compact();

        //返回
        return TOKEN_PREFIX + token;
    }


    /**
     * 解析 token
     *
     * @param token token 字符串
     * @return 用户信息
     */
    public static UserInfoDTO parseToken(String token) {
        // 拿到 token,并重新取出 header 和 payload
        if (Strings.hasText(token)) {
            String actualJwtToken = token.replace(TOKEN_PREFIX, "");
            try {
                // 根据 SECERT 解析 token
                Claims claims = Jwts.parser().setSigningKey(SECERT).parseClaimsJwt(actualJwtToken).getBody();
                Date expiration = claims.getExpiration();
                // 检查过期时间
                if (expiration.after(new Date())) {
                    String subject = claims.getSubject();
                    // 一致则通过，不一致返回 null
                    return JSON.parseObject(subject, UserInfoDTO.class);
                }

            } catch (ExpiredJwtException ignred) {
            } catch (Exception e) {
                log.error("解析 token 失败", e);
            }
        }
        return null;
    }
}
