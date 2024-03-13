package com.sjj.ticketservice.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sjj.ticketservice.dto.domain.BulletTrainDTO;
import lombok.Data;

import java.util.Date;

/**
 * 车票分页查询响应参数
 *
 * @author Island_World
 */
@Data
public class TicketPageQueryRespDTO{
    /**
     * 车次
     */
    private String trainNumber;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date departureTime;

    /**
     * 历时
     */
    private String duration;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 高铁属性
     */
    private BulletTrainDTO bulletTrain;
}
