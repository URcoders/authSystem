package com.qgailab.authsystem.service;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.model.dto.FingerInfoDto;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.dto.SignatureInfoDto;
import com.qgailab.authsystem.model.po.UserPo;

/**
 * @Description : Tcp信息缓存区的业务层
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-17
 */
public interface CacheService {

    /**
     * @Description : 存储机器健康信息的缓存
     * @Param : [machineType, machineId, value]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    Integer cacheMachineHealth(MachineType machineType , Integer machineId , Integer value);

    /**
     * @Description : 删除机器健康的缓存信息
     * @Param : [machineType, machineId]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    void delMachineHealth(MachineType machineType , Integer machineId);

    /**
     * @Description : 查找机器健康状态的缓存
     * @Param : [machineType, machineId]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    Integer queryMachineHealth(MachineType machineType , Integer machineId);
    /**
     * @Description : 缓存嵌入式发送的身份证信息
     * @Param : [machineId, idCardInfoDto]
     * @Return : com.qgailab.authsystem.model.dto.IdCardInfoDto
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    IdCardInfoDto cacheIdCardInfo(Integer machineId , IdCardInfoDto idCardInfoDto);

    /**
     * @Description : 删除嵌入式发送的身份证信息的缓存
     * @Param : [machineId]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    void delIdCardInfoCache(Integer machineId);

    /**
     * @Description : 查找用户信息的缓存
     * @Param : [machineId]
     * @Return : com.qgailab.authsystem.model.dto.IdCardInfoDto
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    IdCardInfoDto queryIdCardInfo(Integer machineId);
    /**
     * @Description : 缓存嵌入式发送的指纹信息的缓存
     * @Param : [machineId, fingerInfoDto]
     * @Return : com.qgailab.authsystem.model.dto.FingerInfoDto
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    FingerInfoDto cacheFingerInfo(Integer machineId, FingerInfoDto fingerInfoDto);

    /**
     * @Description : 删除嵌入式发送的指纹信息的缓存
     * @Param : [machineId]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    void delFingerInfoCache(Integer machineId);

    /**
     * @Description : 查找指纹信息的缓存
     * @Param : [machineId]
     * @Return : com.qgailab.authsystem.model.dto.FingerInfoDto
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    FingerInfoDto queryFingerInfo(Integer machineId);
    /**
     * @Description : 缓存嵌入式发送的签名信息
     * @Param : [machineId, signatureInfoDto]
     * @Return : com.qgailab.authsystem.model.dto.SignatureInfoDto
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    SignatureInfoDto cacheSignatureInfo(Integer machineId, SignatureInfoDto signatureInfoDto);

    /**
     * @Description : 删除缓存的签名信息的缓存
     * @Param : [machineType, machineId]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    void delSignatureInfoCache(Integer machineId);

    /**
     * @Description : 查找指纹信息的缓存
     * @Param : [machineId]
     * @Return : com.qgailab.authsystem.model.dto.SignatureInfoDto
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    SignatureInfoDto querySignatureInfo(Integer machineId);

    /**
     * @Description : 缓存注册用户的信息
     * @Param : [userPo]
     * @Return : com.qgailab.authsystem.model.po.UserPo
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    UserPo cacheUserInfo(UserPo userPo);

    /**
     * @Description : 删除注册用户的缓存
     * @Param : [idCard]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    void delUserCache(String idCard);

    /**
     * @Description : 查找用户信息的缓存
     * @Param : [idCard]
     * @Return : com.qgailab.authsystem.model.po.UserPo
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    UserPo queryUserInfo(String idCard);

    /**
     * @Description : 生成token
     * @Param : [token,idCard]
     * @Return : void
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    String cacheToken(String token , String idCard);
}
