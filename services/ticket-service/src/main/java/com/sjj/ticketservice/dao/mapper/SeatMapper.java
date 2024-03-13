package com.sjj.ticketservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjj.ticketservice.dao.entity.SeatDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 座位持久层
 *
 * @author Island_World
 */

public interface SeatMapper extends BaseMapper<SeatDO> {
    /**
     * 获取列车车厢余票集合
     */
    List<Integer> listSeatRemainingTicket(@Param("seatDO")SeatDO seatDO,
                                          @Param("trainCarriageList")List<String> trainCarriageList);
}
