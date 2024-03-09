/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sjj.databasespringbootstarter.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 数据持久层基础属性
 * <p>
 * 因为每个表都有公共的一些属性，比如创建时间、修改时间以及是否逻辑删除标识。
 * 避免每个实体上都重复定义，我们在持久层对象中抽象一个基础的 BaseDO 杜绝上述重复行为。
 *
 * @author Island_World
 */

@Data
public class BaseDO {
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // 修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 删除标志
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

}
