package com.leyou.sms.mq;

import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 45207
 * @create 2018-09-06 20:08
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired SmsProperties prop;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("ly.sms.verify.queue"),
            exchange = @Exchange(value = "ly.sms.exchange", type = ExchangeTypes.TOPIC),
            key = "ly.sms.verify.code"
    ))
    public void listenVerifyCode(Map<String,String> msg){
        if (msg == null) {
            return;
        }
        String phone = msg.get("phone");
        if (StringUtils.isBlank(phone)) {
            return;
        }
        //移除手机数据,剩下的是短信参数
        msg.remove("phone");
        smsUtil.sendSms(prop.getSignName(),prop.getVerifyCodeTemplate(),phone,msg);

    }
}
