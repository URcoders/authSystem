package com.qgailab.authsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:  认证系统注册接口数据接收对象
 * @Param: $
 * @return: $
 * @author: SheledonPeng
 * @Date: $
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDto implements Serializable {

    // 身份证验证仪的编号ID
    private Integer idCardMachine;
}
