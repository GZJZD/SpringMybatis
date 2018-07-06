package com.web.service;

import com.web.dao.AgentDao;
import com.web.pojo.Agent;

import java.util.List;

public interface AgentService {
    Agent getAgentById(Long agentId);
    List<Agent> getListAgent();
}
