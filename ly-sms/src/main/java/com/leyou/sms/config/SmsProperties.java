package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 45207
 * @create 2018-09-06 19:53
 */

@ConfigurationProperties(prefix = "ly.sms")
@Data
public class SmsProperties {
    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;
}
