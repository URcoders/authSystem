package com.qgailab.authsystem.controller;


import com.alibaba.druid.util.StringUtils;
import com.qgailab.authsystem.constance.Status;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.vo.AuthVo;
import com.qgailab.authsystem.service.LoginService;
import com.qgailab.authsystem.service.QRCodeService;
import com.qgailab.authsystem.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @Description : 认证系统控制层
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-13
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private QRCodeService qrCodeService;

    @PostMapping("/checkIdCard")
    public AuthVo checkIdCard(@RequestBody RegisterDto registerDto){
        return new AuthVo(registerService.checkIdCard(registerDto));
    }

    @PostMapping("/loadIdCard")
    public AuthVo loadIdCard(@RequestBody RegisterDto registerDto){
        return registerService.loadIdCard(registerDto);
    }

    @PostMapping("/loadFinger")
    public AuthVo loadFinger(@RequestBody RegisterDto registerDto){
        return new AuthVo(registerService.loadFinger(registerDto));
    }

    @PostMapping("/signature")
    public AuthVo signature(@RequestBody RegisterDto registerDto){
        return new AuthVo(registerService.signature(registerDto));
    }

    @PostMapping("/login")
    public AuthVo login(@RequestBody Map<String, Object> map) {
        String idCardMachine = (String) map.get("idCardMachine");
        return loginService.login(idCardMachine);
    }

    @PostMapping("/getQRCode")
    public String getQRCode(@RequestBody Map<String, Object> map) throws IOException {
        String token = (String) map.get("token");
        return qrCodeService.getQRCode(token);
    }
}
