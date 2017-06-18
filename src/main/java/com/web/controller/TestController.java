package com.web.controller;

import com.web.pojo.Test;
import com.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 测试
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    @Qualifier("testService")
    private TestService testService;

    @RequestMapping(value = "/get.Action", method = RequestMethod.GET)
    @ResponseBody
    public Test getTestById(@RequestParam("id") String id) {
        System.out.println("hello come in");
        return testService.getById(Integer.parseInt(id)

        );
    }
}
