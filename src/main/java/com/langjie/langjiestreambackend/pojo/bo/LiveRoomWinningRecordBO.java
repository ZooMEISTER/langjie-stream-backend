package com.langjie.langjiestreambackend.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/27 13:06
 **/

@Data
@AllArgsConstructor
public class LiveRoomWinningRecordBO {
    private String winning_record_id;
    private String prize_id;
    private String user_id;
    private Date record_time;
    private String user_name;
    private String user_real_name;
    private String live_id;
    private String prize_pic;
    private String prize_name;
    private String prize_description;
    private String prize_level;
}
