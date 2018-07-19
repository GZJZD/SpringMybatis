package com.web.service.imp;

import com.web.dao.FollowOrderClientDao;
import com.web.database.OrderHongKongService;
import com.web.database.entity.PlatFromUsers;
import com.web.pojo.ContractInfo;
import com.web.pojo.FollowOrder;
import com.web.pojo.FollowOrderClient;
import com.web.pojo.vo.followOrder.FollowOrderClientParamVo;
import com.web.pojo.vo.orderuser.OrderUserDetailsVo;
import com.web.service.ContractInfoService;
import com.web.service.FollowOrderClientService;
import com.web.service.FollowOrderService;
import com.web.service.OrderUserService;
import com.web.util.common.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by may on 2018/5/19.
 */
@Service
@Transactional
public class FollowOrderClientServiceImpl implements FollowOrderClientService {
    @Autowired
    private FollowOrderClientDao followOrderClientDao;
    @Autowired
    private OrderUserService orderUserService;
    @Autowired
    private FollowOrderService followOrderService;
    @Autowired
    private ContractInfoService contractInfoService;
    @Autowired
    private OrderHongKongService orderHongKongService;

    @Override
    public List<Long> getListByUserCodeAndPlatformCode(String userCode, String PlatformCode) {
        return followOrderClientDao.findListFollowOrderIDsByUserCode(userCode, PlatformCode);
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
    public FollowOrderClient getByUserCodeAndPlatformCode(String userCode, String platformCode, Long followOrderId) {
        return followOrderClientDao.getByUserCodeAndPlatformCode(userCode, platformCode, followOrderId);
    }

    @Override
    public List<String> getListUserCodeByFollowOrderId(Long followOrderId) {
        return followOrderClientDao.getListUserCodeByFollowOrderId(followOrderId);
    }

    @Override
    public void saveListFollowOrderClient(List<FollowOrderClient> followOrderClients, FollowOrder followOrder) {
        if (followOrderClients.size() != 0) {
            for (FollowOrderClient orderClient : followOrderClients) {
                FollowOrderClient followOrderClient = new FollowOrderClient();
                followOrderClient.setFollowOrderId(followOrder.getId());
                followOrderClient.setUserCode(orderClient.getUserCode());
                followOrderClient.setPlatformCode(orderClient.getPlatformCode());
                followOrderClient.setFollowDirection(orderClient.getFollowDirection());
                followOrderClient.setHandNumberType(orderClient.getHandNumberType());
                followOrderClient.setFollowHandNumber(orderClient.getFollowHandNumber());
                followOrderClient.setCreateDate(DateUtil.getStringDate());
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
        ContractInfo info = contractInfoService.getInfoByVarietyIdAndPlatformId(followOrder.getVariety().getId(), 2L);
        for (FollowOrderClient followOrderClient : followOrderClients) {
            FollowOrderClientParamVo followOrderClientParamVo = new FollowOrderClientParamVo();
            OrderUserDetailsVo orderUserCount = orderUserService.getOrderUserCount(followOrderClient.getUserCode(),
                    info.getContractCode(), followOrderClient.getPlatformCode());
            followOrderClientParamVo.setUserCode(followOrderClient.getUserCode());//用户编号 todo

            followOrderClientParamVo.setUserName("向日葵");//用户姓名
            followOrderClientParamVo.setOffset_gain_and_loss(orderUserCount.getOffset_gain_and_loss());//平仓盈亏
            followOrderClientParamVo.setProfit_loss_than(orderUserCount.getPosition_gain_and_loss());//持仓盈亏
            followOrderClientParamVo.setFollowDirection(followOrderClient.getFollowDirection());//跟单方向
            followOrderClientParamVo.setHandNumber(followOrderClient.getFollowHandNumber());//跟单手数
            followOrderClientParamVo.setHandNumberType(followOrderClient.getHandNumberType());//跟单类型
            followOrderClientParamVoList.add(followOrderClientParamVo);
        }
        return followOrderClientParamVoList;
    }


    @Override
    public FollowOrderClient getFollowOrderClient(Long followOrderClientId) {
        return followOrderClientDao.getFollowOrderClient(followOrderClientId);
    }

    @Override
    public List<Map<String,Object>> getListUserNameByFollowOrderId(Long followOrderId) {
        List<FollowOrderClient> followOrderClients = followOrderClientDao.getListByFollowOrderId(followOrderId);
        List<Map<String,Object>> userName = new ArrayList<>();
        for (FollowOrderClient orderClient : followOrderClients) {
            Map<String,Object> map = new HashMap<>();
            String userName1 = getUserName(orderClient.getUserCode(), orderClient.getPlatformCode());
            map.put("userName",userName1);
            map.put("followOrderClientId",orderClient.getId());

            userName.add(map);
        }
        return userName;
    }

    /*
     * 通过平台和userCode获取对应的客户名字
     * */
    public String getUserName(String userCode, String platformCode) {

        PlatFromUsers users;
        if ("orders75".equals(platformCode)) {
            users = orderHongKongService.getUser75(userCode);
        } else {
            users = orderHongKongService.getUser76(userCode);
        }
        if (users == null) {
            return "-";
        }
        return users.getNAME();

    }
}
