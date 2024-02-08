package com.sjj.basespringbootstarter;

/*
 * 一个存放单例对象的容器类
 *
 * @Author Island_World
 */

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Supplier;

import java.util.concurrent.ConcurrentHashMap;

// 构造函数为私有，表示该单例类不允许由外部类创建实例
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Singleton {
    private static final ConcurrentHashMap<String, Object> SINGLE_OBJECT_POOL = new ConcurrentHashMap();

    // 根据 key 获取单例对象
    public static <T> T get(String key) {
        Object result = SINGLE_OBJECT_POOL.get(key);
        return result == null ? null : (T) result;
    }

    // 根据 key 获取单例对象，若为null，通过 supplier 动态地（因为是泛型<T>而非具体类型，不能直接 new）构建单例对象并放入容器
    public static <T> T get(String key, Supplier<T> supplier) {
        Object result = SINGLE_OBJECT_POOL.get(key);
        // 注意，！=的优先级高于 &&
        // 本句句意为：若result为null，且supplier.get()不为null，则将supplier.get()放入容器
        if (result == null && (result = supplier.get()) != null) {
            SINGLE_OBJECT_POOL.put(key, result);
        }
        // 考虑到泛型，不能直接返回 Object 类型的 result，需要强制类型转换
        return result == null ? null : (T) result;
    }

    // 将单例对象放入容器
    public static void put(Object value){
        put(value.getClass().getName(),value);
    }

    // 将单例对象放入容器
    public static void put(String key,Object value){
        SINGLE_OBJECT_POOL.put(key,value);
    }
}
