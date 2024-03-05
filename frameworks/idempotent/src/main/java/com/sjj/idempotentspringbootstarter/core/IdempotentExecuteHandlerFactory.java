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
