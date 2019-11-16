package com.qgailab.authsystem.net.supervise;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @author: SheledonPeng
 * @Date: $
 */
public class ChannelSupervise {

    private  static final ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // K->指纹仪器ID V->ChannelId
    private  static final ConcurrentMap<Integer, Channel> fingerMachineMap = new ConcurrentHashMap<>();
    // K->身份证记录仪ID  V->ChannelId
    private  static final ConcurrentHashMap<Integer,Channel> idCardMachineMap = new ConcurrentHashMap<>();
    // K->签名验证仪ID V->ChannelId
    private  static final ConcurrentHashMap<Integer,Channel> signatureMachineMap = new ConcurrentHashMap<>();
    // todo 图像暂时未做

    public static void addfingerMachineChannel(Channel channel , Integer machineId) {
        GlobalGroup.add(channel);
        fingerMachineMap.put(machineId, channel);
    }
    public static void addidCardMachineChannel(Channel channel , Integer machineId) {
        GlobalGroup.add(channel);
        idCardMachineMap.put(machineId, channel);
    }
    public static void addSignatureMachineChannel(Channel channel , Integer machineId) {
        GlobalGroup.add(channel);
        signatureMachineMap.put(machineId, channel);
    }

    /**
     * @Description : 根据channel关闭channel通道并移除
     * @Param : [channel]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static void closeChannel(Channel channel) {

        if ( GlobalGroup.contains(channel)){

            channel.close();
            GlobalGroup.remove(channel);
            Integer id = travelMap(channel);
            Map<Integer, Channel> channelMap = getMapByChannel(channel);
            if ( id != null && channelMap != null ){
                channelMap.remove(id);
            }
        }
    }

    /**
     * @Description : 通过channel查找对应的机器ID
     * @Param : [channel]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static Integer findFingerMachineID(Channel channel){

        return travelMap(channel);
    }

    /**
     * @Description : 通过channel查找对应的机器ID
     * @Param : [channel]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static Integer findIdCardMachineID(Channel channel){

        return travelMap(channel);
    }

    /**
     * @Description : 通过channel查找对应的机器ID
     * @Param : [channel]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static Integer findSignatureMachineID(Channel channel){

        return travelMap(channel);
    }

    /**
     * @Description : 通过机器id查找channel
     * @Param : [machineId]
     * @Return : io.netty.channel.Channel
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static Channel findFingerMachineChannel(Integer machineId) {

        return fingerMachineMap.get(machineId);
    }

    /**
     * @Description : 通过机器id查找channel
     * @Param : [machineId]
     * @Return : io.netty.channel.Channel
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static Channel findIdCardMachineChannel(String machineId) {

        return idCardMachineMap.get(machineId);
    }

    /**
     * @Description : 通过机器id查找channel
     * @Param : [machineId]
     * @Return : io.netty.channel.Channel
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    public static Channel findSignatureMachineChannel(String machineId) {

        return signatureMachineMap.get(machineId);
    }

    /**
     * @Description : 根据channel获得所在的map
     * @Param : [channel]
     * @Return : java.util.Map<java.lang.Integer,io.netty.channel.Channel>
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    private static Map<Integer,Channel> getMapByChannel(Channel channel){

        Map<Integer,Channel> map = null;
        MachineType machineType = getMachineType(channel);
        // 判断map中不存在此机器
        if ( null != machineType){
            switch ( machineType){

                case FingerMachine:{
                    map = fingerMachineMap;
                    break;
                }
                case IdCardMachine:{
                    map = idCardMachineMap;
                    break;
                }
                case SignatureMachine: {
                    map = signatureMachineMap;
                    break;
                }
            }
        }
        return map;
    }

    /**
     * @Description : 通过channel遍历三个不同的map，从而获得其机器ID
     * @Param : [channel]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    private static Integer travelMap(Channel channel){

        // 判断当前channel是否在group中
        if ( ! GlobalGroup.contains(channel)){
            return null;
        }
        // 查找当前channel所在的map集合
        Map<Integer,Channel> travelMap = getMapByChannel(channel);

        if ( null != travelMap){

            Iterator<Map.Entry<Integer, Channel>> iterator = travelMap.entrySet().iterator();
            while ( iterator.hasNext()){

                Map.Entry<Integer, Channel> next = iterator.next();
                if ( next.getValue() == channel){
                    return next.getKey();
                }
            }
        }
        return null;
    }

    /**
     * @Description : 根据channel获得机器类型
     * @Param : [channel]
     * @Return : com.qgailab.authsystem.net.supervise.ChannelSupervise.MachineType
     * @Author : SheldonPeng
     * @Date : 2019-11-14
     */
    private static MachineType getMachineType(Channel channel){

        if ( idCardMachineMap.containsValue(channel)){
            return MachineType.IdCardMachine;
        }
        if ( fingerMachineMap.containsValue(channel)){
            return MachineType.FingerMachine;
        }
        if ( signatureMachineMap.containsValue(channel)){
            return MachineType.SignatureMachine;
        }
        return null;
    }
    private enum MachineType{

        SignatureMachine("SignatureMachine"),
        IdCardMachine("IdCardMachine"),
        FingerMachine("FingerMachine");


        private String machineType;

        MachineType(String machineType){
            this.machineType = machineType;
        }
        public String getMachineType(){
            return machineType;
        }

    }
}
