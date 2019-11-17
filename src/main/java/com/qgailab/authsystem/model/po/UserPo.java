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

    private String idCard;
}
