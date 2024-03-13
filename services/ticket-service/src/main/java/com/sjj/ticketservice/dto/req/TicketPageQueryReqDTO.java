package com.sjj.ticketservice.dto.req;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 车票分页查询请求参数：出发城市，目的城市，出发站，目的站，出发日期
 *
 * @author Island_World
 */
//@Data
public class TicketPageQueryReqDTO extends Page {
    /**
     * 出发地
     */
    private String fromStation;

    /**
     * 目的地
     */
    private String toStation;

    /**
     * 出发日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;
}
