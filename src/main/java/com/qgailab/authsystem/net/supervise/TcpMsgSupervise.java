package com.qgailab.authsystem.net.supervise;

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

        Channel channel = ChannelSupervise.findChannel(machineId, machineType);
        if ( null == channel){
            return false;
        }
        channel.writeAndFlush("ready");
        return true;
    }

    /**
     * @Description : 读取身份证信息
     * @Param : [machineId]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean loadIdCardInfomation(Integer machineId){

        Channel channel = ChannelSupervise.findChannel(machineId,MachineType.IdCardMachine);
        if ( null == channel ){
            return false;
        }
        channel.writeAndFlush("load");
        return true;
    }


    /**
     * @Description : 读取指纹信息
     * @Param : [machineId]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean loadFingerInfomation(Integer machineId){

        Channel channel = ChannelSupervise.findChannel(machineId,MachineType.FingerMachine);
        if ( null == channel){
            return false;
        }
        channel.writeAndFlush("load");
        return true;
    }

    /**
     * @Description : 读取签名信息
     * @Param : [machineId]
     * @Return : boolean
     * @Author : SheldonPeng
     * @Date : 2019-11-16
     */
    public static boolean loadSignatureInfomation(Integer machineId) {

        Channel channel = ChannelSupervise.findChannel(machineId, MachineType.SignatureMachine);
        if (null == channel) {
            return false;
        }
        channel.writeAndFlush("load");
        return true;
    }
}
