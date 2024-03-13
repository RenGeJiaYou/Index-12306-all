package com.sjj.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sjj.databasespringbootstarter.base.BaseDO;
import lombok.Data;

/**
 * 车厢实体类
 *
 * @author Island_World
 */

@Data
@TableName("t_carriage")
public class CarriageDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 列车id
     */
    private Long trainId;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 车厢类型
     */
    private Integer carriageType;

    /**
     * 座位数
     */
    private Integer seatCount;
}
