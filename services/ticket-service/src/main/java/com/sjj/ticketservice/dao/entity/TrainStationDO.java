package com.sjj.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjj.databasespringbootstarter.base.BaseDO;
import lombok.Data;

import java.util.Date;

/**
 * 列车站点表
 *
 * @author Island_World
 */

@Data
@TableName("t_train_station")
public class TrainStationDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 车次id
     */
    private Long trainId;

    /**
     * 车站id
     */
    private Long stationId;

    /**
     * 站点顺序
     */
    private String sequence;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 起始城市
     */
    private String startRegion;

    /**
     * 终点城市
     */
    private String endRegion;

    /**
     * 到站时间
     */
    private Date arrivalTime;

    /**
     * 出站时间
     */
    private Date departureTime;

    /**
     * 停留时间，单位分
     */
    private Integer stopoverTime;
}
