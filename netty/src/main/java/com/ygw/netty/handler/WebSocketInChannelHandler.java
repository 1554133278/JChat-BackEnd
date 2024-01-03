package com.ygw.netty.handler;

import com.alibaba.fastjson2.JSON;
import com.ygw.common.entty.ChatMsg;
import com.ygw.common.entty.ConnMsg;
import com.ygw.common.entty.HeardMsg;
import com.ygw.common.entty.NettyMsg;
import com.ygw.netty.channel.ChannelGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketInChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        log.info("客户端发送的数据:"+text);

        // 根据不同的类型转成具体的实体类
        // 在交给具体的handler处理
        // 把JSON字符串转成对象
        NettyMsg nettyMsg = JSON.parseObject(text, NettyMsg.class);
        if(nettyMsg.getType() == 1){ // 新的连接
            nettyMsg = JSON.parseObject(text, ConnMsg.class);
        }else if(nettyMsg.getType() == 2){
            nettyMsg = JSON.parseObject(text, HeardMsg.class);
        }else if(nettyMsg.getType() == 3){ // 单聊
            nettyMsg = JSON.parseObject(text, ChatMsg.class);
        }

        // 往下传递
        channelHandlerContext.fireChannelRead(nettyMsg);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("新客户端连接。。");
    }

    // 客户端断开后重Map中删除这个连接
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
       log.info("客户端断开。。");
        ChannelGroup.removeChannel(ctx.channel());
    }
}
