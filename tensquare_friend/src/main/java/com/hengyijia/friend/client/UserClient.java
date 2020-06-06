package com.hengyijia.friend.client;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sun.javafx.robot.impl.BaseFXRobot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "tensquare-user")  //指定的微服务名
public interface UserClient {
    /**
     * 更新好友粉丝数和登录用户关注数
     */
    @PutMapping("/user/{userid}/{friendid}/{x}")
    public void updateFanscountAndFollowcount(@PathVariable("userid") String userid,
                                              @PathVariable("friendid") String friendid,
                                              @PathVariable("x") Integer x);
}
