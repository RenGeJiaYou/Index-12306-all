package com.sjj.userservice.toolkit;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.sjj.userspringbootstarter.core.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

import static com.sjj.basespringbootstarter.constant.UserConstant.*;
import static com.sjj.userspringbootstarter.toolkit.JWTUtil.TOKEN_PREFIX;

/**
 * JWT 工具类，生成包含 token 的响应报文
 *
 * @author Island_World
 */
@Slf4j
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
    public static String generateAccessToken(UserInfoDTO userInfo) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put(USER_ID_KEY, userInfo.getUserId());
        map.put(USER_NAME_KEY, userInfo.getUsername());
        map.put(USER_REAL_NAME_KEY, userInfo.getRealName());
        return TOKEN_PREFIX + Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuedAt(new Date())
                .setIssuer(ISSURE)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .setSubject(JSON.toJSONString(map))
                .compact();
    }

    /**
     * 解析用户 Token
     *
     * @param jwtToken 用户访问 Token
     * @return 用户信息
     */
    public static UserInfoDTO parseJwtToken(String jwtToken) {
        if (StrUtil.isNotBlank(jwtToken)) {
            String actualJwtToken = jwtToken.replace(TOKEN_PREFIX, "");
            try {
                Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(actualJwtToken).getBody();
                Date expiration = claims.getExpiration();
                if (expiration.after(new Date())) {
                    String subject = claims.getSubject();
                    return JSON.parseObject(subject,UserInfoDTO.class);
                }
            } catch (ExpiredJwtException ignored) {
            } catch (Exception ex) {
                log.error("JWT Token解析失败，请检查", ex);
            }
        }
        return null;
    }
}
