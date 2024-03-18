package com.sjj.ticketservice.dto.req;

import com.sjj.ticketservice.common.enums.RegionStationQueryTypeEnum;
import lombok.Data;

/**
 * 地区&站点查询请求参数，查询本地区有哪些车站
 *
 * @author Island_World
 */
@Data
public class RegionStationQueryReqDTO {
    /**
     * 查询方式，详见 {@link RegionStationQueryTypeEnum}
     */
    private Integer queryTYpe;

    /**
     * 名称
     */
    private String name;

}
