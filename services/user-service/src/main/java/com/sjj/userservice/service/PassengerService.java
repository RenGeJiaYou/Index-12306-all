package com.sjj.userservice.service;

import com.sjj.userservice.dto.req.PassengerRemoveReqDTO;
import com.sjj.userservice.dto.req.PassengerReqDTO;
import com.sjj.userservice.dto.resp.PassengerRespDTO;

import java.util.List;

/**
 * 乘车人 Service 层
 *
 * @author Island_World
 */

public interface PassengerService {

    /**
     * 根据用户名查询乘车人列表
     *
     * @param username 用户名（注册时已确保不重名）
     * @return 该用户保存的乘车人列表
     */
    List<PassengerRespDTO> listPassengerQueryByUsername(String username);

    /**
     * 根据乘车人 ID 集合查询乘车人列表
     *
     * @param username 用户名
     * @param ids 乘车人 ID 集合
     * @return 乘车人返回列表
     */
    List<PassengerRespDTO> listPassengerQueryByIds(String username,List<Long> ids);

    /**
     * 保存新的乘车人
     *
     * @param requestParam 乘车人 post 数据
     */
    void savePassenger(PassengerReqDTO requestParam);

    /**
     * 修改乘车人
     *
     * @param requestParam 乘车人 post 数据
     */
    void updatePassenger(PassengerReqDTO requestParam);

    void removePassenger(PassengerRemoveReqDTO requestParam);
}
