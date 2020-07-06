package com.guli.service.sms.service;


import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Service;


public interface SmsService {

    void send(String mobile, String checkCode) throws ClientException;

}
