package com.sjj.distributedidspringbootstarter.core.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * 随机数后去雪花 WorkId
 *
 * @Author Island_World
 */

@Slf4j
public class RandomWorkIdChoose extends AbstractWorkIdChooseTemplate implements InitializingBean {
    private static long getRandom(int start, int end) {
        return (long) (Math.random() * (end - start + 1) + start);
    }

    @Override
    protected WorkIdWrapper chooseWorkId() {
        int start = 0, end = 31;
        return new WorkIdWrapper(getRandom(start, end), getRandom(start, end));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chooseWorkId();
    }


}
