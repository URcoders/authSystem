package com.qgailab.authsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description : 人脸信息的传输类
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaceIDInfoDto implements Serializable {

    // 指纹信息
    private String faceIDInfo;
    // 机器ID
    private Integer faceIDMachine;
}
