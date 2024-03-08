package com.sjj.idempotentspringbootstarter.core.token;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Token 幂等验证 Controller
 *
 * @author Island_World
 */

@RestController
@RequiredArgsConstructor
public class IdempotentTokenController {
    private final IdempotentTokenService idempotentTokenService;

    @GetMapping("/token")
    public Result<String> createToken(){
        return Results.success(idempotentTokenService.createToken());
    }
}
