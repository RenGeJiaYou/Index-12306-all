package com.sjj.basespringbootstarter.safa;

/*
 * FastJson 安全模式，开启后将关闭类型隐式传递
 *
 * @Author Island_World
 */

import org.springframework.beans.factory.InitializingBean;

public class FastJsonSafeMode implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        System.setProperty("fastjson2.parser.safeMode", "true");
    }
}
