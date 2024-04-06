package com.sjj.userservice.dto.req;

import lombok.Data;

/**
 * 乘车人添加&修改请求参数
 *
 * @author Island_World
 */
@Data
public class PassengerReqDTO {
    /**
     * 乘车人id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号码
     */
    private String idCard;

    /**
     * 优惠类型
     */
    private Integer discountType;

    /**
     * 手机号
     */
    private String phone;
}
