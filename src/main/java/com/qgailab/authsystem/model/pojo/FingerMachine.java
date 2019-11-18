package com.qgailab.authsystem.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $ 指纹仪的实体类
 * @Param: $
 * @return: $
 * @author: SheledonPeng
 * @Date: $
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FingerMachine implements Serializable {

    private Integer fingerMachine;
}
