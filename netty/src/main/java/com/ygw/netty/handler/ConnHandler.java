package com.ygw.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ygw.common.entty.ConnMsg;
import com.ygw.common.entty.ShutDownMsg;
import com.ygw.netty.channel.ChannelGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
public class ConnHandler extends SimpleChannelInboundHandler<ConnMsg> {

    private StringRedisTemplate redisTemplate;

    public ConnHandler(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ConnMsg connMsg) throws Exception {
        log.info("新的连接。。要保存到Map中"+connMsg);

        // 把新到的连接添加到map中
        ChannelGroup.addChannel(connMsg.getDid(),channelHandlerContext.channel());

        // 根据用户id从Redis中查询did
        Integer uid = connMsg.getUid();
        String did = connMsg.getDid();
        String redisDid = redisTemplate.opsForValue().get(uid.toString());
        if(redisDid != null && !redisDid.equals(did)){
            ShutDownMsg shutDownMsg = new ShutDownMsg();
            TextWebSocketFrame resp = new TextWebSocketFrame(JSON.toJSONString(shutDownMsg));
            channelHandlerContext.writeAndFlush(resp);
        }
    }
}
