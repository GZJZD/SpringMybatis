package com.web.service.imp;

import com.web.dao.FollowOrderClientDao;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.FollowOrderDetail;
import com.web.pojo.vo.FollowOrderClientParamVo;
import com.web.pojo.vo.OrderUserDetailsVo;
import com.web.service.IFollowOrderClientService;
import com.web.service.IFollowOrderService;
import com.web.service.OrderUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by may on 2018/5/19.
 */
@Service@Transactional
public class FollowOrderClientServiceImpl implements IFollowOrderClientService {
    @Autowired
    private FollowOrderClientDao followOrderClientDao;
    @Autowired
    private OrderUserService orderUserService;
    @Autowired
    private IFollowOrderService followOrderService;

    @Override
    public List<FollowOrderClient> getListByUserCode(String userCode) {
        return followOrderClientDao.selectByUserCode(userCode);
    }

    @Override
    public List<FollowOrderClient> getListByFollowOrderId(Long followOrderId) {
        return followOrderClientDao.getListByFollowOrderId(followOrderId);
    }

    @Override
    public void deleteByFollowOrderId(Long followOrderId) {
        followOrderClientDao.deleteByFollowOrderId(followOrderId);
    }

    @Override
    public List<String> getListUserCodeByFollowOrderId(Long followOrderId) {
        return followOrderClientDao.getListUserCodeByFollowOrderId(followOrderId);
    }

    @Override
    public void saveListFollowOrderClient(List<FollowOrderClient> followOrderClients, FollowOrder followOrder) {
        if(followOrderClients.size() != 0){
            for (FollowOrderClient orderClient : followOrderClients) {
                FollowOrderClient followOrderClient = new FollowOrderClient();
                followOrderClient.setFollowOrderId(followOrder.getId());
                followOrderClient.setUserCode(orderClient.getUserCode());
                followOrderClient.setFollowDirection(orderClient.getFollowDirection());
                followOrderClient.setHandNumberType(orderClient.getHandNumberType());
                followOrderClient.setFollowHandNumber(orderClient.getFollowHandNumber());
                followOrderClientDao.insert(followOrderClient);
            }
        }
    }

    @Override
    public void update(FollowOrderClient record) {
        followOrderClientDao.updateByPrimaryKey(record);
    }

    /*
    * 通过跟单id，返回跟单编辑中的客户列表
    * param 跟单id
    * */
    @Override
    public List<FollowOrderClientParamVo> getListFollowOrderClientParamVo(Long followOrderId) {
        List<FollowOrderClient> followOrderClients = getListByFollowOrderId(followOrderId);
        FollowOrder followOrder = followOrderService.getFollowOrder(followOrderId);
        List<FollowOrderClientParamVo> followOrderClientParamVoList = new ArrayList<>();

        for (FollowOrderClient followOrderClient : followOrderClients) {
            FollowOrderClientParamVo followOrderClientParamVo = new FollowOrderClientParamVo();
            OrderUserDetailsVo userDetails = orderUserService.getUserDetails(followOrderClient.getUserCode(),
                    followOrder.getVariety().getVarietyCode());
            followOrderClientParamVo.setUserCode(followOrderClient.getUserCode());//用户编号
            followOrderClientParamVo.setUserName("向日葵");//用户姓名
            followOrderClientParamVo.setOffset_gain_and_loss(userDetails.getOffset_gain_and_loss());//平仓盈亏
            followOrderClientParamVo.setProfit_loss_than(userDetails.getPosition_gain_and_loss());//持仓盈亏
            followOrderClientParamVo.setFollowDirection(followOrderClient.getFollowDirection());//跟单方向
            followOrderClientParamVo.setHandNumber(followOrderClient.getFollowHandNumber());//跟单手数
            followOrderClientParamVo.setHandNumberType(followOrderClient.getHandNumberType());//跟单类型
            followOrderClientParamVoList.add(followOrderClientParamVo);
        }
        return followOrderClientParamVoList;
    }
}
