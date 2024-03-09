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

package com.sjj.idempotentspringbootstarter.core;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * 幂等上下文
 *
 * @author Island_World
 */

public class IdempotentContext {
    private static final ThreadLocal<Map<String,Object>> CONTEXT = new ThreadLocal<>();

    /**
     * @return 获取 ThreadLocal 容器中的 Map 实例
     * */
    private static Map<String,Object> get(){
        return CONTEXT.get();
    }

    /**
     * 获取 Map 上下文实例中对应 key 的 value
     * @param key
     * @return 当前 Context 实例中对应 key 的 value
     */
    public static Object getKey(String key){
        Map<String, Object> context = get();
        if(CollUtil.isNotEmpty(context)){
            return context.get(key);
        }
        return null;
    }

    public static String getString(String key){
        Object actual = getKey(key);
        return Optional.ofNullable(actual).map(Object::toString).orElse(null);
    }

    /**
     * 将 context（一个 Map 实例） 新增/追加到 CONTEXT （一个容纳 Map 类型的 ThreadLocal 容器）
     * */
    private static void putContext(Map<String, Object> context) {
        Map<String, Object> threadContext = get();
        if (CollUtil.isNotEmpty(threadContext)) {
            threadContext.putAll(context);
            return;
        }
        CONTEXT.set(context);
    }

    /**
     * 将 &lt;k,v&gt; 实参写入 ThreadLocal 容器中的 Map 中<p>
     * 再将这个 Map 写回到 ThreadLocal 容器中
     *
     * @param key
     * @param val
     */
    public static void put(String key,Object val){
        Map<String, Object> context = get();
        if (CollUtil.isEmpty(context)) {
            context = Maps.newHashMap();
        }
        context.put(key, val);
        putContext(context);
    }

    public static void clean(){
        CONTEXT.remove();
    }
}
