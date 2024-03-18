package com.sjj.ticketservice.dto.resp;

import lombok.Data;

/**
 * 地区&站点查询响应参数
 *
 * @author Island_World
 */
@Data
public class RegionStationQueryRespDTO {
    /**
     * 名称
     */
    private String name;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 拼音
     */
    private String spell;
}
