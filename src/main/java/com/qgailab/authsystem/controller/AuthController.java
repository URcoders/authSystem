package com.qgailab.authsystem.controller;


import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.vo.AuthVo;
import com.qgailab.authsystem.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : 认证系统控制层
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-13
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RegisterService registerService;

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
}
