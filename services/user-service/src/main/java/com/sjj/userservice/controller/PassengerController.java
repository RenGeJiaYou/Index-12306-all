package com.sjj.userservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.userservice.dto.req.PassengerReqDTO;
import com.sjj.userservice.dto.resp.PassengerRespDTO;
import com.sjj.userservice.service.PassengerService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 乘车人控制层
 *
 * @author Island_World
 */
@RestController
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    /**
     * 根据用户名查询乘车人列表
     */
    @GetMapping("/api/user-service/passenger/query")
    public Result<List<PassengerRespDTO>> listPassengerQuery(String username){
        return Results.success(passengerService.listPassengerQuery(username));
    }

    /**
     * 新增乘车人
     */
    @PostMapping("/api/user-service/passenger/save")
    public Result<Void> savePassenger(@RequestBody PassengerReqDTO requestParam){
        passengerService.savePassenger(requestParam);
        return Results.success();
    }

    /**
     * 修改乘车人
     */
    @PostMapping("/api/user-service/passenger/update")
    public Result<Void> updatePassenger(@RequestBody PassengerReqDTO requestParam){
        passengerService.updatePassenger(requestParam);
        return Results.success();
    }
}
