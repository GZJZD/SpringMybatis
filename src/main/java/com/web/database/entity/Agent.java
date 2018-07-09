package com.web.database.entity;

public class Agent {
    /**
     * 代理人id
     */
    private String agent;
    /**
     * 代理人姓名  对应鼎字段 未使用驼峰
     */
    private String agentname;


    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }
}
