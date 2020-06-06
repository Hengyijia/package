package com.hengyijia.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.hengyijia.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    @RabbitHandler
    public void executeSms(Map<String,String> map){

        String mobile = map.get("mobile");
        String checkCode = map.get("checkCode");

        try {
            smsUtil.sendSms(mobile, template_code, sign_name, "{\"code\":\""+checkCode+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
