package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.service.edu.entity.Chapter;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.entity.vo.ChapterVo;
import com.guli.service.edu.entity.vo.VideoVo;
import com.guli.service.edu.mapper.ChapterMapper;
import com.guli.service.edu.mapper.VideoMapper;
import com.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.service.edu.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
   @Autowired
    private VideoMapper videoMapper;

   @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {

       //删除课时信息：video
       QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
       videoQueryWrapper.eq("chapter_id", id);
       videoMapper.delete(videoQueryWrapper);

       //删除章节信息：chapter
       return this.removeById(id);
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        List<ChapterVo> chapterVoList = new ArrayList<>();
        //从数据库获取
        //course_id获取章信息Chapter
        QueryWrapper<Chapter> queryWrapperChapter = new QueryWrapper<>();
        queryWrapperChapter.eq("course_id", courseId);
        queryWrapperChapter.orderByAsc("sort", "id");
        List<Chapter> chapterList = baseMapper.selectList(queryWrapperChapter);

        //course_id获取课时信息Video（通过冗余字段）
        QueryWrapper<Video> queryWrapperVideo = new QueryWrapper<>();
        queryWrapperVideo.eq("course_id", courseId);
        queryWrapperVideo.orderByAsc("sort", "id");
        List<Video> videoList = videoMapper.selectList(queryWrapperVideo);

        //封装数据
        //填充列表数据：ChapterVo列表
        for (int i = 0; i < chapterList.size(); i++) {
            Chapter chapter = chapterList.get(i);
            //创建ChapterVo对象
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoList.add(chapterVo);
            //填充列表数据：VideoVo列表
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < videoList.size(); j++) {
                Video video = videoList.get(j);
                if(chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            //将videoVo加入ChapterVo，形成嵌套
            chapterVo.setChildren(videoVoList);
        }

        return chapterVoList;
    }
}
