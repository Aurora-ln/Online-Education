package com.guli.service.ucenter.mapper;

import com.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author Aurora
 * @since 2020-06-23
 */
public interface MemberMapper extends BaseMapper<Member> {
    //统计所有用户
    Integer selectRegisterNumByDay(String day);
}
