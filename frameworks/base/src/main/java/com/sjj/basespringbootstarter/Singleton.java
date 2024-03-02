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

package com.sjj.basespringbootstarter;

/*
 * 一个存放单例对象的容器类
 *
 * @author Island_World
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
