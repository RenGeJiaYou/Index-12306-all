package com.sjj.ticketservice.toolkit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类，主要解决日期数据的加减问题
 *
 * @author Island_World
 */

@Slf4j
public final class DateUtil {
    /**
     * 计算时差，精确到分钟
     *
     * @param startTime 开始时间 2022-10-01 00:00:00
     * @param endTime   结束时间 2022-10-01 12:23:00
     * @return 12:23
     */
    public static String calculateHourDifference(Date startTime, Date endTime) {
        LocalDateTime start = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(start, end);
        return String.format("%02d:%02d",
                duration.toHours(),
                duration.toMinutes() % 60);
    }

    /**
     * 日期转换为列车行驶开始时间和结束时间
     *
     * @param date    时间
     * @param pattern 日期格式
     * @return 日期格式对应的时间字符串
     */
    public static String convertDateToLocalTime(Date date,String pattern){
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);

    }

    @SneakyThrows
    public static void main(String[] args) {
        String startTimeStr = "2022-10-01 01:00:00";
        String endTimeStr = "2022-10-01 12:23:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = formatter.parse(startTimeStr);
        Date endTime = formatter.parse(endTimeStr);
        String calculateHourDifference = calculateHourDifference(startTime, endTime);
        log.info("开始时间：{}，结束时间：{}，两个时间相差时分：{}", startTimeStr, endTimeStr, calculateHourDifference);
    }
}
