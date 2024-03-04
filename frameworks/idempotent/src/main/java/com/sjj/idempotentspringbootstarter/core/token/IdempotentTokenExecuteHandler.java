package com.sjj.idempotentspringbootstarter.core.token;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Strings;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.conventionspringbootstarter.errorcode.BaseErrorCode;
import com.sjj.conventionspringbootstarter.exception.ClientException;
import com.sjj.idempotentspringbootstarter.config.IdempotentProperties;
import com.sjj.idempotentspringbootstarter.core.AbstractIdempotentExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.IdempotentParamWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

/**
 * 基于 Token 验证幂等性,通常应用于 RestAPI 方法
 *
 * @author Island_World
 */

// @RequiredArgsConstructor 生成一个包含所有未初始化的 final 字段和 @NonNull 字段的构造函数。
@RequiredArgsConstructor
public class IdempotentTokenExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentTokenService {
    private final DistributedCache distributedCache;
    private final IdempotentProperties idempotentProperties;

    public static final String TOKEN_KEY = "token";
    public static final String TOKEN_PREFIX_KEY = "idempotent:token:";
    public static final long TOKEN_EXPIRED_TIME = 6000;

    /**
     * @Return 构造幂等验证过程中所需要的参数包装器
     */
    @Override
    public IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        return new IdempotentParamWrapper().setJoinPoint(joinPoint);

    }

    /**
     * handler() 中加可重入锁后，将锁放入上下文
     */
    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader(TOKEN_KEY);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(TOKEN_KEY);
            if (StrUtil.isBlank(token)) {
                throw new ClientException(BaseErrorCode.IDEMPOTENT_TOKEN_NULL_ERROR);
            }
        }
        Boolean deleteFlag = distributedCache.delete(token);
        if (!deleteFlag) {
            String ErrorMsg = StrUtil.isNotBlank(wrapper.getIdempotent().message())
                    ? wrapper.getIdempotent().message()
                    : BaseErrorCode.IDEMPOTENT_TOKEN_DELETE_ERROR.message();
            throw new ClientException(ErrorMsg, BaseErrorCode.IDEMPOTENT_TOKEN_DELETE_ERROR);
        }
    }

    /**
     * 先根据配置文件中的前缀和过期时间创建 token，没有则采用默认值
     * 将 token 存到 redis 中并返回
     *
     * @return
     */
    @Override
    public String createToken() {
        String token = Optional.ofNullable(Strings.emptyToNull(idempotentProperties.getPrefix())).orElse(TOKEN_PREFIX_KEY) + UUID.randomUUID();
        distributedCache.put(token, "", Optional.ofNullable(idempotentProperties.getTimeout()).orElse(TOKEN_EXPIRED_TIME));
        return token;
    }
}
