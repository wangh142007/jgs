package com.wh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author wh
 * @create 2020/3/27 0027
 * @since 1.0.0
 */
@Controller
public class SSOController {

    private final static Logger log = LoggerFactory.getLogger(SSOController.class);

    @GetMapping("/hello")
    @ResponseBody
    public Object hello(){

        return "hello";
    }

}
