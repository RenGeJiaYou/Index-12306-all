package com.sjj.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sjj.databasespringbootstarter.base.BaseDO;
import lombok.Data;

/**
 * 座位实体类
 *
 * @author Island_World
 */
@Data
@TableName("t_seat")
public class SeatDO extends BaseDO {
    /**
     * id 主键
     */
    private Long id;

    /**
     * 列车id，外键
     */
    private Long trainId;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 起始站
     */
    private String startStation;

    /**
     * 终点站
     */
    private String endStation;

    /**
     * 座位状态
     */
    private Integer seatStatus;

    /**
     * 车票价格
     */
    private Integer price;
}
