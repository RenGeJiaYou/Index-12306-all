package com.sjj.userspringbootstarter.core;

/*
 * 用户上下文拦截器，请求存在 token 时拦截
 *
 * @Author Island_World
 */

import com.sjj.basespringbootstarter.constant.UserConstant;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URLDecoder;

public class UserTransmitFilter implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest =(HttpServletRequest)servletRequest;
        // 检查 HTTP 请求中有无 token
        String userId = httpServletRequest.getHeader(UserConstant.USER_NAME_KEY);
        if (Strings.hasText(userId)){
            String userName = httpServletRequest.getHeader(UserConstant.USER_NAME_KEY);
            String realName = httpServletRequest.getHeader(UserConstant.USER_REAL_NAME_KEY);
            if (Strings.hasText(userName)){
                userName = URLDecoder.decode(userName, "UTF-8");
            }
            if (Strings.hasText(realName)){
                realName = URLDecoder.decode(realName, "UTF-8");
            }
            String token = httpServletRequest.getHeader(UserConstant.USER_TOKEN_KEY);

            // 将用户信息存储到 UserContext 中
            UserInfoDTO userInfoDTO = UserInfoDTO.builder().userId(userId).username(userName).realName(realName).token(token).build();
            UserContext.setUser(userInfoDTO);
        }
        try{
            // 将请求和响应传递给过滤器链中的下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            // 为了防止SpringMVC复用线程，防止用户拿到其他用户的信息；而且也可以避免OOM
            UserContext.removeUser();
        }
    }
}
