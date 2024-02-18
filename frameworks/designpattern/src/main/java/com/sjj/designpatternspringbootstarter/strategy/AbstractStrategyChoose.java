package com.sjj.designpatternspringbootstarter.strategy;

/*
 * 策略选择器
 *
 * @Author Island_World
 */

import com.sjj.basespringbootstarter.ApplicationContextHolder;
import com.sjj.basespringbootstarter.init.ApplicationInitializingEvent;
import com.sjj.conventionspringbootstarter.exception.ServiceException;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class AbstractStrategyChoose implements ApplicationListener<ApplicationInitializingEvent> {

    /**
     * 策略实现类的容器
     */
    private final Map<String, AbstractExecuteStrategy> abstractExecuteStrategyMap = new HashMap<>();

    /**
     * 根据 mark 查询具体策略
     *
     * @param mark          策略标识
     * @param predicateFlag 匹配解析标识
     * @return 实际执行策略
     */
    public AbstractExecuteStrategy choose(String mark, Boolean predicateFlag) {
        if (predicateFlag != null && predicateFlag) {
            return abstractExecuteStrategyMap
                    .values()
                    .stream()
                    .filter(each -> StringUtils.hasText(each.patternMatchMark())) // patternMatchMark() 的作用？
                    .filter(each -> Pattern.compile(each.patternMatchMark()).matcher(mark).matches()) // 检测 mark 中是否有匹配 each.patternMatchMark() 的子串
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("策略未定义"));
        }
        return Optional.ofNullable(abstractExecuteStrategyMap.get(mark))
                .orElseThrow(() -> new ServiceException(String.format("[%s] 策略未定义", mark)));
    }

    /**
     * 根据 mark 查询具体策略并执行
     *
     * @param mark         策略标识
     * @param requestParam 执行策略入参
     * @param <REQUEST>    执行策略入参的泛型
     */
    public <REQUEST> void chooseAndExecute(String mark, REQUEST requestParam) {
        AbstractExecuteStrategy strategy = choose(mark, null);
        strategy.execute(requestParam);
    }

    /**
     * 根据 mark 查询具体策略并执行
     *
     * @param mark              策略标识
     * @param requestParam      执行策略入参
     * @param predicateFlag     匹配范解析标识
     * @param <REQUEST>         执行策略入参的泛型
     */
    public <REQUEST> void chooseAndExecute(String mark, REQUEST requestParam,boolean predicateFlag) {
        AbstractExecuteStrategy strategy = choose(mark, predicateFlag);
        strategy.execute(requestParam);
    }

    /**
     * 根据 mark 查询具体策略并执行，带返回结果
     *
     * @param mark         策略标识
     * @param requestParam 执行策略入参
     * @param <REQUEST>    执行策略入参范型
     * @param <RESPONSE>   执行策略出参范型
     * @return
     */
    public <REQUEST,RESPONSE> RESPONSE chooseAndExecuteResp(String mark, REQUEST requestParam) {
        AbstractExecuteStrategy strategy = choose(mark, null);
        return (RESPONSE)strategy.executeResp(requestParam);
    }

    /**
     * 监听 ApplicationInitializingEvent 事件，
     * 当这个事件发生时，onApplicationEvent方法会被调用。
     * 本方法会获取所有的 AbstractExecuteStrategy 实例，并将它们添加到abstractExecuteStrategyMap中。
     */
    @Override
    public void onApplicationEvent(ApplicationInitializingEvent event) {
        Map<String, AbstractExecuteStrategy> allActualStrategies = ApplicationContextHolder.getBeansOfType(AbstractExecuteStrategy.class);
        allActualStrategies
                .forEach((beanName, bean) -> {
                    AbstractExecuteStrategy beanExist = abstractExecuteStrategyMap.get(bean.mark());
                    if (beanExist != null) {
                        throw new ServiceException(String.format("服务异常:策略 [%s] 重复定义", bean.mark()));
                    }
                    abstractExecuteStrategyMap.put(bean.mark(), bean);
                });
    }
}
