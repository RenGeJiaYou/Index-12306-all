package com.sjj.basespringbootstarter.init;

/*
 * 规约事件，通过此事件可查看业务系统的所有初始化行为
 *
 * @Author Island_World
 */

import org.springframework.context.ApplicationEvent;

public class ApplicationInitializingEvent extends ApplicationEvent {
    public ApplicationInitializingEvent(Object source) {
        super(source);
    }
}
