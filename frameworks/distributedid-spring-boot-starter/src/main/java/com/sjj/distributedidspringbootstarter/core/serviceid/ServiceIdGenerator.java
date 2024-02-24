package com.sjj.distributedidspringbootstarter.core.serviceid;

import com.sjj.distributedidspringbootstarter.core.IdGenerator;
import com.sjj.distributedidspringbootstarter.core.snowflake.SnowflakeIdInfo;

/**
 * 业务 ID 生成器<p>
 * &#064;Author:  Island_World
 *
 */

public interface ServiceIdGenerator extends IdGenerator {

        /**
         * 根据 {@param serviceId} 生成雪花算法 ID
         */
        default long nextId(long serviceId) {
            return 0L;
        }

        /**
         * 根据 {@param serviceId} 生成雪花算法 ID
         */
        default long nextId(String serviceId) {
            return 0L;
        }

        /**
         * 根据 {@param serviceId} 生成字符串类型雪花算法 ID
         */
        default String nextIdStr(long serviceId) {
            return null;
        }

        /**
         * 根据 {@param serviceId} 生成字符串类型雪花算法 ID
         */
        default String nextIdStr(String serviceId) {
            return null;
        }

        /**
         * 解析雪花算法
         */
        SnowflakeIdInfo parseSnowflakeId(long snowflakeId);
    }

