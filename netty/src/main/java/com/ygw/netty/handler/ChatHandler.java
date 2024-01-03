package com.ygw.netty.handler;

import com.ygw.common.entty.ChatMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<ChatMsg> {

    private RabbitTemplate rabbitTemplate;

    public ChatHandler(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatMsg chatMsg) throws Exception {
        log.info("用户聊天的信息"+chatMsg);

        // 消息入库

        // 转发给交换机
        rabbitTemplate.convertAndSend("chat_exchange","",chatMsg);
    }
}
