package com.sjj.userservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.userservice.dto.req.UserLoginReqDTO;
import com.sjj.userservice.dto.resp.UserLoginRespDTO;
import com.sjj.userservice.service.UserLoginService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制层
 *
 * @author Island_World
 */
@RestController
@RequiredArgsConstructor
public class UserLoginController {
    private final UserLoginService userLoginService;

    @PostMapping("/api/user-service/v1/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO req) {
        return Results.success(userLoginService.login(req));
    }

    @GetMapping("api/user-service/check-login")
    public Result<UserLoginRespDTO> checkLogin(@RequestParam String accessToken) {
        UserLoginRespDTO result = userLoginService.checkLogin(accessToken);
        return Results.success(result);
    }

    @GetMapping("/api/user-service/logout")
    public Result<Void> logout(@RequestParam(required = false) String accessToken) {
        userLoginService.logout(accessToken);
        return Results.success();
    }
}
