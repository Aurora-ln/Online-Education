package com.guli.service.edu.service;

import com.guli.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.edu.entity.vo.CourseCollectVo;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
public interface CourseCollectService extends IService<CourseCollect> {

        boolean isCollect(String courseId, String id);

    void saveCourseCollect(String courseId, String id);

    List<CourseCollectVo> selectListByMemberId(String memberId);

    boolean removeCourseCollect(String courseId, String memberId);
}



