package com.sjj.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sjj.databasespringbootstarter.base.BaseDO;
import lombok.Data;

/**
 * 地区表
 *
 * @author Island_World
 */
@Data
@TableName("t_region")
public class RegionDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区全名
     */
    private String fullName;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 地区首字母
     */
    private String initial;

    /**
     * 拼音
     */
    private String spell;

    /**
     * 热门标识
     */
    private Integer popularFlag;
}
