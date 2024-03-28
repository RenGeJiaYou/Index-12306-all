package com.sjj.ticketservice.service.handler.ticket.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.basespringbootstarter.ApplicationContextHolder;
import com.sjj.cachespringbootstarter.DistributedCache;
import com.sjj.conventionspringbootstarter.exception.ServiceException;
import com.sjj.designpatternspringbootstarter.strategy.AbstractExecuteStrategy;
import com.sjj.ticketservice.common.enums.SeatsStatusEnum;
import com.sjj.ticketservice.dao.entity.SeatDO;
import com.sjj.ticketservice.dao.entity.TrainStationDO;
import com.sjj.ticketservice.dao.mapper.SeatMapper;
import com.sjj.ticketservice.dao.mapper.TrainStationMapper;
import com.sjj.ticketservice.dto.req.PurchaseTicketReqDTO;
import com.sjj.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;
import com.sjj.ticketservice.toolkit.StationCalculateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;

/**
 * 抽象购票模板基础服务.使用了模板模式 selectSeats() 由各子类具体实现；executeResp() 调用了 selectSeats()
 *
 * @author Island_World
 */

@RequiredArgsConstructor
public abstract class AbstractTrainPurchaseTicketTemplate implements ApplicationRunner, IPurchaseTicket, AbstractExecuteStrategy<PurchaseTicketReqDTO, List<TrainPurchaseTicketRespDTO>> {
    private SeatMapper seatMapper;
    private TrainStationMapper trainStationMapper;
    private DistributedCache distributedCache;

    /**
     * 选择[商务座,一等座,二等座]，由各子类具体实现
     *
     * @param req 购票请求入参
     * @return 乘车人座位
     */
    protected abstract List<TrainPurchaseTicketRespDTO> selectSeats(PurchaseTicketReqDTO req);


    @Override
    public List<TrainPurchaseTicketRespDTO> executeResp(PurchaseTicketReqDTO req) {
        // TODO 后续逻辑全部转换为 LUA 缓存原子操作
        List<TrainPurchaseTicketRespDTO> actualSeats = selectSeats(req);
        if (CollUtil.isEmpty(actualSeats)) {
            throw new ServiceException("站点余票不足，请尝试更换座位类型或选择其它站点");
        }
        // 获取扣减开始站点和目的站点及中间站点信息
        var wrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, req.getTrainId());
        // 如车次1 路线为 (起点站)A-B-C-D-(终点站)E，将返回 [(A-B),(B-C),(C-D),(D-E),(E-null)] 五条数据
        var trainStationDOList = trainStationMapper.selectList(wrapper);
        // 返回 [A,B,C,D,E] 五条数据
        var trainStationAllList = trainStationDOList.stream().map(TrainStationDO::getDeparture).toList();
        // 如出发站点为 B, 到达站点为 D, 返回[(B-C),(C-D)]
        var trainStationThroughList = StationCalculateUtil.throughStation(trainStationAllList, req.getDeparture(), req.getArrival());
        // 锁定座位车票库存
        actualSeats.forEach(each -> trainStationThroughList.forEach(item -> {
            var updateWrapper = Wrappers.lambdaUpdate(SeatDO.class)
                    .eq(SeatDO::getTrainId,req.getTrainId())                // TrainID 来自请求参数。因为购票时，已经选定具体车次
                    .eq(SeatDO::getCarriageNumber,each.getCarriageNumber()) // CarriageNumber 来自列车购票响应参数。响应参数包含了找到的空座的所在车厢
                    .eq(SeatDO::getStartStation,item.getStartStation())     // 出发站点。依次将[出发站点-到达站点间]每相邻两站的票扣除一张
                    .eq(SeatDO::getEndStation,item.getEndStation())         // 到达站点。依次将[出发站点-到达站点间]每相邻两站的票扣除一张
                    .eq(SeatDO::getSeatNumber,each.getSeatNumber())         // 座位号来自列车购票响应参数。响应参数包含了空座的座位号
                    .set(SeatDO::getSeatStatus, SeatsStatusEnum.LOCKED.getCode()); // 将 t_seat 表中对应记录设置为锁定
            seatMapper.update(null,updateWrapper);
        }));
        return actualSeats;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seatMapper = ApplicationContextHolder.getBean(SeatMapper.class);
        trainStationMapper = ApplicationContextHolder.getBean(TrainStationMapper.class);
        distributedCache = ApplicationContextHolder.getBean(DistributedCache.class);
    }
}
