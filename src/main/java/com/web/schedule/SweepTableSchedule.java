package com.web.schedule;

import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderDetail;
import com.web.service.FollowOrderDetailService;
import com.web.service.FollowOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@PropertySource("classpath:scheduleConfig.properties")
public class SweepTableSchedule {

    private static Map<String,List<FollowOrderDetail>> detailPositionGainAndLoss = new HashMap<>();
    @Autowired
    private FollowOrderDetailService followOrderDetailService;
    @Autowired
    private FollowOrderService followOrderService;

    public List<FollowOrderDetail> getDetailPositionGainAndLoss(String followOrderId){
        return detailPositionGainAndLoss.get(followOrderId);
    }

    @Scheduled(cron = "${schedule}")
    public void doSweepTable(){
        List<FollowOrder> followOrder = followOrderService.getNOStopFollowOrder();
        for (FollowOrder order : followOrder) {
            List<FollowOrderDetail> detailList = followOrderDetailService.getNOCloseDetailListByFollowOrderId(order.getId());
            if(detailList.size()!=0){

            }
        }

    }

}
