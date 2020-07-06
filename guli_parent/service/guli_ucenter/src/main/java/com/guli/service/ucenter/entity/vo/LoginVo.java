package com.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
   //登陆页面输入信息
    private static final long serialVersionUID = 1L;
    private String mobile;
    private String password;

}