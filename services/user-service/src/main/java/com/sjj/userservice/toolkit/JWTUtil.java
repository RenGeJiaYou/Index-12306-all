package com.sjj.userservice.toolkit;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.sjj.userservice.dto.req.UserLoginReqDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

/**
 * JWT 工具类，生成包含 token 的响应报文
 *
 * @author Island_World
 */

public final class JWTUtil {
    private static final long EXPIRATION = 86400L;
    public static final String ISSURE = "index12306";
    public static final String SECRET = "SecretKey039245678901232039675254656783092349288901402967890140939827";

    /**
     * 生成用户 Token
     *
     * @param userInfo 用户信息
     * @return 用户访问 token
     */
    public static String generateAccessToken(UserLoginReqDTO userInfo){
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("username",userInfo.getUsernameOrMailOrPhone());
        map.put("password",userInfo.getPassword());
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .setIssuedAt(new Date())
                .setIssuer(ISSURE)
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION*1000))
                .setSubject(JSON.toJSONString(map))
                .compact();
    }
}
