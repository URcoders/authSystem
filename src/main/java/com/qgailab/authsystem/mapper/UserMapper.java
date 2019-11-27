package com.qgailab.authsystem.mapper;

import com.qgailab.authsystem.model.po.UserPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Insert("INSERT INTO user(id_card,name,sex,finger_info,signature,nation" +
            ",register_date,tel,address,get_card_date,faceID_info) " +
            "values(#{idCard},#{name},#{sex},#{fingerInfo},#{signature},#{nation}," +
            "#{registerDate},#{tel},#{address},#{getCardDate},#{faceIDInfo})")
    void insertUser(UserPo userPo);

}
