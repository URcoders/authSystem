package com.qgailab.authsystem.mapper;

import com.qgailab.authsystem.model.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description : user表对应的dao层
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-18
 */
@Mapper
public interface UserMapper {


    @Select("SELECT * FROM user WHERE id_card = #{idCard}")
    UserPo getUserByIdCard(String idCard);

    @Select("SELECT count(*) FROM user WHERE id_card = #{idCard}")
    Integer getCountByIdCard(String idCard);

}
