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

package com.sjj.commonspringbootstarter.toolkit;

/*
 * 对象属性复制工具类
 *
 * @author Island_World
 */

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.*;

import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapEmptyString;
import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapNull;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BeanUtil {
    protected static Mapper BEAN_MAPPER_BUILDER;

/*     static{} 是「静态初始化块」，用于初始化静态变量。
     在类加载时执行且只执行一次，并且在任何其他静态方法、静态块和构造函数之前执行。*/
    static {
        BEAN_MAPPER_BUILDER = DozerBeanMapperBuilder.buildDefault();
    }

    /**
     * 属性复制
     *
     * @param source 数据对象
     * @param target 目标对象
     * @param <T>
     * @param <S>
     * @return 转换后对象
     */
    public static <T,S> T convert(S source,T target){
        Optional.ofNullable(source)
                .ifPresent(each->BEAN_MAPPER_BUILDER.map(each,target));

        return target;
    }

    /**
     * 拷贝非空且非空串属性
     *
     * @param source 数据源
     * @param target 指向源
     */
    public static void convertIgnoreNullAndBlank(Object source,Object target) {
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilders(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), target.getClass(),mapNull(false),mapEmptyString(false));
            }
        }).build();
        mapper.map(source,target);
    }

    /**
     * 拷贝非空且非空串属性
     *
     * @param source 数据源
     * @param target 指向源
     */
    public static void convertIgnoreNull(Object source,Object target) {
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilders(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), target.getClass(),mapNull(false));
            }
        }).build();
        mapper.map(source,target);
    }


    /**
     * 复制单个对象
     *
     * @param source 数据对象
     * @param clazz  复制目标类型
     * @param <T>
     * @param <S>
     * @return 转换后对象
     */
    public static <T,S> T convert(S source,Class<T> clazz){
        return Optional.ofNullable(source)
                .map(each->BEAN_MAPPER_BUILDER.map(each,clazz))
                .orElse(null);
    }

    /**
     * 复制多个对象(List)
     *
     * @param sources 数据对象
     * @param clazz   复制目标类型
     * @param <T>
     * @param <S>
     * @return 转换后对象集合
     */
    public static<T,S> List<T> convert(List<S> sources, Class<T> clazz){
     return Optional.ofNullable(sources)
             .map(each->{
                 List<T> targetList = new ArrayList<>(each.size());
                 each.stream()
                         .forEach(item -> targetList.add(BEAN_MAPPER_BUILDER.map(item,clazz)));
                 return targetList;
             })
             .orElse(null);
    }

    /**
     * 复制多个对象(Set)
     *
     * @param sources 数据对象
     * @param clazz   复制目标类型
     * @param <T>
     * @param <S>
     * @return 转换后对象集合
     */
    public static<T,S> Set<T> convert(Set<S> sources, Class<T> clazz){
        return Optional.ofNullable(sources)
                .map(each->{
                    Set<T> targetList = new HashSet<>(each.size());
                    each.stream()
                            .forEach(item -> targetList.add(BEAN_MAPPER_BUILDER.map(item,clazz)));
                    return targetList;
                })
                .orElse(null);
    }

    /**
     * 复制多个对象(数组)
     *
     * @param sources 数据对象
     * @param clazz   复制目标类型
     * @param <T>
     * @param <S>
     * @return 转换后对象集合
     */
    public static<T,S> T[] convert(S[] sources, Class<T> clazz){
        return Optional.ofNullable(sources)
                .map(each->{
                    @SuppressWarnings("unchecked")
                    T[] targetList = (T[])Array.newInstance(clazz,sources.length);
                    for (int i = 0; i < targetList.length; i++) {
                        targetList[i] = BEAN_MAPPER_BUILDER.map(sources[i],clazz);
                    }
                    return targetList;
                })
                .orElse(null);
    }
}

