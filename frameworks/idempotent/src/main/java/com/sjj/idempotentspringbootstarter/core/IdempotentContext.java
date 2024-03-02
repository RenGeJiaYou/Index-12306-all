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
     * @return 当前调用线程的上下文
     * */
    public static Map<String,Object> get(){
        return CONTEXT.get();
    }

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
     * 将 context 新增/追加到 CONTEXT
     * */
    private static void putContext(Map<String, Object> context) {
        Map<String, Object> threadContext = get();
        if (CollUtil.isNotEmpty(threadContext)) {
            threadContext.putAll(context);
            return;
        }
        CONTEXT.set(context);
    }

    //
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
