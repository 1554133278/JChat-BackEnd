package com.ygw.listener;

import com.alibaba.fastjson.JSON;
import com.ygw.common.entty.ChatMsg;
import com.ygw.common.entty.ShutDownMsg;
import com.ygw.netty.channel.ChannelGroup;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "chat_queue_${netty.port}")
public class ChatQueueListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitHandler
    public void wsMsg(ShutDownMsg shutDownMsg){

        // 查看当前设备号是否在本机
        String did = shutDownMsg.getDid();
        Channel channel = ChannelGroup.getChannel(did);
        log.info("监听器获得数据。。"+shutDownMsg);
        if(channel != null){
            log.info("监听器发送消息给客户端完成。。。");

            TextWebSocketFrame resp = new TextWebSocketFrame(JSON.toJSONString(shutDownMsg));
            channel.writeAndFlush(resp);
        }
    }

    @RabbitHandler
    public void wsChat(ChatMsg chatMsg){
        log.info("监听器中收到数据："+chatMsg);
        // 根据tid查询did
        String did = redisTemplate.opsForValue().get(chatMsg.getTid().toString());

        // 根据did查询channel
        Channel channel = ChannelGroup.getChannel(did);
        if(channel != null){
            TextWebSocketFrame resp = new TextWebSocketFrame(JSON.toJSONString(chatMsg));
            channel.writeAndFlush(resp);
            log.info("监听器消息发送成功。。。。");
        }
    }
}
