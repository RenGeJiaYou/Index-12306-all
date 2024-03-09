/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * 判断结果是否为空或空串
     * @param cacheVal
     * @return
     */
    public static boolean isNullOrBlank(Object cacheVal) {
        return cacheVal == null || (cacheVal instanceof String && Strings.isNullOrEmpty((String) cacheVal));
    }
}
