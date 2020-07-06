package com.guli.service.trade.service;

import java.util.Map;

public interface WeixinPayService {
    //商品号和设备号
    Map<String, Object> createNative(String orderNo, String remoteAddr);

}
