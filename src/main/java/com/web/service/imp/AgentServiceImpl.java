package com.web.service.imp;

import com.web.dao.AgentDao;
import com.web.pojo.Agent;
import com.web.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {
    @Autowired
    private AgentDao agentDao;
    @Override
    public Agent getAgentById(Long agentId) {
        return agentDao.getAgentById(agentId);
    }

    @Override
    public List<Agent> getListAgent() {
        return agentDao.getListAgent();
    }
}
