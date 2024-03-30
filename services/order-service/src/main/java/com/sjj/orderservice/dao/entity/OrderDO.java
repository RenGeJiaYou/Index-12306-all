package com.sjj.orderservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sjj.databasespringbootstarter.base.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单数据库实体
 *
 * @author Island_World
 */

@Data
@TableName("t_order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 用户名
     */
    private String username;

    /**
     * 车次 ID
     */
    private Long trainId;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 订单来源
     */
    private Integer source;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 支付时间
     */
    private Date payTime;

}
