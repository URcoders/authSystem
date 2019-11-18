package com.qgailab.authsystem.net.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author linxu
 * @date 2019/8/21
 * <tip>take care of yourself.everything is no in vain.</tip>
 */
@Component
@Slf4j
@ChannelHandler.Sharable//如果不是共享处理器，就无法多次添加、移除
public class SocketHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        //do some process
        //response
        log.info("收到socket端信息为 " + s);
        try {
            // 解析嵌入式的json信息
            Object object = objectMapper.readValue(s,Object.class);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //读写超时
        if (cause instanceof ReadTimeoutException) {
            //do some task
            log.warn("read idle timeout",ctx.channel().remoteAddress());
            if ( ! ctx.channel().isActive()){

            }
        }
        if (cause instanceof WriteTimeoutException){
            //do some task
        }
    }

    /**
     * 由空转处理器触发
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果是由空转触发
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                //读空转超时

                ctx.channel().close();
            }
            if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
                //写空转超时
            }
            ctx.channel().close();
            ctx.close();
        }
    }
}
