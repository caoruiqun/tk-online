package com.taikang.search.mq;

import com.taikang.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-05-02 16:14
 **/
@Component
public class ItemListener {

    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.insert.queue",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId) {
        if (null == spuId) {
            return;
        }
        //处理消息，对索引库进行新增或修改
        searchService.createOrUpdateIndex(spuId);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.item.delete.queue",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenDelete(Long spuId) {
        if (null == spuId) {
            return;
        }
        //处理消息，对索引库进行新增或修改
        searchService.deleteIndex(spuId);
    }

}
