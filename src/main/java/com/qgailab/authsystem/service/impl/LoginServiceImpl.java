package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.po.UserPo;
import com.qgailab.authsystem.model.vo.LoginVo;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.LoginService;
import com.qgailab.authsystem.service.TcpCacheService;
import com.qgailab.authsystem.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TcpCacheService tcpCacheService;

    @Override
    public LoginVo login(String idCardMachine) {
        if (StringUtils.isEmpty(idCardMachine)){
            log.info("用户执行登录操作时未检测到身份证机器ID");
            return LoginVo.fail(Status.GET_ID_CARD_ERROR);
        }
        // 向嵌入式发送信号，触发身份证记录仪获取身份证信息
        TcpMsgSupervise.checkMachineHealth(Integer.valueOf(idCardMachine), MachineType.IdCardMachine);

        // 休眠一段时间，此时嵌入式返回的消息会做处理进行缓存区
        try {
            log.info("开始进行休眠");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.info("睡眠失败");
            e.printStackTrace();
            return LoginVo.fail(Status.GET_ID_CARD_ERROR);
        }
        // 休眠完毕，进入缓存区获取身份证信息。如获取不到则再次进入休眠
        IdCardInfoDto idCard = tcpCacheService.queryIdCardInfo(Integer.valueOf(idCardMachine));
        for (int i = 0; i < 2; i++){
            if (null == idCard){
                log.info("获取不到身份证信息");
                if (i == 0){
                    try {
                        log.info("进入新的睡眠期，等待信息的获取");
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        log.info("睡眠失败");
                        e.printStackTrace();
                    }
                    idCard = tcpCacheService.queryIdCardInfo(Integer.valueOf(idCardMachine));
                }else{
                    log.info("身份证信息获取失败");
                    return LoginVo.fail(Status.GET_ID_CARD_ERROR);
                }
            }
        }
        // 获取信息成功，通知嵌入式设备
        TcpMsgSupervise.loadIdInfomation(Integer.valueOf(idCardMachine));

        String token = TokenUtil.createToken("idCard");

        //todo 进行数据库的查询，查看当前idCard是否注册过。0
        // 加密token，而后存入缓存区
        tcpCacheService.cacheToken(token);
        return LoginVo.success(Status.GET_ID_CARD_OK, token);
    }
}
