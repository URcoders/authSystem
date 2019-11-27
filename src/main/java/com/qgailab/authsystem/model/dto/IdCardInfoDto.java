package com.qgailab.authsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 用户身份证信息的存储传输实体类
 * @Param: $
 * @return: $
 * @author: SheledonPeng
 * @Date: $
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdCardInfoDto implements Serializable {

    // 身份证信息获取仪的Id
    private Integer idCardMachine;

    // 身份证号
    private String idCard;

    // 姓名
    private String name;

    // 性别
    private String sex;

    // 民族
    private String nation;

    // 电话号码
    private String tel;

    // 位置信息
    private String address;

    // 身份证办理日期
    private String getCardDate;

}
