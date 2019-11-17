package com.qgailab.authsystem.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.service.RegisterService;
import com.qgailab.authsystem.utils.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public Integer checkIdCard(RegisterDto registerDto) {

        System.out.println(registerDto);
        // 判断前端传送的身份证是否合法
        if ( VerifyUtil.isNull(registerDto.getIdCardMachine())){
            return Status.PARAM_ERROR.getStatus();
        }

        return Status.BROKEN.getStatus();

    }
}
