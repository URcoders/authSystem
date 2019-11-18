package com.qgailab.authsystem.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description : 用户信息的实体类
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPo implements Serializable {

    // 身份证号
    private String idCard;

    // 姓名
    private String name;

    // 性别
    private String sex;

    // 指纹信息
    private String fingerInfo;

    // 签名信息
    private String signature;

    // 民族
    private String nation;

    // 注册时间
    private String registerDate;

    // 电话号码
    private String tel;

    // 位置信息
    private String address;

    // 身份证办理日期
    private String getCardDate;
}
