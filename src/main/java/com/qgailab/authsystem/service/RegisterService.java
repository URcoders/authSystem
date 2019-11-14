package com.qgailab.authsystem.service;


import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.dto.RegisterDto;

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
    Integer checkIdCard(RegisterDto registerDto);

}
