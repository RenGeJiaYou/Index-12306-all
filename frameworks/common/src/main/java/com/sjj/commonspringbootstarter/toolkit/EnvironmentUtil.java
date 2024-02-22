package com.sjj.commonspringbootstarter.toolkit;

import com.sjj.basespringbootstarter.ApplicationContextHolder;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * 环境工具类
 *
 * @Author Island_World
 */

public class EnvironmentUtil {

    private static List<String> ENVIRONMENT_LIST = new ArrayList<>();

    static {
        ENVIRONMENT_LIST.add("dev");
        ENVIRONMENT_LIST.add("test");
    }

    // 判断当前是否为开发环境
    public static boolean isDevEnvironment() {
        ConfigurableEnvironment configurableEnvironment = ApplicationContextHolder.getBean(ConfigurableEnvironment.class);
        // 获取当前活动的 spring 配置文件
        String propertyActive = configurableEnvironment.getProperty("spring.profiles.active", "dev");
        return ENVIRONMENT_LIST
                .stream()
                .filter(each -> propertyActive.contains(each))
                .findFirst()
                .isPresent();
    }

    // 判断当前是否为生产环境
    public static boolean isProdEnvironment() {
        ConfigurableEnvironment configurableEnvironment = ApplicationContextHolder.getBean(ConfigurableEnvironment.class);
        // 获取当前活动的 spring 配置文件
        String propertyActive = configurableEnvironment.getProperty("spring.profiles.active", "dev");
        return ENVIRONMENT_LIST
                .stream()
                .noneMatch(propertyActive::contains);
    }
}
