package com.sjj.databasespringbootstarter.toolkit;

/*
 * 分页工具类，配合规约组件中封装的分页对象
 *
 * @Author Island_World
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjj.conventionspringbootstarter.page.PageRequest;

public class PageUtil {
    /**
     * {@link PageRequest} to {@link Page}
     *
     * @param pageRequest
     * @return
     */
    public static Page convert(PageRequest pageRequest) {
        return convert
    }


    public static Page convert(long current,long size){
        return new Page(current,size);
    }
}
