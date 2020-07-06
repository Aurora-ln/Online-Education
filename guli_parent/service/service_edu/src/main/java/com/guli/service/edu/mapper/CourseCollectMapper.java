package com.guli.service.edu.mapper;

import com.guli.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.service.edu.entity.vo.CourseCollectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
@Repository
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {
    List<CourseCollectVo> selectPageByMemberId(String memberId);
}
