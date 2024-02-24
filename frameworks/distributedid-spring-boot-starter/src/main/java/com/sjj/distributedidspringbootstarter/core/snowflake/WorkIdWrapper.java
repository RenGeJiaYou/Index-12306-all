package com.sjj.distributedidspringbootstarter.core.snowflake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WorkId 包装类
 *
 * @Author Island_World
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkIdWrapper {
    // 工作 ID
    private Long workId;

    // 数据中心 ID
    private Long dataCenterId;
}
