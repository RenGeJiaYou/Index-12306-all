package com.sjj.databasespringbootstarter.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sjj.databasespringbootstarter.handler.CustomIdGenerator;
import com.sjj.databasespringbootstarter.handler.MyMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 配置类
 *
 * @Author Island_World
 */

public class MybatisPlusAutoConfiguration {
    /**
     * 定义 MySQL 分页插件配置，避免每个项目都需要重新定义。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * DO 元数据配置类，将在 insert/update 数据库时自动填充某些字段
     */
    @Bean
    public MyMetaObjectHandler myMetaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    /**
     * 自定义雪花算法生成器
     * <p>
     * 配置类中声明 @Primary 上述雪花算法生成器是主要的，不再使用默认的雪花 ID 生成器。
     */
    @Bean
    @Primary
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }
}
