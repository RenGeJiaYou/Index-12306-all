package com.sjj.designpatternspringbootstarter.config;

import com.sjj.basespringbootstarter.config.ApplicationBaseAutoConfiguration;
import com.sjj.designpatternspringbootstarter.chain.AbstractChainContext;
import com.sjj.designpatternspringbootstarter.strategy.AbstractStrategyChoose;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

/*
 * 自动配置类
 *
 * @ImportAutoConfiguration 用于导入 ApplicationBaseAutoConfiguration 自动配置类到本类中。
 * 当 Spring Boot 在启动时，会自动加载 ApplicationBaseAutoConfiguration 类中定义的 Bean。
 * 这个注解通常用在自定义的自动配置类中，以便将其他的自动配置类引入到当前的配置类中。
 * 这种方式可以帮助你更好地组织和管理你的自动配置类，尤其是当你有很多自动配置类需要被引入时。
 * @Author Island_World
 */
@ImportAutoConfiguration(ApplicationBaseAutoConfiguration.class)
public class DesignPatternAutoConfiguration {
    /**
     * 策略模式选择器
     * */
    @Bean
    public AbstractStrategyChoose abstractStrategyChoose(){
        return new AbstractStrategyChoose();
    }

    /**
     * 责任链上下文
     * */
    @Bean
    public AbstractChainContext abstractChainContext(){return new AbstractChainContext();}
}
