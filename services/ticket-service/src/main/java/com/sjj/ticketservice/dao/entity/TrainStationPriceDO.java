package com.sjj.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 列车站点价格实体
 *
 * @author Island_World
 */
@Data
@TableName("t_train_station_price")
public class TrainStationPriceDO {
    /**
     * id
     */
    private Long id;

    /**
     * 车次id
     */
    private Long trainId;

    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 车票价格
     */
    private Integer price;
}
