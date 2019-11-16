package com.qgailab.authsystem.model.vo;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.qgailab.authsystem.constance.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName LoginVo
 * @Description TODO
 * @Author huange7
 * @Date 2019-11-14 10:52
 * @Version 1.0
 */

@Data
@Builder
public class LoginVo {

    private Status status;

    private String token;

    private LoginVo(){
        this.status = Status.GET_ID_CARD_ERROR;
        this.token = "";
    }

    private LoginVo(Status status, String token){
        this.status = status;
        this.token = token;
    }

    private LoginVo(Status status){
        this.status = status;
        this.token = "";
    }

    public static LoginVo fail(){
        return new LoginVo();
    }

    public static LoginVo fail(Status status){
        return new LoginVo(status);
    }

    public static LoginVo success(Status status, String token){
        return new LoginVo(status, token);
    }
}
