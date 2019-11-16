package com.qgailab.authsystem.service;

import com.qgailab.authsystem.model.vo.LoginVo;

/**
 * @ClassName LoginService
 * @Description 登录服务层
 * @Author huange7
 * @Date 2019-11-15 15:18
 * @Version 1.0
 */
public interface LoginService {

    LoginVo login(String idCardMachine);
}
