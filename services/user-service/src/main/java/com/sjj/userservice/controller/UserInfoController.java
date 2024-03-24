package com.sjj.userservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.userservice.dto.req.UserRegisterReqDTO;
import com.sjj.userservice.dto.resp.UserQueryRespDTO;
import com.sjj.userservice.dto.resp.UserRegisterRespDTO;
import com.sjj.userservice.service.UserLoginService;
import com.sjj.userservice.service.UserService;
import com.sjj.webspringbootstarter.Results;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录控制层次
 *
 * @author Island_World
 */
@RestController
@RequiredArgsConstructor
public class UserInfoController {
    private final UserLoginService userLoginService;
    private final UserService userService;

    @GetMapping("/api/user-service/query")

    public Result<UserQueryRespDTO>queryUsername(@RequestParam("username") @NotEmpty String username){
        return Results.success(userService.queryUserByUsername(username));
    }

    @GetMapping("/api/user-service/has-username")
    public Result<Boolean>hasUserName(@RequestParam("username") @NotEmpty String username){
        return Results.success(userLoginService.hasUsername(username));
    }

    @PostMapping("/api/user-service/register")
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO req){
        return Results.success(userLoginService.register(req));
    }
}
