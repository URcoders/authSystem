package com.qgailab.authsystem.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.vo.RegisterVo;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.RegisterService;
import com.qgailab.authsystem.utils.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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




    @Cacheable(value = "tcp_temp",key = "#registerDto")
    @Override
    public Integer checkIdCard(RegisterDto registerDto) {


//        // 判断前端传送的身份证是否合法
//        // 判断参数的正确性
//        if (VerifyUtil.isNull(registerDto)
//                || VerifyUtil.isNull(registerDto.getIdCardMachine())){
//            return Status.PARAM_ERROR.getStatus();
//        }
//        // 检测TCP客户端在线状态，并发送信息
//        if ( ! TcpMsgSupervise.checkMachineHealth(registerDto.getIdCardMachine(),
//                MachineType.IdCardMachine)){
//            return Status.BROKEN.getStatus();
//        }
//        // 等待TCP信息的回传
//        Thread.sleep(2000);
//
//        return Status.BROKEN.getStatus();
        return 1;

    }



}
