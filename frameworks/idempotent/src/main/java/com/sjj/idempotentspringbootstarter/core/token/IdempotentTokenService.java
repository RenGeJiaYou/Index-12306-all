package com.sjj.idempotentspringbootstarter.core.token;/*
 * @author: Island_World
 *
 */

import com.sjj.idempotentspringbootstarter.core.IdempotentExecuteHandler;

/**
 * 参数方式验证幂等
 *
 * @author Island_World
 */

public interface IdempotentTokenService extends IdempotentExecuteHandler{
    /**
     * 创建幂等验证 Token */
    String createToken();
}
