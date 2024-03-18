package com.sjj.ticketservice.dto.req;


import com.sjj.conventionspringbootstarter.page.PageRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 车票分页查询请求参数：出发城市，目的城市，出发站，目的站，出发日期
 *
 * @author Island_World
 */
@Data
public class TicketPageQueryReqDTO extends PageRequest {
    /**
     * 出发城市
     */
    private String fromCity;

    /**
     * 目的城市
     */
    private String toCity;

    /**
     * 出发日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;

    /**
     * 出发站点
     */
    private String departureStation;

    /**
     * 到达站点
     */
    private String arrivalStation;
}
