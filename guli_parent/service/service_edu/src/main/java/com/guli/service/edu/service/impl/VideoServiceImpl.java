package com.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.service.edu.entity.Video;
import com.guli.service.edu.feign.VodMediaService;
import com.guli.service.edu.mapper.VideoMapper;
import com.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.client.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodMediaService vodMediaService;

    @Override
    public void removeMediaVideoById(String id) {

        log.warn("VideoServiceImpl：video id = " + id);

        //根据videoid找到视频id
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();

        log.warn("VideoServiceImpl：videoSourceId= " + videoSourceId);

        List<String> videoSourceIdList = new ArrayList<>();
        videoSourceIdList.add(videoSourceId);

        vodMediaService.removeVideoByIdList(videoSourceIdList);

    }

    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
       //组装，根据chapter_id查询video_source_id
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("chapter_id", chapterId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        //获取list
        List<String> videoSourceIdList = this.getVideoSourceIdList(maps);
        //调用feign
        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    @Override
    public void removeMediaVideoByCourseId(String courseId) {

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id", courseId);
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);
        //获取list
        List<String> videoSourceIdList = this.getVideoSourceIdList(maps);

        vodMediaService.removeVideoByIdList(videoSourceIdList);
    }

    /**
     * 获取阿里云视频id列表
     */
    private List<String> getVideoSourceIdList(List<Map<String, Object>> maps){
        List<String> videoSourceIdList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            String videoSourceId = (String)map.get("video_source_id");
            videoSourceIdList.add(videoSourceId);
        }
        return videoSourceIdList;
    }

}
