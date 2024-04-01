package com.sjj.gatewayservice.config;

import lombok.Data;

/**
 * 过滤器配置
 *
 * @author Island_World
 */
@Data
public class Config {
    /**
     * 黑名单路径
     */
    private String blackPathPre;
}
