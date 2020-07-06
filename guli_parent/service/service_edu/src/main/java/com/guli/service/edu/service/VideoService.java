package com.guli.service.edu.service;

import com.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.netflix.client.ClientException;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
public interface VideoService extends IService<Video> {

    //调用feign接口
    void removeMediaVideoById(String id);

    void removeMediaVideoByChapterId(String chapterId);

    void removeMediaVideoByCourseId(String courseId);
}
