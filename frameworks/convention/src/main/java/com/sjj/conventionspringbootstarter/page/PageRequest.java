package com.sjj.conventionspringbootstarter.page;

/*
 * 分页的请求对象
 *
 * @author Island_World
 */

import lombok.Data;

@Data
public class PageRequest {
    // 当前页数
    private Long current;

    // 每页条数
    private Long size;
}
