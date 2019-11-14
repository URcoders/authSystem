package com.qgailab.authsystem.controller;


import com.qgailab.authsystem.model.dto.RegisterDto;
import com.qgailab.authsystem.model.vo.RegisterVo;
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
    public RegisterVo checkIdCard(@RequestBody RegisterDto registerDto){
        return new RegisterVo(registerService.checkIdCard(registerDto));
    }
}
