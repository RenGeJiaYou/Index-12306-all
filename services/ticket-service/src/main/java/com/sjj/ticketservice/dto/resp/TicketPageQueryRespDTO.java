package com.sjj.ticketservice.dto.resp;

import com.sjj.ticketservice.dto.domain.TicketListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 车票分页查询响应参数。用户购票时选定起止站和日期后，会响应一堆可选的车次集合
 *
 * @author Island_World
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPageQueryRespDTO{
    /**
     * 车次集合数据
     */
    private List<TicketListDTO> trainList;

    /**
     * 车次类型：D-动车 Z-直达 复兴号等
     */
    private List<Integer> trainBrandList;

    /**
     * 出发车站
     */
    private List<String> departureStationList;

    /**
     * 到达车站
     */
    private List<String> arrivalStationList;

    /**
     * 车次席别
     */
    private List<Integer> seatClassTypeList;
}
