package com.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.base.result.R;
import com.guli.service.edu.entity.Teacher;
import com.guli.service.edu.entity.vo.TeacherQueryVo;
import com.guli.service.edu.feign.OssFileService;
import com.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Aurora
 * @since 2020-05-27
 */
//@CrossOrigin
@Api( "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @GetMapping("list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("获取讲师列表成功");
    }



    @ApiOperation("分页讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVo teacherQuery) {
        IPage<Teacher> pageModel = teacherService.selectPage(page, limit, teacherQuery);
        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam(value = "讲师对象", required = true) @RequestBody Teacher teacher) {
        boolean result = teacherService.save(teacher);
        if (result) {
            return R.ok().message("成功");
        } else {
            return R.error().message("失败");
        }
    }

    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R updataById(@ApiParam(value = "讲师对象", required =true)@RequestBody Teacher teacher){
        boolean result=teacherService.updateById(teacher);
        if(result){
            return R.ok().message("修改成功");
        }else {
            return R.error().message("失败");
        }
    }

    @ApiOperation("根据id获取讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "讲师ID", required = true) @PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if(teacher != null){
            return R.ok().data("item", teacher);
        }else{
            return R.error().message("数据不存在");

        }
    }

    @ApiOperation("根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeRows(
            @ApiParam(value = "讲师id列表", required = true)
            @RequestBody List<String> idList){
        boolean result = teacherService.removeByIds(idList);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据左关键字查询讲师名列表")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(
            @ApiParam(value = "查询关键字", required = true)
            @PathVariable String key){
        List<Map<String, Object>> nameList = teacherService.selectNameListByKey(key);
        return R.ok().data("nameList", nameList);
    }


    @ApiOperation(value = "根据ID删除讲师", notes = "根据ID删除讲师，逻辑删除")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "讲师ID", required = true) @PathVariable String id){
        //删除图片
        teacherService.removeAvatarById(id);
        //删除讲师
        boolean result = teacherService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }
}

