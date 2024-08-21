package com.langjie.langjiestreambackend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/12 15:05
 **/

@Data
@AllArgsConstructor
@TableName("all_live")
public class LivePO {
    @TableId(type = IdType.AUTO)
    private String live_id;
    private String live_name;
    private String live_description;
    private String live_password;
    private String live_creator;
    private String live_push_path;
    private String live_pull_path;
    private String live_create_time;
    private String deleted;
}
