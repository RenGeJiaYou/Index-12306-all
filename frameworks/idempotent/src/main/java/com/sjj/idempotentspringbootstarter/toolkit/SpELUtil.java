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

package com.sjj.idempotentspringbootstarter.toolkit;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

/**
 * SpEL 表达式解析工具
 *
 * @author Island_World
 */

public class SpELUtil {
    /**
     * 首先判断是不是 SpEL 表达式，是则直接返回，不是则解析成 SpEL 再返回
     *
     * @param spel
     * @param method
     * @param contextObj
     * @return SpEL 表达式
     */
    public static Object parseKey(String spel,Method method,Object[] contextObj){
        ArrayList<String> spelFlag = Lists.newArrayList("#", "T(");
        Optional<String> optional = spelFlag.stream().filter(spel::contains).findFirst();
        if(optional.isPresent()){
            return parse(spel, method, contextObj);
        }
        return spel;
    }

    /**
     *
     * @param spel
     * @param method
     * @param contextObj
     * @return 表达式的解析值
     */
    public static Object parse(String spel, Method method, Object[] contextObj){
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(spel);
        // 获取所有形参名
        String[] params = discoverer.getParameterNames(method);
        // 一个装载{形参名:值}的容器，用来传递给 Expression 进行计算
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 有参数再行动
        if(ArrayUtil.isNotEmpty(params)){
            for(int len = 0; len < params.length; len++){
                context.setVariable(params[len], contextObj[len]);
            }
        }
        return expression.getValue(context);
    }
}
