package com.langjie.langjiestreambackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:10
 **/

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    // 查看某一个 user_id 的用户数
    @Select("SELECT COUNT(*) FROM all_user WHERE user_id=#{user_id}")
    int getSpecificUserIdCount(String user_id);

    // 获取所有用户信息
    @Select("SELECT * FROM all_user")
    List<UserPO> getAllUserPO();

    // 根据 user_id 获取用户PO
    @Select("SELECT * FROM all_user WHERE user_id=#{user_id}")
    UserPO getUserPOByUserId(String user_id);

    // 获取某一用户名的用户数
    @Select("SELECT COUNT(*) FROM all_user WHERE user_name=#{user_name}")
    int checkIfUserNameNotAvailable(String user_name);

    // 插入新用户
    @Insert("INSERT INTO all_user(user_id, user_name, user_password, user_real_name, user_organization) VALUES(#{user_id}, #{user_name}, #{user_password}, #{user_real_name}, #{user_organization})")
    int insertNewUser(String user_id, String user_name, String user_password, String user_real_name, String user_organization);

    // 根据用户名获取用户实体
    @Select("SELECT * FROM all_user WHERE user_name=#{user_name}")
    UserPO getUserPOByUserName(String user_name);
}
