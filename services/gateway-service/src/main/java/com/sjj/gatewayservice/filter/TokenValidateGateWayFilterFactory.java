package com.sjj.gatewayservice.filter;

/**
 * SpringCloud Gateway Token 拦截器
 *
 * @author Island_World
 */

import com.sjj.basespringbootstarter.constant.UserConstant;
import com.sjj.gatewayservice.config.Config;
import com.sjj.userspringbootstarter.core.UserInfoDTO;
import com.sjj.userspringbootstarter.toolkit.JWTUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class TokenValidateGateWayFilterFactory extends AbstractGatewayFilterFactory<Config> {
    @Override
    public GatewayFilter apply(Config config) {
        // exchange 保存着请求和响应信息
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestPath = request.getPath().toString();
            if (requiresTokenValidation(requestPath, config.getBlackPathPre())) {
                String token = request.getHeaders().getFirst("Authorization");
                UserInfoDTO userInfoDTO;
                if (!validateToken(token) || (userInfoDTO = JWTUtil.parseToken(token)) == null) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    //setComplete() 用于表示当前的 HTTP 响应已经完成，不再需要进一步的处理。
                    return response.setComplete();
                }
                // 在 Request header 添加首部字段，由于 ServerHttpRequest 实例是不可变的，因此采用 mutate() 返回 ServerHttpRequest类型的一个 builder 类
                ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(httpHeaders -> {
                    httpHeaders.set(UserConstant.USER_ID_KEY, userInfoDTO.getUserId());
                    httpHeaders.set(UserConstant.USER_NAME_KEY, userInfoDTO.getUsername());
                    httpHeaders.set(UserConstant.USER_REAL_NAME_KEY, userInfoDTO.getRealName());
                }).build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                return chain.filter(newExchange);
            }
            return chain.filter(exchange);
        };
    }

    private boolean requiresTokenValidation(String requestPath, String blackPathPre) {
        return requestPath.startsWith(blackPathPre);
    }

    private boolean validateToken(String token) {
        // todo token 验证逻辑
        return true;
    }
}
