package com.leyou.page.mq;

import com.leyou.page.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 45207
 * @create 2018-09-05 21:29
 */

@Component
public class ItemListener {

    @Autowired
    private PageService pageService;

    //增和改一起(改是先删再增)
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ly.page.create.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))
    public void listenCreate(Long spuId) {
        if (spuId != null) {
            //创建html页面(为什么要RuntimeException, spring的事务只能处理RuntimeException)
            pageService.createHtml(spuId);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ly.page.delete.queue", durable = "true"),
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            key = "item.delete"
    ))
    public void listenDelete(Long spuId) {
        if (spuId != null) {
            //删除html页面
            pageService.deleteHtml(spuId);
        }
    }
}
