package com.langjie.langjiestreambackend.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/16 9:45
 **/

@Data
@AllArgsConstructor
public class LiveVO_SUB {
    private String key;
    private String live_id;
    private String live_name;
    private String live_description;
    private String live_creator;
    private String live_creator_name;
    private String live_type;
    private Boolean hasPassword;
    private String live_start_time;
    private String live_end_time;
    private String live_create_time;
}
