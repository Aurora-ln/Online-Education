package com.guli.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.edu.entity.vo.TeacherQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
public interface TeacherService extends IService<Teacher> {

    IPage<Teacher> selectPage(Long page, Long limit, TeacherQueryVo teacherQueryVo);

    List<Map<String, Object>> selectNameListByKey(String key);

    boolean removeAvatarById(String id);

    /**
     * 根据讲师id获取讲师详情页数据
     * @param id
     * @return
     */
    Map<String, Object> selectTeacherInfoById(String id);

    //获取网站首页最热门教师数据
    List<Teacher> selectHotTeacher();
}
