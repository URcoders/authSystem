package com.qgailab.authsystem.service.impl;

import com.qgailab.authsystem.constance.MachineType;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.mapper.UserMapper;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.po.UserPo;
import com.qgailab.authsystem.model.vo.RegisterVo;
import com.qgailab.authsystem.net.supervise.TcpMsgSupervise;
import com.qgailab.authsystem.service.RegisterService;
import com.qgailab.authsystem.service.TcpCacheService;
import com.qgailab.authsystem.utils.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
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


    @Autowired
    private TcpCacheService tcpCacheService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer checkIdCard(RegisterDto registerDto) {


        // 判断前端传送的身份证是否合法
        // 判断参数的正确性
        if (VerifyUtil.isNull(registerDto)
                || VerifyUtil.isNull(registerDto.getIdCardMachine())){
            return Status.PARAM_ERROR.getStatus();
        }
        // 检测TCP客户端在线状态，并发送检查健康状态的信息至嵌入式
        if ( ! TcpMsgSupervise.checkMachineHealth(registerDto.getIdCardMachine(),
                MachineType.IdCardMachine)){
            return Status.BROKEN.getStatus();
        }

        try {
            // 等待TCP信息的回传
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Status.BROKEN.getStatus();
        }
        // 从缓存中读取
        Integer value = tcpCacheService.queryMachineHealth(MachineType.IdCardMachine, registerDto.getIdCardMachine());
        // 清除缓存
        tcpCacheService.delMachineHealth(MachineType.IdCardMachine,registerDto.getIdCardMachine());
        // 缓存中的数据为空  此时需要再等待
        if ( null == value){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Status.BROKEN.getStatus();
            }
            // 继续从缓存中读取
            value = tcpCacheService.queryMachineHealth(MachineType.IdCardMachine, registerDto.getIdCardMachine());
            // 清除缓存
            tcpCacheService.delMachineHealth(MachineType.IdCardMachine,registerDto.getIdCardMachine());
            // 嵌入式不在线,为机器不健康
            if ( null == value){
                return Status.BROKEN.getStatus();
            }

        }
        // 机器状态为正常,返回机器正常工作的结果
        if ( value == 1){
            return Status.HEALTH.getStatus();
        }
        return Status.BROKEN.getStatus();


    }


    @Override
    public RegisterVo loadIdCard(RegisterDto registerDto) {

        // 检查前端传输的参数
        if ( VerifyUtil.isNull(registerDto) || VerifyUtil.isNull(registerDto.getIdCardMachine())){
            return new RegisterVo(Status.PARAM_ERROR);
        }
        // 向嵌入式发送请求，并检验设备是否在线
        if ( ! TcpMsgSupervise.loadIdCardInfomation(registerDto.getIdCardMachine()) ){
            return new RegisterVo(Status.GET_ID_CARD_ERROR);
        }

        // 睡眠等待
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new RegisterVo(Status.GET_ID_CARD_ERROR);
        }

        // 从缓存中读取
        IdCardInfoDto idCardInfoDto = tcpCacheService.queryIdCardInfo(registerDto.getIdCardMachine());
        // 删除缓存
        tcpCacheService.delIdCardInfoCache(registerDto.getIdCardMachine());
        // 若缓存中无数据
        if ( null == idCardInfoDto){

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new RegisterVo(Status.GET_ID_CARD_ERROR);
            }
            // 第二次从缓存中读取
            idCardInfoDto = tcpCacheService.queryIdCardInfo(registerDto.getIdCardMachine());
            // 删除缓存
            tcpCacheService.delIdCardInfoCache(registerDto.getIdCardMachine());
            // 读取身份证信息失败
            if ( null == idCardInfoDto){
                return new RegisterVo(Status.GET_ID_CARD_ERROR);
            }
        }

        // 已注册
        if ( userMapper.getCountByIdCard(idCardInfoDto.getIdCard()) > 0){
            // todo 未做
        }

        UserPo userPo = new UserPo();
        // 复制数据至po中
        BeanUtils.copyProperties(idCardInfoDto,userPo);
        // 缓存
        tcpCacheService.cacheUserInfo(userPo);
        return new RegisterVo(Status.GET_ID_CARD_OK,registerDto.getIdCard());
    }
}
