package com.sjj.designpatternspringbootstarter.config.builder;

/*
 * 建造者模式
 * @Author: Island_World
 *
 */

import java.io.Serializable;

public interface Builder<T> extends Serializable {
    /**
     * 构建方法
     *
     * @return 构建后的对象
     */
    T build();
}
