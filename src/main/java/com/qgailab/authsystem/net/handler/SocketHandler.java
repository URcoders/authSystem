package com.qgailab.authsystem.net.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qgailab.authsystem.constance.Command;
import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.model.dto.FingerInfoDto;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.dto.MachineHealthDto;
import com.qgailab.authsystem.model.dto.SignatureInfoDto;
import com.qgailab.authsystem.model.pojo.FingerMachine;
import com.qgailab.authsystem.model.pojo.IdCardMachine;
import com.qgailab.authsystem.model.pojo.SignatureMachine;
import com.qgailab.authsystem.net.supervise.ChannelSupervise;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.CacheService;
import com.qgailab.authsystem.utils.ObjectUtil;
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
    @Autowired
    private CacheService cacheService;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        //do some process
        //response

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("收到socket端信息为 " + msg);
        try {
            // 解析嵌入式的json信息
            Object object = ObjectUtil.parseJson(msg.toString());

            if ( object instanceof IdCardMachine){

                ChannelSupervise.addidCardMachineChannel(ctx.channel(),((IdCardMachine) object).getIdCardMachine());
                ctx.writeAndFlush("1");

            } else if ( object instanceof FingerMachine){

                ChannelSupervise.addfingerMachineChannel(ctx.channel(),((FingerMachine) object).getFingerMachine());
                ctx.writeAndFlush("1");

            } else if ( object instanceof SignatureMachine ){

                ChannelSupervise.addSignatureMachineChannel(ctx.channel(),((SignatureMachine) object).getSignatureMachine());
                ctx.writeAndFlush("1");

            } else if ( object instanceof MachineHealthDto){

                cacheService.cacheMachineHealth(MachineType.IdCardMachine,
                        ((MachineHealthDto) object).getIdCardMachine(),((MachineHealthDto) object).getHealth());
                ctx.writeAndFlush(Command.ACK.getCommand());

            } else if ( object instanceof FingerInfoDto){

                if ( ChannelSupervise.findChannel(((FingerInfoDto) object).getFingerMachine()
                        ,MachineType.FingerMachine) == null ){
                    ctx.writeAndFlush("请先授权后再发送信息!");
                    return;
                }
                cacheService.cacheFingerInfo((FingerInfoDto) object);
                ctx.writeAndFlush(Command.ACK.getCommand());

            } else if ( object instanceof SignatureInfoDto){

                if ( ChannelSupervise.findChannel(((SignatureInfoDto) object).getSignatureMachine(),
                        MachineType.SignatureMachine) == null ){
                    ctx.writeAndFlush("请先授权后再发送信息!");
                    return;
                }
                cacheService.cacheSignatureInfo((SignatureInfoDto)object);
                ctx.writeAndFlush(Command.ACK.getCommand());

            } else if ( object instanceof IdCardInfoDto){

                if ( ChannelSupervise.findChannel(((IdCardInfoDto) object).getIdCardMachine(),
                        MachineType.IdCardMachine) == null ){
                    ctx.writeAndFlush("请先授权后再发送信息!");
                    return;
                }
                cacheService.cacheIdCardInfo((IdCardInfoDto) object);
                ctx.writeAndFlush(Command.ACK.getCommand());
            } else {
                log.info("嵌入式下毒");
            }

        } catch (Exception e) {
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
