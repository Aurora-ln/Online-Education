package com.guli.service.edu.service.impl;

import com.guli.service.edu.entity.Comment;
import com.guli.service.edu.mapper.CommentMapper;
import com.guli.service.edu.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
