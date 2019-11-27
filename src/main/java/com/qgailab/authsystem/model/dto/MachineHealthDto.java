package com.qgailab.authsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description : 机器健康传输实体类
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MachineHealthDto implements Serializable {

    // 身份证读取仪ID
    private Integer idCardMachine;
    // 机器健康状态
    private Integer health;
}
