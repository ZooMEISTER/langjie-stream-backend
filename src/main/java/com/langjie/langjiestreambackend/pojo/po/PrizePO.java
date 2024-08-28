package com.langjie.langjiestreambackend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/26 14:26
 **/

@Data
@AllArgsConstructor
@TableName("all_prize")
public class PrizePO {
    @TableId(type = IdType.ASSIGN_UUID)
    private String prize_id;
    private String live_id;
    private String prize_pic;
    private String prize_name;
    private String prize_description;
    private String prize_count_total;
    private String prize_count_remain;
    private String prize_level;
    private Integer deleted;
}
