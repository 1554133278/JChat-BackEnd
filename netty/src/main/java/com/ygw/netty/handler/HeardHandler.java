package com.ygw.netty.handler;

import com.ygw.common.entty.HeardMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeardHandler extends SimpleChannelInboundHandler<HeardMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeardMsg heardMsg) throws Exception {
        log.info("处理心跳:"+heardMsg);

        // 响应心跳
        TextWebSocketFrame resp = new TextWebSocketFrame("heard");
        channelHandlerContext.writeAndFlush(resp);
    }
}
