package com.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private Integer sort;

    //一对多  集合型先new 防止空指针异常
    private List<VideoVo> children = new ArrayList<>();

}