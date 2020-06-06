package com.hengyijia.spit.controller;

import com.hengyijia.spit.pojo.Spit;
import com.hengyijia.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功", spitService.findAll());
    }

    @GetMapping("/{spitId}")
    public Result findById(@PathVariable String spitId){

        return new Result(true, StatusCode.OK,"查询成功", spitService.findById(spitId));
    }
    @PutMapping("/{spitId}")
    public Result update(@PathVariable String spitId, @RequestBody Spit spit){

        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"更新成功");
    }
    @PostMapping
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }
    @DeleteMapping("/{spitId}")
    public Result deleteById(@PathVariable String spitId){

        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除成功");
    }
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentid, @PathVariable Integer page, @PathVariable Integer size){
        Page<Spit> pageData = spitService.findByParentid(parentid, page, size);

        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pageData.getTotalElements(),pageData.getContent()));
    }

    @PutMapping("/thumbup/{spitId}")
    public Result thumbup(@PathVariable String spitId){

        //现在暂时没做认证，先把userId写死
        String userid = "12";
        //查询缓存判断当前用户是否已经点过赞，不为null，代表已经点过赞
        if(redisTemplate.opsForValue().get("thumbup_" + userid) != null){
            return new Result(false,StatusCode.REPEAT_ERROR,"不能重复点赞");
        }
        spitService.thumbup(spitId);
        //点赞成功后，写入缓存            后面的参数，写什么都无所谓
        redisTemplate.opsForValue().set("thumbup_" + userid,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
