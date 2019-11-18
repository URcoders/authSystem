package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.mapper.UserMapper;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.po.UserPo;
import com.qgailab.authsystem.model.vo.AuthVo;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.CacheService;
import com.qgailab.authsystem.service.LoginService;
import com.qgailab.authsystem.utils.TokenUtil;
import com.qgailab.authsystem.utils.VerifyUtil;
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
    private CacheService cacheService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public AuthVo login(String idCardMachine) {
        if (StringUtils.isEmpty(idCardMachine)){
            log.info("用户执行登录操作时未检测到身份证机器ID");
            return new AuthVo(Status.PARAM_ERROR);
        }
        // 向嵌入式发送信号，触发身份证记录仪获取身份证信息
        if (!TcpMsgSupervise.loadIdCardInformation(Integer.valueOf(idCardMachine))){
            log.info("嵌入式通道未打开");
            return new AuthVo(Status.BROKEN);
        }

        // 休眠一段时间，此时嵌入式返回的消息会做处理进行缓存区
        try {
            log.info("开始进行休眠");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.info("睡眠失败");
            e.printStackTrace();
            return new AuthVo(Status.GET_ID_CARD_ERROR);
        }
        // 休眠完毕，进入缓存区获取身份证信息。如获取不到则再次进入休眠
        IdCardInfoDto idCardInfoDto = cacheService.queryIdCardInfo(Integer.valueOf(idCardMachine));
        for (int i = 0; i < 2; i++){
            if (null == idCardInfoDto){
                log.info("获取不到身份证信息");
                if (i == 0){
                    try {
                        log.info("进入新的睡眠期，等待信息的获取");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        log.info("睡眠失败");
                        e.printStackTrace();
                    }
                    idCardInfoDto = cacheService.queryIdCardInfo(Integer.valueOf(idCardMachine));
                }else{
                    log.info("身份证信息获取失败");
                    return new AuthVo(Status.GET_ID_CARD_ERROR);
                }
            }
        }

        // 查询数据库
        UserPo userPo = userMapper.getUserByIdCard(idCardInfoDto.getIdCard());

        if (VerifyUtil.isNull(userPo)){
            log.info("该用户暂未注册");
            return new AuthVo(Status.UN_REGISTER);
        }

        // 根据身份证生成token
        String token = TokenUtil.createToken(idCardInfoDto.getIdCard());

        // 存储token
        cacheService.cacheToken(token, idCardInfoDto.getIdCard());

        return new AuthVo(Status.GET_ID_CARD_OK,null,  token);
    }
}
