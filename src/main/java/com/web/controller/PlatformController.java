package com.web.controller;

import com.web.pojo.Platform;
import com.web.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("platform")
public class PlatformController {
    @Autowired
    private PlatformService platformService;

    @RequestMapping("/getListPlatform.Action")
    @ResponseBody
    public List<Platform> getListPlatform(){
        return platformService.getListPlatform();
    }
}
