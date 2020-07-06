package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.base.result.R;
import com.guli.service.edu.entity.Course;
import com.guli.service.edu.entity.Teacher;
import com.guli.service.edu.entity.vo.TeacherQueryVo;
import com.guli.service.edu.feign.OssFileService;
import com.guli.service.edu.mapper.CourseMapper;
import com.guli.service.edu.mapper.TeacherMapper;
import com.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private OssFileService ossFileService;

    @Override
    public IPage<Teacher> selectPage(Long page, Long limit, TeacherQueryVo teacherQueryVo) {
        //显示条件插叙表
        Page<Teacher> pageParam = new Page<>(page, limit);
        //1.排序
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        //2.分页查询
        if (teacherQueryVo == null){
            return baseMapper.selectPage(pageParam, queryWrapper);
        }

        //3.条件查询
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String begin = teacherQueryVo.getJoinDateBegin();
        String end = teacherQueryVo.getJoinDateEnd();
        if (!StringUtils.isEmpty(name)) {
            //左%会使索引失效
            queryWrapper.likeRight("name", name);
        }

        if (level != null) {
            queryWrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("join_date", begin);
        }

        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("join_date", end);
        }

        return baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name", key);
        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);//返回值是Map列表
        return list;
    }


    @Override
    public boolean removeAvatarById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        if(teacher != null) {
            String avatar = teacher.getAvatar();
            if(!StringUtils.isEmpty(avatar)){
                //删除图片
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
        //获取讲师信息
        Teacher teacher = baseMapper.selectById(id);
        //根据讲师id获取讲师课程
        List<Course> courseList =  courseMapper.selectList(new QueryWrapper<Course>().eq("teacher_id", id));
        //用map代替vo
        Map<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("courseList", courseList);
        return map;
    }

    @Override
    @Cacheable(value = "index", key = "'selectHotTeacher'")
    public List<Teacher> selectHotTeacher() {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.last("limit 4");

        return baseMapper.selectList(queryWrapper);

    }
}
