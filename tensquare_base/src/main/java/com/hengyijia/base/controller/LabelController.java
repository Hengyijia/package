package com.hengyijia.base.controller;
import com.hengyijia.base.pojo.Label;
import com.hengyijia.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //跨域请求
@RefreshScope//监听器刷新 会刷新自定义的配置文件
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping
    public Result findAll(){

        //int i = 1 / 0; 测试异常捕获

        return new Result(true, StatusCode.OK,"查询成功", labelService.findAll());
    }

    @GetMapping("/{labelId}")
    public Result findById(@PathVariable String labelId){

        return new Result(true, StatusCode.OK,"查询成功",labelService.findById(labelId));
    }

    @PostMapping
    public Result save(@RequestBody Label label){

        labelService.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{labelId}")
    public Result updateById(@PathVariable String labelId, @RequestBody Label label){

        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @DeleteMapping(("/{labelId}"))
    public Result deleteById(@PathVariable String labelId){

        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label){
        List<Label> list = labelService.findSearch(label);

        return new Result(true,StatusCode.OK,"查询成功",list);

    }

    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@RequestBody Label label, @PathVariable Integer page, @PathVariable Integer size){

        //把参数封装到pageData

        Page<Label> pageData = labelService.pageQuery(label,page,size);

        //前端需要的数据只有两个
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));

    }
}
