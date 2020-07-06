package com.guli.service.sms.controller;

import com.aliyuncs.exceptions.ClientException;
import com.guli.common.base.result.R;
import com.guli.common.base.result.ResultCodeEnum;
import com.guli.common.base.util.FormUtils;
import com.guli.common.base.util.RandomUtils;
import com.guli.service.base.exception.GuliException;
import com.guli.service.sms.service.SmsService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(description = "短信管理")
//@CrossOrigin //跨域
@Slf4j
//短信发送
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {

        //校验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            log.error("请输入正确的手机号码 ");
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();

        //发送验证码
        smsService.send(mobile, checkCode);

        //将验证码存入redis缓存
        redisTemplate.opsForValue().set(mobile, checkCode, 5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");

    }



}
