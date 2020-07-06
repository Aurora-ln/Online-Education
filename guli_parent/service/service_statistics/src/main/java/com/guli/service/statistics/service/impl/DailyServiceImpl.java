package com.guli.service.statistics.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.base.result.R;
import com.guli.service.statistics.entity.Daily;
import com.guli.service.statistics.feign.UcenterMemberService;
import com.guli.service.statistics.mapper.DailyMapper;
import com.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Aurora
 * @since 2020-07-01
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

        @Autowired
        private UcenterMemberService ucenterMemberService;

        @Transactional(rollbackFor = Exception.class)
        @Override
        public void createStatisticsByDay(String day) {

            //如果当日的统计记录已存在，则删除重新统计|或 提示用户当日记录已存在
            QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("date_calculated", day);
            baseMapper.delete(queryWrapper);

            //生成统计记录
            R r = ucenterMemberService.countRegisterNum(day);
            Integer registerNum = (Integer)r.getData().get("registerNum");
            //利用随机数模拟数据
            int loginNum = RandomUtils.nextInt(100, 200);
            int videoViewNum = RandomUtils.nextInt(100, 200);
            int courseNum = RandomUtils.nextInt(100, 200);

            //在本地数据库创建统计信息
            Daily daily = new Daily();
            daily.setRegisterNum(registerNum);
            daily.setLoginNum(loginNum);
            daily.setVideoViewNum(videoViewNum);
            daily.setCourseNum(courseNum);
            daily.setDateCalculated(day);

            baseMapper.insert(daily);

        }

    /**
     * 获取所有类比的数据
     */
    @Override
    public Map<String, Map<String, Object>> getChartData(String begin, String end) {

        Map<String, Map<String, Object>> map = new HashMap<>();

        Map<String, Object> registerNum = this.getChartDataByType(begin, end, "register_num");
        Map<String, Object> loginNum = this.getChartDataByType(begin, end, "login_num");
        Map<String, Object> videoViewNum = this.getChartDataByType(begin, end, "video_view_num");
        Map<String, Object> courseNum = this.getChartDataByType(begin, end, "course_num");
        map.put("registerNum", registerNum);
        map.put("loginNum", loginNum);
        map.put("videoViewNum", videoViewNum);
        map.put("courseNum", courseNum);
        //        / <"xlist" xlist>
        // type ——
        //        \ <"ylist" ylist>
        // (map)         (map)
        return map;
    }
    /**
     * 辅助方法：根据类别获取数据
     */
    private Map<String, Object> getChartDataByType(String begin, String end, String type) {

        HashMap<String, Object> map = new HashMap<>();
        //坐标轴数据组装成列表
        ArrayList<String> xList = new ArrayList<>();//日期列表
        ArrayList<Integer> yList = new ArrayList<>();//数据列表

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        //查询类别和时间
        queryWrapper.select(type, "date_calculated");
        //过滤时间点
        queryWrapper.between("date_calculated", begin, end);
        List<Map<String, Object>> mapsData = baseMapper.selectMaps(queryWrapper);

        for (Map<String, Object> data : mapsData) {
            String dateCalculated = (String)data.get("date_calculated");
            xList.add(dateCalculated);
            Integer count = (Integer) data.get(type);
            yList.add(count);
        }
        map.put("xData", xList);
        map.put("yData", yList);
        return map;
    }



}

