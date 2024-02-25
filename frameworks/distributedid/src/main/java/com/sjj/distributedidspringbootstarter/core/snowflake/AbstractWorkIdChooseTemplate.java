package com.sjj.distributedidspringbootstarter.core.snowflake;

import cn.hutool.core.date.SystemClock;
import com.sjj.distributedidspringbootstarter.toolkit.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 雪花算法模板生成
 *
 * @Author Island_World
 */
@Slf4j
public abstract class AbstractWorkIdChooseTemplate {

    /**
     * 是否使用 {@link SystemClock} 获取当前时间戳
     * <p>
     * 该注解会搜索 Spring 配置文件，未定义就返回空
     */
    @Value("${framework.distributed.id.snowflake.is-use-system-clock:false}")
    private boolean isUseSystemClock;

    /**
     * 一个模板方法，根据自定义策略获取 WorkId 生成器
     *
     * @return WorkIdWrapper
     */
    protected abstract WorkIdWrapper chooseWorkId();


    /**
     * 选择 WorkId 并初始化雪花
     * */
    public void chooseAndInit(){
        // 模板方法模式: 通过抽象方法获取 WorkId 包装器创建雪花算法
        WorkIdWrapper workIdWrapper = chooseWorkId();
        Long workId = workIdWrapper.getWorkId();
        Long dataCenterId = workIdWrapper.getDataCenterId();
        Snowflake snowflake = new Snowflake(workId, dataCenterId, isUseSystemClock);
        log.info("Snowflake type: {}, workId: {}, dataCenterId: {}", this.getClass().getSimpleName(), workId, dataCenterId);
        SnowflakeIdUtil.initSnowflake(snowflake);
    }
}
