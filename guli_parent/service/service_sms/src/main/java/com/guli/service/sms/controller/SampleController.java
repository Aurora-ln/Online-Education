package com.guli.service.sms.controller;

import com.guli.common.base.result.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms/sample")
public class SampleController {
    @Value("${aliyun.sms.signName}")
    private String signName;

    @GetMapping("test1")
    public R test1(){
        return R.ok().data("signName", signName);
    }
}