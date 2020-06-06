package com.hengyijia.qa.client;

import com.hengyijia.qa.client.impl.BaseClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "tensquare-base", fallback = BaseClientImpl.class)
public interface BaseClient {
    @GetMapping("/label/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId);
}
