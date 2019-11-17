package com.qgailab.authsystem.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.vo.LoginVo;
import com.qgailab.authsystem.model.vo.RegisterVo;
import com.qgailab.authsystem.service.RegisterService;
import com.qgailab.authsystem.utils.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/checkIdCard")
    public RegisterVo checkIdCard(@RequestBody RegisterDto registerDto){
        return new RegisterVo(registerService.checkIdCard(registerDto));
    }

    @PostMapping("/login")
    public LoginVo login(@RequestBody Map<String, Object> map) {
        String idCardMachine = (String) map.get("idCardMachine");
        return LoginVo.fail();
    }

    @PostMapping("/getQRCode")
    public String getQRCode(@RequestBody Map<String, Object> map){
        String token = (String) map.get("token");
        return null;
    }
}
