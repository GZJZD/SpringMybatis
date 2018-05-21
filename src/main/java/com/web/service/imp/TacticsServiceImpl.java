package com.web.service.imp;

import com.web.dao.TacticsDao;
import com.web.pojo.Tactics;
import com.web.service.ITacticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by may on 2018/5/20.
 */
@Service@Transactional
public class TacticsServiceImpl implements ITacticsService{
    @Autowired
    private TacticsDao tacticsDao;
    @Override
    public void save(Tactics tactics) {
        tacticsDao.insert(tactics);
    }

    @Override
    public void updateTactics(Tactics tactics) {
        Tactics tactics1 = tacticsDao.selectByPrimaryKey(tactics.getId());
        tactics.setVersion(tactics1.getVersion());
        int count = tacticsDao.updateByPrimaryKey(tactics);
       if(count<=0){
            throw new RuntimeException("时间戳出现异常:"+TacticsServiceImpl.class);
        }

    }

    @Override
    public Tactics getTacticsByTacticsId(Long id) {
        return tacticsDao.selectByPrimaryKey(id);
    }
}
