package com.qgailab.authsystem.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qgailab.authsystem.constance.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description : 认证系统注册接口返回至前端的实体类对象
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterVo implements Serializable {

    // 返回至前端的状态码
    private Integer status;
    // 身份号
    private String idCard;

    public RegisterVo(Integer status){
        this.status = status;
    }

    public RegisterVo(Status status){
        this.status = status.getStatus();
    }

    public RegisterVo(Status status , String idCard){
        this.status = status.getStatus();
        this.idCard = idCard;
    }
}
