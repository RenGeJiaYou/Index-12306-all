package com.sjj.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sjj.databasespringbootstarter.base.BaseDO;
import lombok.Data;

/**
 * 车站实体类
 *
 * @author Island_World
 */

@Data
@TableName("t_station")
public class StationDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 车站编码
     */
    private String code;

    /**
     * 车站名称
     */
    private String name;

    /**
     * 拼音
     */
    private String spell;

    /**
     * 地区编号
     */
    private String region;

    /**
     * 地区名称
     */
    private String regionName;
}
