package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.mapper.UserMapper;
import com.qgailab.authsystem.model.dto.*;
import com.qgailab.authsystem.model.po.UserPo;
import com.qgailab.authsystem.model.vo.AuthVo;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.RegisterService;
import com.qgailab.authsystem.service.CacheService;
import com.qgailab.authsystem.utils.DateUtil;
import com.qgailab.authsystem.utils.TokenUtil;
import com.qgailab.authsystem.utils.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @author: SheledonPeng
 * @Date: $
 */
@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserMapper userMapper;




    @Override
    public Status checkIdCard(RegisterDto registerDto) {


        // 判断前端传送的身份证是否合法
        // 判断参数的正确性
        if (VerifyUtil.isNull(registerDto)
                || VerifyUtil.isNull(registerDto.getIdCardMachine())){

            log.info("前端传入参数不正确,返回14！");
            return Status.PARAM_ERROR;
        }
        log.info("检测TCP客户端在线状态，并发送检查健康状态的信息至嵌入式");

        // 检测TCP客户端在线状态，并发送检查健康状态的信息至嵌入式
        if ( ! TcpMsgSupervise.checkMachineHealth(registerDto.getIdCardMachine(),
                MachineType.IdCardMachine)){
            log.info("TCP客户端不在线，发送故障至前端");
            return Status.BROKEN;
        }

        try {
            // 等待TCP信息的回传
            Thread.sleep(3000);
            log.info("等待完毕,开始从缓存中读取数据");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Status.BROKEN;
        }
        // 从缓存中读取
        Integer value = cacheService.queryMachineHealth(MachineType.IdCardMachine, registerDto.getIdCardMachine());
        // 缓存中的数据为空  此时需要再等待
        if ( null == value){
            try {
                log.info("缓存中数据为空，继续等待");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Status.BROKEN;
            }
            // 继续从缓存中读取
            value = cacheService.queryMachineHealth(MachineType.IdCardMachine, registerDto.getIdCardMachine());
            // 嵌入式不在线,为机器不健康
            if ( null == value){
                log.info("缓存中数据仍然为空，返回故障");
                return Status.BROKEN;
            }

        }
        log.info("缓存中数据为 " + value);
        //  删除缓存
        cacheService.delMachineHealth(MachineType.IdCardMachine,registerDto.getIdCardMachine());
        // 机器状态为正常,返回机器正常工作的结果
        if ( value == 1){
            log.info("机器工作状态正常，返回至前端");
            return Status.HEALTH;
        }
        log.info("机器工作状态不正常，返回至前端");
        return Status.BROKEN;

    }

    @Override
    public AuthVo loadIdCard(RegisterDto registerDto) {

        // 检查前端传输的参数
        if ( VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCardMachine())){
            log.info("前端传入参数不正确,返回14！");
            return new AuthVo(Status.PARAM_ERROR);
        }


        log.info("向嵌入式发送请求，并检验设备是否在线");
        // 向嵌入式发送请求，并检验设备是否在线
        if ( ! TcpMsgSupervise.loadIdCardInformation(registerDto.getIdCardMachine()) ){
            log.info("嵌入式设备不在线，返回至前端");
            return new AuthVo(Status.GET_ID_CARD_ERROR);
        }

        // 睡眠等待
        try {
            Thread.sleep(3000);
            log.info("等待完毕，从缓存中读取数据");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AuthVo(Status.GET_ID_CARD_ERROR);
        }

        // 从缓存中读取
        IdCardInfoDto idCardInfoDto = cacheService.queryIdCardInfo(registerDto.getIdCardMachine());
        // 若缓存中无数据
        if ( null == idCardInfoDto){

            try {
                log.info("缓存中数据为空，继续等待");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new AuthVo(Status.GET_ID_CARD_ERROR);
            }
            // 第二次从缓存中读取
            idCardInfoDto = cacheService.queryIdCardInfo(registerDto.getIdCardMachine());
            // 读取身份证信息失败
            if ( null == idCardInfoDto){
                log.info("缓存中数据仍然为空，返回至前端");
                return new AuthVo(Status.GET_ID_CARD_ERROR);
            }
        }
        log.info("缓存中的数据为 " + idCardInfoDto);
        // 删除缓存
        cacheService.delIdCardInfoCache(registerDto.getIdCardMachine());
        // 已注册
        if ( userMapper.getCountByIdCard(idCardInfoDto.getIdCard()) > 0){
            log.info("此身份证已注册,直接登录！");
            String token = TokenUtil.createToken(idCardInfoDto.getIdCard());
            cacheService.cacheToken(token,idCardInfoDto.getIdCard());
            return new AuthVo(Status.REGISTER,null,token);
        }

        UserPo userPo = new UserPo();
        // 复制数据至po中
        BeanUtils.copyProperties(idCardInfoDto,userPo);
        log.info("从DTO中复制数据至po，复制后po为:" + userPo);
        // 缓存
        cacheService.cacheUserInfo(userPo);
        return new AuthVo(Status.GET_ID_CARD_OK,idCardInfoDto.getIdCard());
    }

    @Override
    public AuthVo loadFinger(RegisterDto registerDto) {

        // 判断前端传入的参数正确与否
        if (VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCard())
        || VerifyUtil.isNull(registerDto.getFingerMachine())){
            log.info("前端传入参数不正确,返回14！");
            return new AuthVo(Status.PARAM_ERROR);
        }

        log.info("判断嵌入式设备的在线状态 并发送读取指令至嵌入式");
        // 判断嵌入式设备的在线状态 并发送信息至嵌入式客户端
        if ( ! TcpMsgSupervise.loadFingerInformation(registerDto.getFingerMachine())){
            log.info("嵌入式不在线，发送错误至前端");
            return new AuthVo(Status.FINGERPRINT_ERROR);
        }

        // 睡眠等待结果
        try {
            Thread.sleep(10000);
            log.info("等待完毕，开始读取缓存");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AuthVo(Status.FINGERPRINT_ERROR);
        }
        // 从缓存中读取指纹信息
        FingerInfoDto fingerInfoDto = cacheService.queryFingerInfo(registerDto.getFingerMachine());
        // 当缓存中未读到信息
        if ( null == fingerInfoDto ){

            // 再次睡眠等待结
            try {
                log.info("缓存中数据为空，继续等待");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new AuthVo(Status.FINGERPRINT_ERROR);
            }
            fingerInfoDto = cacheService.queryFingerInfo(registerDto.getFingerMachine());
            // 缓存中数据仍然为空
            if ( null == fingerInfoDto){
                log.info("缓存中数据仍然为空，返回故障至前端");
                return new AuthVo(Status.FINGERPRINT_ERROR);
            }
        }
        // 删除缓存
        cacheService.delFingerInfoCache(registerDto.getFingerMachine());
        log.info("缓存中指纹信息的数据为 " + fingerInfoDto);
        UserPo userPo = cacheService.queryUserInfo(registerDto.getIdCard());
        if ( null == userPo ){
            // todo 未知错误 需要新字段返回
            log.info("缓存中不存在身份证号为:【{}】的用户，请注意!" + registerDto.getIdCard());
            return new AuthVo(Status.FINGERPRINT_ERROR);
        }
        log.info("从缓存中读取用户po为 " + userPo);
        // 复制指纹信息至PO
        BeanUtils.copyProperties(fingerInfoDto,userPo);
        log.info("复制指纹信息DTO至Po,复制后Po为 " + userPo);
        // 加入缓存区
        cacheService.cacheUserInfo(userPo);
        return new AuthVo(fingerInfoDto.getFingerInfo(),Status.FINGERPRINT_OK);
    }

    @Override
    public AuthVo faceID(RegisterDto registerDto) {
        // 判断前端传入的参数正确与否
        if (VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCard())
                || VerifyUtil.isNull(registerDto.getFaceIDMachine())){
            log.info("前端传入参数不正确,返回14！");
            return new AuthVo(Status.PARAM_ERROR);
        }
        log.info("判断嵌入式设备的在线状态 并发送读取指令至嵌入式");
        // 判断嵌入式设备的在线状态 并发送信息至嵌入式客户端
        if ( ! TcpMsgSupervise.loadFaceIDInformation(registerDto.getFaceIDMachine())){
            log.info("嵌入式不在线，发送错误至前端");
            return new AuthVo(Status.PHOTO_ERROR);
        }

        // 睡眠等待结果
        try {
            Thread.sleep(10000);
            log.info("等待完毕，开始读取缓存");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AuthVo(Status.PHOTO_ERROR);
        }
        // 从缓存中读取指纹信息
        FaceIDInfoDto faceIDInfoDto = cacheService.queryFaceIDInfo(registerDto.getFaceIDMachine());
        // 当缓存中未读到信息
        if ( null == faceIDInfoDto ){

            // 再次睡眠等待结果返回
            try {
                log.info("缓存中数据为空，继续等待");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new AuthVo(Status.PHOTO_ERROR);
            }
            faceIDInfoDto = cacheService.queryFaceIDInfo(registerDto.getFaceIDMachine());
            // 缓存中数据仍然为空
            if ( null == faceIDInfoDto){
                log.info("缓存中数据仍然为空，返回故障至前端");
                return new AuthVo(Status.PHOTO_ERROR);
            }
        }
        // 删除缓存
        cacheService.delFaceIDInfoCache(registerDto.getFaceIDMachine());
        log.info("缓存中人脸信息的数据为 " + faceIDInfoDto);
        UserPo userPo = cacheService.queryUserInfo(registerDto.getIdCard());
        if ( null == userPo ){
            // todo 未知错误 需要新字段返回
            log.info("缓存中不存在身份证号为:【{}】的用户，请注意!" + registerDto.getIdCard());
            return new AuthVo(Status.PHOTO_ERROR);
        }
        log.info("从缓存中读取用户po为 " + userPo);
        // 复制指纹信息至PO
        BeanUtils.copyProperties(faceIDInfoDto,userPo);
        log.info("复制人脸信息DTO至Po,复制后Po为 " + userPo);
        // 加入缓存区
        cacheService.cacheUserInfo(userPo);
        return new AuthVo(faceIDInfoDto.getFaceIDInfo(),Status.PHOTO_OK);
    }

    @Override
    public AuthVo signature(RegisterDto registerDto) {

        // 判断前端参数的正确与否
        if ( VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCard())
                || VerifyUtil.isNull(registerDto.getSignatureMachine())){
            log.info("前端传入参数不正确,返回14！");
            return new AuthVo(Status.PARAM_ERROR);
        }
        log.info("判断签名仪的在线与否，并发送读取指令");
        //  判断签名仪的在线与否，并发送读取指令
        if ( ! TcpMsgSupervise.loadSignatureInformation(registerDto.getSignatureMachine())){
            log.info("签名仪不在线，返回结果至前端");
            return new AuthVo(Status.SIGNATURE_ERROR);
        }

        // 睡眠等待结果返回
        try {
            Thread.sleep(10000);
            log.info("睡眠等待结束，开始从缓存中读取数据");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AuthVo(Status.SIGNATURE_ERROR);
        }
        // 从缓存中读取签名信息
        SignatureInfoDto signatureInfoDto = cacheService.querySignatureInfo(registerDto.getSignatureMachine());
        // 缓存中数据为空
        if ( null == signatureInfoDto){

            // 继续休眠等待结果
            try {
                log.info("缓存中数据为空，继续等待");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new AuthVo(Status.SIGNATURE_ERROR);
            }
            // 从缓存中继续读取
            signatureInfoDto = cacheService.querySignatureInfo(registerDto.getSignatureMachine());
            // 二次读取仍然未读取
            if ( null == signatureInfoDto){
                log.info("缓存中数据仍然为空，返回错误至前端");
                return new AuthVo(Status.SIGNATURE_ERROR);
            }
        }
        log.info("从指纹仪缓存中读取的数据为 " + signatureInfoDto);
        // 删除缓存
        cacheService.delSignatureInfoCache(registerDto.getSignatureMachine());
        // 从缓存中读取用户信息
        UserPo userPo = cacheService.queryUserInfo(registerDto.getIdCard());
        if ( null == userPo ){
            log.info("缓存中不存在身份证号为:【{}】的用户，请注意!" + registerDto.getIdCard());
            // todo 未知错误 需要新字段返回
            return new AuthVo(Status.SIGNATURE_ERROR);
        }
        log.info("从缓存中读取用户的信息，userPo为 " + userPo);
        userPo.setRegisterDate(DateUtil.getCurrentTime());
        BeanUtils.copyProperties(signatureInfoDto, userPo);
        log.info("复制签名DTO至userPo中，复制后为 " + userPo);
        userMapper.insertUser(userPo);
        log.info("将用户信息插入mysql成功，注册完毕！");
        // 删除缓存用户信息
        cacheService.delUserCache(registerDto.getIdCard());
        return new AuthVo(signatureInfoDto.getSignature(),Status.SIGNATURE_OK);

    }



}
