package com.sjj.ticketservice.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 座位类型和座位数量实体
 *
 * @author Island_World
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatTypeCountDTO {
    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 座位类型 - 对应数量
     */
    private Integer seatCount;
}
