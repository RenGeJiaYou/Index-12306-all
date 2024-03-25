package com.sjj.ticketservice.dto.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 乘车人信息实体
 *
 * @author Island_World
 */

@Data
@Accessors(chain = true)
public class PassengerInfoDTO {
    /**
     * 乘车人 ID
     */
    private String passengerId;

    /**
     * 乘车人姓名
     */
    private String realName;

    /**
     * 乘车人证件类型
     */
    private Integer idType;

    /**
     * 乘车人证件号
     */
    private String idCard;

    /**
     * 乘车人手机号
     */
    private String phone;
}
