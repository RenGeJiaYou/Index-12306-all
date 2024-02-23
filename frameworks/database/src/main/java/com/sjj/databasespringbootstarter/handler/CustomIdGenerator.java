package com.sjj.databasespringbootstarter.handler;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @Author Island_World
 */

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public boolean assignId(Object idValue) {
        return IdentifierGenerator.super.assignId(idValue);
    }

    @Override
    public Number nextId(Object entity) {
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        return IdentifierGenerator.super.nextUUID(entity);
    }
}
