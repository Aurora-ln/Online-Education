package com.guli.service.base.dto;

import lombok.Data;
import java.io.Serializable;

@Data
//会员
public class MemberDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;//会员id
    private String mobile;//手机号
    private String nickname;//昵称
}
