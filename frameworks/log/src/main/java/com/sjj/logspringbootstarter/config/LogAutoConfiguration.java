package com.sjj.logspringbootstarter.config;

import com.sjj.logspringbootstarter.annotation.ILog;
import com.sjj.logspringbootstarter.core.ILogPrintAspect;
import org.springframework.context.annotation.Bean;

/**
 * Log 子模块自动配置类
 *
 * @Author Island_World
 */

public class LogAutoConfiguration {
    /**
     * {@link ILog} 日志打印 AOP 切面
     */
    @Bean
    public ILogPrintAspect iLogPrintAspect() {
        return new ILogPrintAspect();
    }
}
