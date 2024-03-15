package com.sjj.ticketservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sjj.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.sjj.ticketservice.dto.resp.TicketPageQueryRespDTO;

/**
 * 车票服务接口
 *
 * @author Island_World
 */

public interface TicketService {
    /**
     * 按照条件分页查询余票
     */
    IPage<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO req);
}
