package com.sjj.distributedidspringbootstarter.core;

/**
 * ID 生成器
 *
 * @author: Island_World
 *
 */

public interface IdGenerator {
    // 获取下一个 ID
    default long nextId() {return 0;}

    // 获取下一个 ID 字符串
    default String nextIdStr() {return "";}

}
