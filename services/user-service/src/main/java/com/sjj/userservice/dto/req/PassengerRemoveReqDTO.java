package com.sjj.userservice.dto.req;

import lombok.Data;

/**
 * 乘车人移除请求参数
 *
 * @author Island_World
 */

@Data
public class PassengerRemoveReqDTO {
    /**
     * 乘车人id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;
}
