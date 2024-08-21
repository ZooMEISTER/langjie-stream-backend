package com.langjie.langjiestreambackend.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/20 14:53
 **/

@Data
@AllArgsConstructor
public class UserVO {
    private String user_id;
    private String user_name;
    private int user_permission_level;
}
