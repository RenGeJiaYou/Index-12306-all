package com.sjj.cachespringbootstarter;

import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

/**
 * 缓存接口
 *
 * @author Island_World
 */

public interface Cache {
    /**
     * 获取缓存,并返回 clazz 类型的实例对象
     * */
    <T> T get(@NotBlank String key, Class<T> clazz);

    /**
     * 放置缓存
     **/
    void put(@NotBlank String key, Object value);

    /**
     * 如果 keys 全部不存在，则新增，返回 true，反之 false
     * */
    Boolean putIfAllAbsent(Collection<String> keys);

    /**
     * 删除缓存单个 key
     * */
    Boolean delete(@NotBlank String key);

    /**
     * 删除 keys，返回删除数量
     * */
    Long delete(@NotBlank Collection<String> keys);

    /**
     * 判断 key 是否存在
     * */
    Boolean hasKey(@NotBlank String key);

    /**
     * 获取缓存组件实例
     * */
    Object getInstance();
}
