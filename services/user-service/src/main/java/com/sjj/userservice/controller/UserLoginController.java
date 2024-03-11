package com.sjj.userservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.userservice.dto.req.UserRegisterReqDTO;
import com.sjj.userservice.dto.resp.UserRegisterRespDTO;
import com.sjj.userservice.service.UserLoginService;
import com.sjj.webspringbootstarter.Results;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户登录控制层次
 *
 * @author Island_World
 */
@Controller
public class UserLoginController {
    private UserLoginService userLoginService;

    @GetMapping("/api/user-service/has-username")
    public Result<Boolean>hasUserName(@RequestParam("username") @NotEmpty String username){
        return Results.success(userLoginService.hasUsername(username));
    }

    @PostMapping("/api/user-service/register")
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO req){
        return Results.success(userLoginService.register(req));
    }
}
