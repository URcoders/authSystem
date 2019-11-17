package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.vo.LoginVo;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.LoginService;
import com.qgailab.authsystem.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @ClassName LoginServiceImpl
 * @Description 登录业务层
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
        // 向嵌入式发送信号，触发身份证记录仪获取身份证信息
        TcpMsgSupervise.checkMachineHealth(Integer.valueOf(idCardMachine), MachineType.IdCardMachine);

        // 休眠一段时间，此时嵌入式返回的消息会做处理进行缓存区
        //todo 休眠代码
        System.out.println("我正在休眠");
        // 休眠完毕，进入缓存区获取身份证信息。如获取不到则再次进入休眠
        String idCard = "身份证";
        for (int i = 0; i < 2; i++){
            if (StringUtils.isEmpty(idCard)){
                log.info("获取不到身份证信息");
                if (i == 0){
                    //todo 进入第二次休眠
                    System.out.println("我正在睡觉");
                    idCard = "新的身份证";
                }else{
                    log.info("身份证信息获取失败");
                    return LoginVo.fail(Status.GET_ID_CARD_ERROR);
                }
            }
        }
        String token = TokenUtil.createToken(idCard);
        return LoginVo.success(Status.GET_ID_CARD_OK, token);
    }
}
