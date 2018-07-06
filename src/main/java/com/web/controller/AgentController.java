package com.web.controller;

import com.web.pojo.Agent;
import com.web.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("agent")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @RequestMapping("/getListAgent.Action")
    @ResponseBody
    public List<Agent> getListAgent(){
        return agentService.getListAgent();
    }
}
