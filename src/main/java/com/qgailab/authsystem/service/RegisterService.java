package com.qgailab.authsystem.service;


import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.vo.AuthVo;

/**
 * @Description : 认证系统注册的接口服务层
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-13
 */
public interface RegisterService {

    /**
     * @Description : 验证身份证硬件是否正常
     * @Param : [IdCardMachine]
     * @Return : java.lang.String  返回状态码
     * @Author : SheldonPeng
     * @Date : 2019-11-13
     */
    Status checkIdCard(RegisterDto registerDto);

    /**
     * @Description : 读取身份证信息
     * @Param : [registerDto]
     * @Return : java.lang.Integer
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    AuthVo loadIdCard(RegisterDto registerDto);

    /**
     * @Description : 录取指纹信息
     * @Param : [registerDto]
     * @Return : com.qgailab.authsystem.constance.Status
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    Status loadFinger(RegisterDto registerDto);

    /**
     * @Description : 录取签名信息
     * @Param : [registerDto]
     * @Return : com.qgailab.authsystem.constance.Status
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    Status signature(RegisterDto registerDto);
}
