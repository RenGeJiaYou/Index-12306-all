package com.sjj.cachespringbootstarter.toolkit;

import com.alibaba.fastjson2.util.ParameterizedTypeImpl;

import java.lang.reflect.Type;

/**
 * FastJson2 工具类
 *
 * @author Island_World
 */

public final class FastJson2Util {
    /**
     * 构建嵌套 Type
     * <p>
     * 举例:<p>
     * types = [ArrayList.class,List.class,String.class]<p>
     * 返回值 = ArrayList&lt;List&lt;String&gt;&gt;
     *
     * @param types
     * @return 一个嵌套的 ParameterizedTypeImpl 对象，其中嵌套级别和类型参数对应于 types 数组中的元素。这可以用来表示 Java 中的复杂泛型类型。
     *
     */
    public static Type buildType(Type... types) {
        ParameterizedTypeImpl beforeType = null;
        if (types != null && types.length > 0) {
            if (types.length == 1) {
                return new ParameterizedTypeImpl(
                        new Type[]{null},
                        null,
                        types[0]);
            }
            for (int i = types.length - 1; i > 0; i--) {
                beforeType = new ParameterizedTypeImpl(
                        new Type[]{beforeType == null ? types[i] : beforeType},
                        null,
                        types[i - 1]);
            }
        }
        return beforeType;
    }
}
