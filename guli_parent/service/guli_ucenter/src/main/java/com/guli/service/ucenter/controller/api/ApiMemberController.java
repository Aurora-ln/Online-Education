package com.guli.service.ucenter.controller.api;


import com.guli.common.base.result.R;
import com.guli.common.base.result.ResultCodeEnum;
import com.guli.common.base.util.JwtInfo;
import com.guli.common.base.util.JwtUtils;
import com.guli.service.base.dto.MemberDto;
import com.guli.service.base.exception.GuliException;
import com.guli.service.ucenter.entity.vo.LoginVo;
import com.guli.service.ucenter.entity.vo.RegisterVo;
import com.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Aurora
 * @since 2020-06-23
 */
@Api(description = "会员管理")
@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private MemberService memberService;


    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){

        memberService.register(registerVo);

        return R.ok();
    }


    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {

        String token = memberService.login(loginVo);

        return R.ok().data("token", token);

    }


    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("get-login-info")
    public R getLoginInfo(HttpServletRequest request){

        try{
            //util中通过Token获取用户信息
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.ok().data("userInfo", jwtInfo);
        }catch (Exception e){
            log.error("解析用户信息失败，" + e.getMessage());
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    @ApiOperation("根据会员id查询订单中需要的会员信息")
    @GetMapping("inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(
            @ApiParam(value = "会员ID", required = true)
            @PathVariable String memberId){

        MemberDto memberDto = memberService.getMemberDtoByMemberId(memberId);
        return memberDto;

    }




}

