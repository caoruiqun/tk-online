package com.taikang.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.taikang.common.utils.JsonUtils;
import com.taikang.sms.config.SmsProperties;
import com.taikang.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Author: 98050
 * @Time: 2018-10-22 19:21
 * @Feature:短信服务监听器
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "tk.sms.verify.code.queue",durable = "true"),
            exchange = @Exchange(name = "tk.sms.exchange",type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}
    ))
    public void listenSms(Map<String,String> msg){
//        if (msg == null || msg.size() <= 0){
//            //不做处理
//            return;
//        }
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code)){
            //发送消息
            try {
                SendSmsResponse response = this.smsUtils.sendSms(phone,smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate(), JsonUtils.toString(code));
            }catch (ClientException e){
                return;
            }
        }else {
            //不做处理
            return;
        }
    }
}
