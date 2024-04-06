package com.sjj.userservice.controller;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.userservice.dto.req.PassengerRemoveReqDTO;
import com.sjj.userservice.dto.req.PassengerReqDTO;
import com.sjj.userservice.dto.resp.PassengerRespDTO;
import com.sjj.userservice.service.PassengerService;
import com.sjj.webspringbootstarter.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Result<List<PassengerRespDTO>> listPassengerQueryByUsername(String username){
        return Results.success(passengerService.listPassengerQueryByUsername(username));
    }

    /**
     * 根据用户名及乘车人 ID 列表查询乘车人列表
     */
    @GetMapping("/api/user-service/passenger/query/ids")
    public Result<List<PassengerRespDTO>> listPassengerQueryByUsername(@RequestParam("username")String username, @RequestParam("username") List<Long> ids){
        return Results.success(passengerService.listPassengerQueryByUsername(username));
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

    /**
     * 删除乘车人
     */
    @PostMapping("/api/user-service/passenger/remove")
    public Result<Void> removePassenger(@RequestBody PassengerRemoveReqDTO requestParam){
        passengerService.removePassenger(requestParam);
        return Results.success();
    }
}
