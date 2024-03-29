package com.sjj.ticketservice.common.constant;

/**
 * Redis Key 定义常量类
 *
 * @author Island_World
 */

public final class RedisKeyConstant {
    /**
     * 站点查询，Key Prefix + 起始城市_终点城市_日期
     */
    public static final String REGION_TRAIN_STATION = "region_train_station:";

    /**
     * 站点余票查询，Key Prefix + 列车ID_起点站_终点站(在 Service Impl 中实现)
     */
    public static final String TRAIN_STATION_REMAINING_TICKET = "train_station_remaining_ticket:";

    /**
     * 站点详细信息查询，Key Prefix + 列车ID_起点站_终点站
     */
    public static final String TRAIN_STATION_DETAIL = "train_station_detail:";

    /**
     * 列车路线信息查询，Key Prefix + 列车ID
     */
    public static final String TRAIN_STATION_STOPOVER_DETAIL = "train_station_stopover_detail:";
}
