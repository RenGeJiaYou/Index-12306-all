package com.sjj.databasespringbootstarter.toolkit;

/*
 * 分页工具类，配合规约组件中封装的分页对象
 *
 * @Author Island_World
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjj.commonspringbootstarter.toolkit.BeanUtil;
import com.sjj.conventionspringbootstarter.page.PageRequest;
import com.sjj.conventionspringbootstarter.page.PageResponse;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtil {
    /**
     * arguments to {@link Page}
     *
     * @param current
     * @param size
     * @return Page
     */
    public static Page convert(long current,long size){return new Page(current,size);}

    /**
     * {@link PageRequest} to {@link Page}
     *
     * @param pageRequest
     * @return Page
     */
    public static Page convert(PageRequest pageRequest) {
        return convert(pageRequest.getCurrent(), pageRequest.getSize());
    }

    /**
     * {@link IPage} build to {@link PageResponse}
     *
     * @param ipage
     * @return PageResponse
     */
    private static PageResponse buildConventionPage(IPage ipage) {
        return PageResponse
                .builder()
                .current(ipage.getCurrent())
                .size(ipage.getSize())
                .records(ipage.getRecords())
                .total(ipage.getTotal())
                .build();
    }

    /**
     * {@link IPage} to {@link PageResponse}
     * @param ipage
     * @return PageResponse
    * */
    public static PageResponse convert(IPage ipage){return buildConventionPage(ipage);}

    /**
     * {@link IPage} to {@link PageResponse}
     *
     * @param iPage
     * @param targetClass
     * @param <TARGET>
     * @param <ORIGINAL>
     * @return PageResponse
     */
    public static <TARGET, ORIGINAL> PageResponse<TARGET> convert(IPage<ORIGINAL>iPage,Class<TARGET> targetClass){
        // 对本 iPage 实例持有的分页数据进行转换，要求入参是 TARGET.class。转换完后原地返回实例
        iPage.convert(each -> BeanUtil.convert(each,targetClass));
        return buildConventionPage(iPage);
    }

    /**
     * {@link IPage} to {@link PageResponse}
     *
     * @param iPage
     * @param mapper
     * @return PageResponse
     */
    public static <TARGET, ORIGINAL> PageResponse<TARGET> convert(IPage<ORIGINAL> iPage, Function<? super ORIGINAL, ? extends TARGET> mapper) {
        // 对本 iPage 实例持有的分页数据进行转换，要求入参是自定义的 mapper 函数。转换完后原地返回实例
        List<TARGET> targetList = iPage.getRecords().stream()
                .map(mapper)
                .collect(Collectors.toList());
        return PageResponse.<TARGET>builder()
                .current(iPage.getCurrent())
                .total(iPage.getTotal())
                .records(targetList)
                .total(iPage.getTotal())
                .build();
    }
}
