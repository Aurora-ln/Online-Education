package com.guli.service.cms.controller.api;

import com.guli.common.base.result.R;
import com.guli.service.cms.entity.Ad;
import com.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin //解决跨域问题
@Api(description = "广告推荐")
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    private AdService adService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位id", required = true) @PathVariable String adTypeId) {

        List<Ad> ads = adService.selectByAdTypeId(adTypeId);

        return R.ok().data("items", ads);
    }

    @PostMapping("save-test")
    public R saveAd(@RequestBody Ad ad){
        //redisTemplate.opsForValue().set("ad1", ad);
        redisTemplate.opsForValue().set("index::ad", ad);
        return R.ok();
    }

}