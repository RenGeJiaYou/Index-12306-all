package com.sjj.idempotentspringbootstarter.core.token;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.webspringbootstarter.Results;

/**
 * Token 幂等验证 Controller
 *
 * @author Island_World
 */

public class IdempotentTokenController {
    private IdempotentTokenService idempotentTokenService;

    public Result<String> createToken(){
        return Results.success(idempotentTokenService.createToken());
    }
}
