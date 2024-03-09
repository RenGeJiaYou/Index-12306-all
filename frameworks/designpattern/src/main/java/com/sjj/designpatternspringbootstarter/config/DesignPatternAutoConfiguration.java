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

package com.sjj.designpatternspringbootstarter.config;

import com.sjj.basespringbootstarter.config.ApplicationBaseAutoConfiguration;
import com.sjj.designpatternspringbootstarter.chain.AbstractChainContext;
import com.sjj.designpatternspringbootstarter.strategy.AbstractStrategyChoose;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

/*
 * 自动配置类
 *
 * @ImportAutoConfiguration 用于导入 ApplicationBaseAutoConfiguration 自动配置类到本类中。
 * 当 Spring Boot 在启动时，会自动加载 ApplicationBaseAutoConfiguration 类中定义的 Bean。
 * 这个注解通常用在自定义的自动配置类中，以便将其他的自动配置类引入到当前的配置类中。
 * 这种方式可以帮助你更好地组织和管理你的自动配置类，尤其是当你有很多自动配置类需要被引入时。
 * @author Island_World
 */
@ImportAutoConfiguration(ApplicationBaseAutoConfiguration.class)
public class DesignPatternAutoConfiguration {
    /**
     * 策略模式选择器
     * */
    @Bean
    public AbstractStrategyChoose abstractStrategyChoose(){
        return new AbstractStrategyChoose();
    }

    /**
     * 责任链上下文
     * */
    @Bean
    public AbstractChainContext abstractChainContext(){return new AbstractChainContext();}
}
