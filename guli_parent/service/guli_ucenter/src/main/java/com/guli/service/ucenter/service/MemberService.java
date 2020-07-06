package com.guli.service.ucenter.service;

import com.guli.service.base.dto.MemberDto;
import com.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.ucenter.entity.vo.LoginVo;
import com.guli.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Aurora
 * @since 2020-06-23
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    /**
     * 根据openid返回用户信息
     * @param openid
     * @return
     */
    Member getByOpenid(String openid);


    MemberDto getMemberDtoByMemberId(String memberId);
//统计注册人数
    Integer countRegisterNum(String day);
}
