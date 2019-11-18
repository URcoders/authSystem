package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.mapper.UserMapper;
import com.qgailab.authsystem.model.dto.FingerInfoDto;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.dto.SignatureInfoDto;
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
            return Status.PARAM_ERROR;
        }
        // 检测TCP客户端在线状态，并发送检查健康状态的信息至嵌入式
        if ( ! TcpMsgSupervise.checkMachineHealth(registerDto.getIdCardMachine(),
                MachineType.IdCardMachine)){
            return Status.BROKEN;
        }

        try {
            // 等待TCP信息的回传
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Status.BROKEN;
        }
        // 从缓存中读取
        Integer value = cacheService.queryMachineHealth(MachineType.IdCardMachine, registerDto.getIdCardMachine());
        // 缓存中的数据为空  此时需要再等待
        if ( null == value){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Status.BROKEN;
            }
            // 继续从缓存中读取
            value = cacheService.queryMachineHealth(MachineType.IdCardMachine, registerDto.getIdCardMachine());
            // 嵌入式不在线,为机器不健康
            if ( null == value){
                return Status.BROKEN;
            }

        }
        //  删除缓存
        cacheService.delMachineHealth(MachineType.IdCardMachine,registerDto.getIdCardMachine());
        // 机器状态为正常,返回机器正常工作的结果
        if ( value == 1){
            return Status.HEALTH;
        }
        return Status.BROKEN;

    }

    @Override
    public AuthVo loadIdCard(RegisterDto registerDto) {

        // 检查前端传输的参数
        if ( VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCardMachine())){
            return new AuthVo(Status.PARAM_ERROR);
        }
        // 向嵌入式发送请求，并检验设备是否在线
        if ( ! TcpMsgSupervise.loadIdCardInformation(registerDto.getIdCardMachine()) ){
            return new AuthVo(Status.GET_ID_CARD_ERROR);
        }

        // 睡眠等待
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new AuthVo(Status.GET_ID_CARD_ERROR);
        }

        // 从缓存中读取
        IdCardInfoDto idCardInfoDto = cacheService.queryIdCardInfo(registerDto.getIdCardMachine());
        // 若缓存中无数据
        if ( null == idCardInfoDto){

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new AuthVo(Status.GET_ID_CARD_ERROR);
            }
            // 第二次从缓存中读取
            idCardInfoDto = cacheService.queryIdCardInfo(registerDto.getIdCardMachine());
            // 读取身份证信息失败
            if ( null == idCardInfoDto){
                return new AuthVo(Status.GET_ID_CARD_ERROR);
            }
        }

        // 删除缓存
        cacheService.delIdCardInfoCache(registerDto.getIdCardMachine());
        // 已注册
        if ( userMapper.getCountByIdCard(idCardInfoDto.getIdCard()) > 0){
            String token = TokenUtil.createToken(idCardInfoDto.getIdCard());
            cacheService.cacheToken(token,idCardInfoDto.getIdCard());
            return new AuthVo(Status.REGISTER,null,token);
        }

        UserPo userPo = new UserPo();
        // 复制数据至po中
        BeanUtils.copyProperties(idCardInfoDto,userPo);
        // 缓存
        cacheService.cacheUserInfo(userPo);
        return new AuthVo(Status.GET_ID_CARD_OK,registerDto.getIdCard());
    }

    @Override
    public Status loadFinger(RegisterDto registerDto) {

        // 判断前端传入的参数正确与否
        if (VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCard())
        || VerifyUtil.isNull(registerDto.getFingerMachine())){
            return Status.PARAM_ERROR;
        }
        // 判断嵌入式设备的在线状态 并发送信息至嵌入式客户端
        if ( ! TcpMsgSupervise.loadFingerInformation(registerDto.getFingerMachine())){
            return Status.FINGERPRINT_ERROR;
        }

        // 睡眠等待结果
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Status.FINGERPRINT_ERROR;
        }
        // 从缓存中读取指纹信息
        FingerInfoDto fingerInfoDto = cacheService.queryFingerInfo(registerDto.getFingerMachine());
        // 删除缓存
        cacheService.delFingerInfoCache(registerDto.getFingerMachine());
        // 当缓存中未读到信息
        if ( null == fingerInfoDto ){

            // 再次睡眠等待结果返回
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Status.FINGERPRINT_ERROR;
            }
            fingerInfoDto = cacheService.queryFingerInfo(registerDto.getFingerMachine());
            // 缓存中数据仍然为空
            if ( null == fingerInfoDto){
                return Status.FINGERPRINT_ERROR;
            }
        }
        // 删除缓存
        cacheService.delFingerInfoCache(registerDto.getFingerMachine());
        UserPo userPo = UserPo.builder().idCard(registerDto.getIdCard()).build();
        // 复制指纹信息至PO
        BeanUtils.copyProperties(fingerInfoDto,userPo);
        // 加入缓存区
        cacheService.cacheUserInfo(userPo);
        return Status.FINGERPRINT_OK;
    }

    @Override
    public Status signature(RegisterDto registerDto) {

        // 判断前端参数的正确与否
        if ( VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCard())
                || VerifyUtil.isNull(registerDto.getSignatureMachine())){
            return Status.PARAM_ERROR;
        }
        //  判断签名仪的在线与否，并发送读取指令
        if ( ! TcpMsgSupervise.loadSignatureInformation(registerDto.getSignatureMachine())){
            return Status.SIGNATURE_ERROR;
        }

        // 睡眠等待结果返回
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Status.SIGNATURE_ERROR;
        }
        // 从缓存中读取签名信息
        SignatureInfoDto signatureInfoDto = cacheService.querySignatureInfo(registerDto.getSignatureMachine());
        // 缓存中数据为空
        if ( null == signatureInfoDto){

            // 继续休眠等待结果
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Status.SIGNATURE_ERROR;
            }
            // 从缓存中继续读取
            signatureInfoDto = cacheService.querySignatureInfo(registerDto.getSignatureMachine());
            // 二次读取仍然未读取
            if ( null == signatureInfoDto){
                return Status.SIGNATURE_ERROR;
            }
        }
        // 删除缓存
        cacheService.delSignatureInfoCache(registerDto.getSignatureMachine());
        // 创建po
        UserPo userPo = UserPo.builder()
                .idCard(registerDto.getIdCard())
                .registerDate(DateUtil.getCurrentTime())
                .build();
        BeanUtils.copyProperties(signatureInfoDto,userPo);
        // 缓存用户信息
        cacheService.cacheUserInfo(userPo);
        // 从缓存中获取用户的所有信息并写入mysql
        UserPo cacheUser = cacheService.queryUserInfo(registerDto.getIdCard());
        if ( cacheUser != null ){

            userMapper.insertUser(cacheUser);
            return Status.SIGNATURE_OK;
        }
        return Status.SIGNATURE_ERROR;
    }
}
