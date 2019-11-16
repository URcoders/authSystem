package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.vo.LoginVo;
import com.qgailab.authsystem.service.LoginService;
import com.qgailab.authsystem.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @ClassName LoginServiceImpl
 * @Description TODO
 * @Author huange7
 * @Date 2019-11-15 15:29
 * @Version 1.0
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginVo login(String idCardMachine) {
        if (StringUtils.isEmpty(idCardMachine)){
            log.info("用户执行登录操作时未检测到身份证机器ID");
            return LoginVo.fail(Status.GET_ID_CARD_ERROR);
        }

        String token = TokenUtil.createToken(idCardMachine);
        return LoginVo.success(Status.GET_ID_CARD_OK, token);
    }
}
