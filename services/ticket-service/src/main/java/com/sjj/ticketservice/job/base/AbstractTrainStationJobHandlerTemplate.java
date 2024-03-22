package com.sjj.ticketservice.job.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjj.basespringbootstarter.ApplicationContextHolder;
import com.sjj.commonspringbootstarter.toolkit.EnvironmentUtil;
import com.sjj.ticketservice.dao.entity.TrainDO;
import com.sjj.ticketservice.dao.mapper.TrainMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 抽象列车 & 车票相关定时任务
 *
 * @author Island_World
 */

public abstract class AbstractTrainStationJobHandlerTemplate extends IJobHandler {
    /**
     * 模板方法模式具体实现子类执行定时任务
     *
     * @param trainDOPageRecords 列车信息分页记录
     */
    protected abstract void actualExecute(List<TrainDO> trainDOPageRecords);

    private String getJobRequestParam() {
        return EnvironmentUtil.isDevEnvironment()
                ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("requestParam")
                : XxlJobHelper.getJobParam();
    }

    @Override
    public void execute() {
        var currentPage = 1L;
        var size = 1000L;
        var requestParam = getJobRequestParam();
        // 如果用户传入发车日期，采纳用户的意愿；若没传入，默认明天
        var dateTime = StrUtil.isNotBlank(requestParam)
                ? DateUtil.parse(requestParam, "yyyy-MM-dd")
                : DateUtil.tomorrow();
        var trainMapper = ApplicationContextHolder.getBean(TrainMapper.class);

        for (; ; currentPage++) {
            // 按照给定发车时间搜索车次，用 DateUtil 寻找一天起止时刻
            var wrapper = Wrappers.lambdaQuery(TrainDO.class)
                    .between(TrainDO::getDepartureTime, DateUtil.beginOfDay(dateTime), DateUtil.endOfDay(dateTime));
            var trainDOPage = trainMapper.selectPage(new Page<>(currentPage, size), wrapper);
            if (trainDOPage == null || CollUtil.isEmpty(trainDOPage.getRecords())) {
                break;
            }
            actualExecute(trainDOPage.getRecords());
        }
    }
}
