package com.hengyijia.qa.client.impl;

import com.hengyijia.qa.client.BaseClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class BaseClientImpl implements BaseClient {
    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR,"熔断器触发了！");
    }
}
