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

package com.sjj.designpatternspringbootstarter.chain;

/**
 * 抽象责任链上下文
 *
 * @author Island_World
 */

import com.sjj.basespringbootstarter.ApplicationContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.core.Ordered;

import java.util.*;
import java.util.stream.Collectors;

public final class AbstractChainContext<T> implements CommandLineRunner {
    // 保存责任链处理类的容器从 List 改为了 Map，这样可以方便扩展更多的不同业务责任链子类。
    private final Map<String, List<AbstractChainHandler>> abstractChainHandlerContainer = new HashMap<>();

    /**
     * 责任链组件执行
     *
     * @param mark         责任链组件标识
     * @param requestParam 请求参数
     */
    public void handler(String mark,T requestParam){
        // 根据 handler 获取对应的一系列责任链处理类
        List<AbstractChainHandler> handlers = abstractChainHandlerContainer.get(mark);
        if(CollectionUtils.isEmpty(handlers)){
            throw new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.",mark));
        }
        // 依次执行责任链处理类的 handler 方法
        handlers.forEach(each -> each.handler(requestParam));
    }

    /**
     * run() 将在 Application 启动后执行一些初始化任务，例如初始化数据库，启动定时任务，加载缓存数据等。
     * 在这里，我们将【从 SpringBoot Application Context 中找到的】责任链处理类加载到 abstractChainHandlerContainer 容器中。
     * */
    @Override
    public void run(String... args) throws Exception {
        // 获取到了所有的 AbstractChainHandler 实现类，然后将它们按照 mark 分组，放到 abstractChainHandlerContainer 容器中。
        Map<String, AbstractChainHandler> chainFilterMap = ApplicationContextHolder
                .getBeansOfType(AbstractChainHandler.class);

        chainFilterMap.forEach((beanName,bean)->{
            List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(bean.mark());
            if(CollectionUtils.isEmpty(abstractChainHandlers)){
                abstractChainHandlers = new ArrayList();
            }
            // 添加 bean 并按序重排
            abstractChainHandlers.add(bean);
            List<AbstractChainHandler>actualAbstractChainHandlers = abstractChainHandlers
                    .stream()
                    .sorted(Comparator.comparing(Ordered::getOrder))
                    .collect(Collectors.toList());
            abstractChainHandlerContainer.put(bean.mark(),actualAbstractChainHandlers);
        });
    }
}
