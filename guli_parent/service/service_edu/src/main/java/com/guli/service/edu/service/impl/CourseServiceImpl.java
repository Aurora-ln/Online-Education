package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.base.result.R;
import com.guli.service.base.dto.CourseDto;
import com.guli.service.edu.entity.*;
import com.guli.service.edu.entity.form.CourseInfoForm;
import com.guli.service.edu.entity.vo.*;
import com.guli.service.edu.feign.OssFileService;
import com.guli.service.edu.mapper.*;
import com.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    //注意：为了避免idea在这个位置报告找不到依赖的错误，
    //我们可以在CourseDescriptionMapper接口上添加@Repository注解
    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private OssFileService ossFileService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //courseInfoForm页面信息
        //course，courseDescription  数据库信息
        //保存课程基本信息
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.insert(course);

        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        //CourseServiceImpl直接服务于Course，如果有其他表要注入！！！
        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        //从course表中取数据
        Course course = baseMapper.selectById(id);
        if(course == null){
            return null;
        }

        //从course_description表中取数据
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        //创建courseInfoForm对象
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);
        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {

        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("c.gmt_create");
        //获取查询条件
        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("c.title", title);
        }
        if (!StringUtils.isEmpty(teacherId) ) {
            queryWrapper.eq("c.teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("c.subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("c.subject_id", subjectId);
        }

        Page<CourseVo> pageParam = new Page<>(page, limit);
        //放入分页参数和查询条件参数，mp会自动组装
        //注意此处querywapper使用
        List<CourseVo> records = baseMapper.selectPageByCourseQueryVo(pageParam, queryWrapper);

        pageParam.setRecords(records);
        return pageParam;
    }

    @Override
    //删除封面
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);
        if(course!=null){
            String cover = course.getCover();
            if(!StringUtils.isEmpty(cover)){
                //删除图片
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {

        //收藏信息：course_collect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //评论信息：comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);

        //课程信息：course
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
       Course course=new Course();
       course.setId(id);
       course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

        //查询已发布的课程
        queryWrapper.eq("status", Course.COURSE_NORMAL);

        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", webCourseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())) {
            queryWrapper.eq("subject_id", webCourseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())) {
            if(webCourseQueryVo.getType() == null || webCourseQueryVo.getType() == 1){
                queryWrapper.orderByAsc("price");
            }else{
                queryWrapper.orderByDesc("price");
            }
        }

        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo selectWebCourseVoById(String id) {
        //更新课程浏览数
        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        //获取组装的课程信息
        return baseMapper.selectWebCourseVoById(id);
    }

    @Override
    @Cacheable(value = "index", key = "'selectHotCourse'")
    public List<Course> selectHotCourse() {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");

        return baseMapper.selectList(queryWrapper);

    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        //也可以多次查询，但会增加数据库压力
        return baseMapper.selectCourseDtoById(courseId);
    }

    @Override
    public void updateBuyCountById(String id) {
        Course course = baseMapper.selectById(id);
        course.setBuyCount(course.getBuyCount() + 1);
        this.updateById(course);
    }

}
