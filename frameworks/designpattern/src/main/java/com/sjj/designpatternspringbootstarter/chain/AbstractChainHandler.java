package com.sjj.designpatternspringbootstarter.chain;

/*
 * 责任链抽象接口
 *
 * @author Island_World
 */

import org.springframework.core.Ordered;

public interface AbstractChainHandler<T> extends Ordered {
    // 执行责任链逻辑
    void handler(T requestParam);

    // 责任链组件标识，用于区分不同业务的责任链
    String mark();
}
