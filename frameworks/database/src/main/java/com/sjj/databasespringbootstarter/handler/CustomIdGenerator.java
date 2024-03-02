package com.sjj.databasespringbootstarter.handler;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.sjj.distributedidspringbootstarter.toolkit.SnowflakeIdUtil;

/**
 * @author Island_World
 */

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        return SnowflakeIdUtil.nextId();
    }


}
