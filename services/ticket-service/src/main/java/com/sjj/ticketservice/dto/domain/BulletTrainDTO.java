package com.sjj.ticketservice.dto.domain;

import lombok.Data;

/**
 * 动车实体
 *
 * @author Island_World
 */
@Data
public class BulletTrainDTO {
    /**
     * 商务座数量
     */
    private Integer businessSeatQuantity;

    /**
     * 商务座候选标识
     */
    private Boolean businessSeatCandidate;

    /**
     * 商务座价格
     */
    private Float businessSeatPrice;

    /**
     * 一等座数量
     */
    private Integer firstSeatQuantity;

    /**
     * 一等座候选标识
     */
    private Boolean firstSeatCandidate;

    /**
     * 一等座价格
     */
    private Float firstSeatPrice;

    /**
     * 二等座数量
     */
    private Integer secondSeatQuantity;

    /**
     * 二等座候选标识
     */
    private Boolean secondSeatCandidate;

    /**
     * 二等座价格
     */
    private Float secondSeatPrice;
}
