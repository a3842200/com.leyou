package com.leyou.tset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 45207
 * @create 2018-09-06 20:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsTest {

    @Autowired
    private AmqpTemplate template;

    @Test
    public void testSendMsg(){
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", "15618113527");
        msg.put("code", "123456");
        template.convertAndSend("ly.sms.exchange", "ly.sms.verify.code", msg);
    }
}
