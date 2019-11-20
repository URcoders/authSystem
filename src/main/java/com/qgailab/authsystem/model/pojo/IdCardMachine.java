package com.qgailab.authsystem.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description : 身份证获取仪
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCardMachine implements Serializable {

    private Integer idCardMachine;
}
