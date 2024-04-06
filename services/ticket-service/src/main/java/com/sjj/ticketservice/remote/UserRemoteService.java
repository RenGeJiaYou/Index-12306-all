package com.sjj.ticketservice.remote;

import com.sjj.conventionspringbootstarter.result.Result;
import com.sjj.ticketservice.remote.dto.PassengerRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户远程服务调用
 *
 * @author Island_World
 */

@FeignClient("user-service")
public interface UserRemoteService {
    @GetMapping("api/user-service/passenger/query/ids")
    Result<List<PassengerRespDTO>> listPassengerQueryByIds(@RequestParam("username") String username,
                                                           @RequestParam("ids") List<String> ids);
}
