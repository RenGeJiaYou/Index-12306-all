package com.sjj.ticketservice.toolkit;

import com.sjj.ticketservice.dto.domain.RouteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 中间站计算工具类
 *
 * @author Island_World
 */
public final class StationCalculateUtil {
    /**
     * 计算出发站和终点站中间的站点（包含出发站和终点站）
     *
     * @param stations     所有站点数据
     * @param startStation 出发站
     * @param endStation   终点站
     * @return 出发站和终点站中间的站点（包含出发站和终点站）
     */
    public static List<RouteDTO> throughStation(List<String> stations, String startStation, String endStation) {
        List<RouteDTO> results = new ArrayList<>();
        int start = stations.indexOf(startStation);
        int end = stations.indexOf(endStation);
        if (start<0|| end<0 ||start >= end) {
            return results;
        }
        for (int i = start; i <end ; i++) {
            results.add(new RouteDTO(stations.get(i), stations.get(i + 1)));
        }
        return results;
    }
}
