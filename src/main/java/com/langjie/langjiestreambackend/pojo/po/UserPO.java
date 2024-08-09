package com.langjie.langjiestreambackend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:10
 **/

@Data
@TableName("all_user")
public class UserPO {
    @TableId(type = IdType.AUTO)
    private int user_id;
    private String user_name;
    private String user_password;
}
