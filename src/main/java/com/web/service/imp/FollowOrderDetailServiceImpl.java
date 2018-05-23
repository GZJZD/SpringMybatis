package com.web.service.imp;

import com.web.dao.FollowOrderDetailDao;
import com.web.pojo.FollowOrderDetail;
import com.web.service.IFollowOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by may on 2018/5/23.
 */
@Service
@Transactional
public class FollowOrderDetailServiceImpl implements IFollowOrderDetailService {
    @Autowired
    private FollowOrderDetailDao followOrderDetailDao;

    @Override
    public void save(FollowOrderDetail followOrderDetail) {
        followOrderDetailDao.insert(followOrderDetail);
    }

    @Override
    public FollowOrderDetail getFollowOrderDetail(Long id) {
        return followOrderDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public List<FollowOrderDetail> getDetailListByFollowOrderId(Long followOrderId) {
        return followOrderDetailDao.getDetailListByFollowOrderId(followOrderId);
    }

    @Override
    public List<FollowOrderDetail> getDetailListByOrderIdAndDirection(Long followOrderId, Integer direction) {
        return followOrderDetailDao.getDetailListByOrderIdAndDirection(followOrderId,direction);
    }

    @Override
    public void updateDetail(FollowOrderDetail followOrderDetail) {
        int count = followOrderDetailDao.updateByPrimaryKey(followOrderDetail);
        if(count<=0){
            throw new RuntimeException("乐观锁异常：" +FollowOrderDetail.class);
        }
    }
}
