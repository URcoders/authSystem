package com.qgailab.authsystem.net.supervise;

import com.qgailab.authsystem.constance.Command;
import com.qgailab.authsystem.constance.MachineType;
import io.netty.channel.Channel;

/**
 * @Description : TCP消息控制器
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-16
 */

public class TcpMsgSupervise {

    /**
     * @Description : 检查机器健康状态
     * @Param : [machineId, machineType]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean checkMachineHealth(Integer machineId, MachineType machineType){

        return ChannelSupervise.sentToAll(Command.READY.getCommand());
//        Channel channel = ChannelSupervise.findChannel(machineId, machineType);
//        if ( null == channel){
//            return false;
//        }
//        channel.writeAndFlush(Command.READY.getCommand());
//        return true;
    }

    /**
     * @Description : 读取身份证信息
     * @Param : [machineId]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean loadIdCardInformation(Integer machineId){

        return ChannelSupervise.sentToAll(Command.LOAD_IDCARD.getCommand());
//        Channel channel = ChannelSupervise.findChannel(machineId,MachineType.IdCardMachine);
//        if ( null == channel ){
//            return false;
//        }
//        channel.writeAndFlush(Command.LOAD.getCommand());
//        return true;
    }


    /**
     * @Description : 读取指纹信息
     * @Param : [machineId]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean loadFingerInformation(Integer machineId){

        return ChannelSupervise.sentToAll(Command.LOAD_FINGER.getCommand());
//        Channel channel = ChannelSupervise.findChannel(machineId,MachineType.FingerMachine);
//        if ( null == channel){
//            return false;
//        }
//        channel.writeAndFlush(Command.LOAD.getCommand());
//        return true;
    }

    public static boolean loadFaceIDInformation(Integer machineId){

        return ChannelSupervise.sentToAll(Command.LOAD_FACEID.getCommand());
    }

    /**
     * @Description : 读取签名信息
     * @Param : [machineId]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean loadSignatureInformation(Integer machineId) {

        return ChannelSupervise.sentToAll(Command.LOAD_SIGNATURE.getCommand());
//        Channel channel = ChannelSupervise.findChannel(machineId, MachineType.SignatureMachine);
//        if (null == channel) {
//            return false;
//        }
//        channel.writeAndFlush(Command.LOAD.getCommand());
//        return true;
    }
}
