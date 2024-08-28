package com.langjie.langjiestreambackend.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/16 16:44
 **/

@Data
@AllArgsConstructor
public class LiveVO_FULL {
    private String live_id;
    private String live_name;
    private String live_description;
    private String live_creator;
    private String live_type;
    private String live_password;
    private String live_push_path;
    private String live_pull_path;
    private String live_start_time;
    private String live_end_time;
    private String live_create_time;
}
