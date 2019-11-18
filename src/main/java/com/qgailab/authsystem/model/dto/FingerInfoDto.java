package com.qgailab.authsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 用户指纹信息的存储传输实体类$
 * @Param: $
 * @return: $
 * @author: SheledonPeng
 * @Date: $
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FingerInfoDto implements Serializable {

    // 指纹信息
    private String fingerInfo;
    // 机器ID
    private Integer fingerMachine;

}
