package com.sjj.cachespringbootstarter.toolkit;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 缓存工具类
 *
 * @author Island_World
 */

public final class CacheUtil {
    private static final String SPLICING_OPERATOR = "_";

    /**
     * 构建缓存 key, 过滤掉空字符串 “” 后以 "_" 连接
     * @param keys
     * @return 以 "_" 连接的 keys 字符串
     */
    public static String buildKey(String... keys){
        Stream.of(keys)
                .forEach(each -> Optional
                        .ofNullable(Strings.emptyToNull(each))
                        .orElseThrow(() -> new RuntimeException("构建缓存 key 不允许为空")));
        return Joiner.on(SPLICING_OPERATOR).join(keys);
    }

    /**
     * 判断结果是否为空或空的字符串
     * @param cacheVal
     * @return
     */
    public static boolean isNullOrBlank(Object cacheVal) {
        return cacheVal == null || (cacheVal instanceof String && Strings.isNullOrEmpty((String) cacheVal));
    }
}
