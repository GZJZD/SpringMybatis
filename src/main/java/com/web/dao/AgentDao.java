package com.web.dao;



import com.web.pojo.Agent;

import java.util.List;

public interface AgentDao {
    Agent getAgentById(Long agentId);
    List<Agent> getListAgent();
}
