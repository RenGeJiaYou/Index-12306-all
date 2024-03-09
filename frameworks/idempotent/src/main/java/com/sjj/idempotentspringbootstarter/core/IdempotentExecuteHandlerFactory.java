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

package com.sjj.idempotentspringbootstarter.core;

import com.sjj.basespringbootstarter.ApplicationContextHolder;
import com.sjj.idempotentspringbootstarter.core.param.IdempotentParamExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.spel.IdempotentSpELByMQExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import com.sjj.idempotentspringbootstarter.core.token.IdempotentTokenExecuteHandler;
import com.sjj.idempotentspringbootstarter.enums.IdempotentSceneEnum;
import com.sjj.idempotentspringbootstarter.enums.IdempotentTypeEnum;

/**
 * 幂等切面类工厂
 *
 * @author Island_World
 */

public class IdempotentExecuteHandlerFactory {
    /**
     * 获取幂等执行处理器
     *
     * @param scene
     * @param type
     * @return 指定场景和访问方式的幂等执行处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type) {
        IdempotentExecuteHandler handler = null;
        switch(scene){
            case RESTAPI -> {
                switch (type){
                    case TOKEN -> handler = ApplicationContextHolder.getBean(IdempotentTokenExecuteHandler.class);
                    case PARAM -> handler = ApplicationContextHolder.getBean(IdempotentParamExecuteHandler.class);
                    case SPEL ->  handler = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
                }
            }
            case MQ -> handler = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
            default -> {}
        }
        return handler;
    }
}
