package com.sjj.distributedidspringbootstarter.core.snowflake;

import cn.hutool.core.collection.CollUtil;
import com.sjj.basespringbootstarter.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis 生成雪花 ID
 *
 * @author Island_World
 */

@Slf4j
public class LocalRedisWorkIdChoose extends AbstractWorkIdChooseTemplate implements InitializingBean {
    private RedisTemplate stringRedisTemplate;

    public LocalRedisWorkIdChoose(){
        this.stringRedisTemplate = ApplicationContextHolder.getBean(StringRedisTemplate.class);
    }

    @Override
    protected WorkIdWrapper chooseWorkId() {
        DefaultRedisScript script = new DefaultRedisScript();
        script.setScriptSource(
                new ResourceScriptSource(
                        // 将锁定本模块的 resource/ 文件夹
                        new ClassPathResource("lua/chooseWorkIdLua.lua")
                )
        );
        List<Long> luaResultList = null;
        try{
            script.setResultType(List.class);
            luaResultList = (ArrayList)this.stringRedisTemplate.execute(script,null);
        }catch(Exception ex){
            log.error("Redis Lua 脚本获取 WorkId 失败", ex);
        }
        return CollUtil.isNotEmpty(luaResultList)?
                new WorkIdWrapper(luaResultList.get(0), luaResultList.get(1)):
                new RandomWorkIdChoose().chooseWorkId();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chooseWorkId();
    }
}
