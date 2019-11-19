package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.model.dto.FingerInfoDto;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.dto.SignatureInfoDto;
import com.qgailab.authsystem.model.po.UserPo;
import com.qgailab.authsystem.service.CacheService;
import com.qgailab.authsystem.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description : Tcp缓存服务的实现类,所有属性未做判空，需要在调用此服务层时先行判空
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-17
 */
@Service
@Slf4j
public class CacheServiceImpl implements CacheService {


    @Cacheable(value = "tcp_temp" , key = "#machineType + ':' + #machineId")
    @Override
    public Integer cacheMachineHealth(MachineType machineType, Integer machineId, Integer value) {
        return value;
    }

    @CacheEvict(value = "tcp_temp" , key = "#machineType + ':' + #machineId")
    @Override
    public void delMachineHealth(MachineType machineType, Integer machineId) {

    }

    @Cacheable(value = "tcp_temp" , key = "#machineType + ':' + #machineId" , unless="#result == null")
    @Override
    public Integer queryMachineHealth(MachineType machineType, Integer machineId) {
        return null;
    }

    @Cacheable(value = "tcp_temp" , key = "'idCardInfo:' + #idCardInfoDto.idCardMachine")
    @Override
    public IdCardInfoDto cacheIdCardInfo(IdCardInfoDto idCardInfoDto) {
        return idCardInfoDto;
    }

    @CacheEvict(value = "tcp_temp" , key = "'idCardInfo:' + #machineId")
    @Override
    public void delIdCardInfoCache(Integer machineId) {

    }

    @Cacheable(value = "tcp_temp" , key = "'idCardInfo:' + #machineId" , unless="#result == null")
    @Override
    public IdCardInfoDto queryIdCardInfo(Integer machineId) {
        return null;
    }

    @Cacheable(value = "tcp_temp" , key = "'fingerInfo:' + #fingerInfoDto.fingerMachine")
    @Override
    public FingerInfoDto cacheFingerInfo(FingerInfoDto fingerInfoDto) {
        return fingerInfoDto;
    }

    @CacheEvict(value = "tcp_temp" , key = "'fingerInfo:' + #machineId")
    @Override
    public void delFingerInfoCache(Integer machineId) {

    }

    @Cacheable(value = "tcp_temp" , key = "'fingerInfo:' + #machineId" , unless="#result == null")
    @Override
    public FingerInfoDto queryFingerInfo(Integer machineId) {
        return null;
    }

    @Cacheable(value = "tcp_temp" , key = "'signatureInfo:' + #signatureInfoDto.signatureMachine")
    @Override
    public SignatureInfoDto cacheSignatureInfo(SignatureInfoDto signatureInfoDto) {
        return signatureInfoDto;
    }

    @CacheEvict(value = "tcp_temp" , key = "'signatureInfo:' + #machineId")
    @Override
    public void delSignatureInfoCache(Integer machineId) {

    }

    @Cacheable(value =  "tcp_temp" , key = "'signatureInfo:' + #machineId" , unless="#result == null")
    @Override
    public SignatureInfoDto querySignatureInfo(Integer machineId) {
        return null;
    }

    @CachePut(value = "register_temp_storage" , key = "'userInfo:' + #userPo.idCard")
    @Override
    public UserPo cacheUserInfo(UserPo userPo) {

        UserPo queryUser = queryUserInfo(userPo.getIdCard());
        if ( queryUser != null ){
            ObjectUtil.combineSydwCore(queryUser,userPo);
        }
        return userPo;
    }

    @CacheEvict(value = "register_temp_storage" , key = "'userInfo:' + #idCard")
    @Override
    public void delUserCache(String idCard) {

    }

    @Cacheable(value = "register_temp_storage" , key = "'userInfo:' + #idCard" , unless="#result == null")
    @Override
    public UserPo queryUserInfo(String idCard) {
        return null;
    }


    @Cacheable(value = "tokens" , key = "#token")
    @Override
    public String cacheToken(String token, String idCard) {
        return idCard;
    }

    @CachePut(value = "tokens", key = "#token" , unless="#result == null")
    @Override
    public String queryIdCard(String token ) {
        return null;
    }
}
