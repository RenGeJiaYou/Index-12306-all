package com.sjj.orderservice.service.impl;

import com.sjj.distributedidspringbootstarter.toolkit.SnowflakeIdUtil;
import com.sjj.orderservice.dao.entity.OrderDO;
import com.sjj.orderservice.dao.entity.OrderItemDO;
import com.sjj.orderservice.dao.mapper.OrderItemMapper;
import com.sjj.orderservice.dao.mapper.OrderMapper;
import com.sjj.orderservice.dto.TicketOrderCreateReqDTO;
import com.sjj.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单接口层实现类
 *
 * @author Island_World
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public String createTicketOrder(TicketOrderCreateReqDTO req) {
        String orderSN = SnowflakeIdUtil.nextIdStr();
        OrderDO orderDO = OrderDO.builder()
                .orderSn(orderSN)
                .username(req.getUsername())
                .trainId(req.getTrainId())
                .departure(req.getDeparture())
                .arrival(req.getArrival())
                .status(0)
                .orderTime(req.getOrderTime())
                .build();
        orderMapper.insert(orderDO);// user_id,riding_date,train_number,source 尚为空

        var ticketOrderItems = req.getTicketOrderItems();
        List<OrderItemDO> orderItemDOList = new ArrayList<>();
        ticketOrderItems.forEach(each -> {
            var orderItemDO = OrderItemDO.builder()
                    .orderSn(orderSN)
                    .trainId(req.getTrainId())
                    .carriageNumber(each.getCarriageNumber())
                    .seatNumber(each.getSeatNumber())
                    .realName(each.getRealName())
                    .idType(each.getIdType())
                    .idCard(each.getIdCard())
                    .phone(each.getPhone())
                    .status(0)
                    .amount(each.getAmount())
                    .build();
            orderItemDOList.add(orderItemDO);
        });
        // todo 批量插入
        orderItemDOList.forEach(orderItemMapper::insert);
        return orderSN;
    }
}
