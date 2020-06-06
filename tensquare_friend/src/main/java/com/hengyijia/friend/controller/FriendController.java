package com.hengyijia.friend.controller;

import com.hengyijia.friend.client.UserClient;
import com.hengyijia.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClient userClient;
    /**
     * 添加好友或者添加非好友
     * @param friendid
     * @param type
     * @return
     */
    @PutMapping("/like/{friendid}/{type}")
    public Result addFriend(@PathVariable("friendid") String friendid, @PathVariable("type") String type ){

        //验证是否登陆，并且拿到当前用户的id
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false,StatusCode.LOGIN_ERROR,"权限不足！");
        }
        //获取当前登陆用户的id
        String userid = claims.getId();

        //判断是添加好友还是添加非好友    type=1 添加好友， type=2 添加非好友
        if(type != null){
            if(type.equals("1")){
                //添加好友
                int result = friendService.addFriend(userid, friendid);
                if(result == 0){
                    return new Result(false,StatusCode.ERROR,"不能重复添加");
                } else if(result == 1){
                    //自己的关注+1，好友的粉丝+1
                    userClient.updateFanscountAndFollowcount(userid, friendid, 1);
                    return new Result(true,StatusCode.OK,"添加好友成功");
                }
            } else if(type.equals("2")){
                //添加非好友
                int result = friendService.addNoFriend(userid,friendid);
                if(result == 0){
                    return new Result(false,StatusCode.ERROR,"不能重复添加");
                } else if(result == 1){
                    return new Result(true,StatusCode.OK,"添加非好友成功");
                }
            }
            return new Result(false,StatusCode.ERROR,"参数错误");
        } else {
            return new Result(false,StatusCode.ERROR,"参数错误");
        }
    }

    @DeleteMapping("/{friendid}")
    public Result deleteFriend(@PathVariable String friendid){

        //验证是否登陆，并且拿到当前用户的id
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false,StatusCode.LOGIN_ERROR,"权限不足！");
        }
        //获取当前登陆用户的id
        String userid = claims.getId();

        friendService.deleteFriend(userid,friendid);
        //删除好友，自己的关注-1，好友的粉丝-1
        userClient.updateFanscountAndFollowcount(userid, friendid, -1);
        return new Result(true, StatusCode.OK, "删除好友成功");
    }
}
