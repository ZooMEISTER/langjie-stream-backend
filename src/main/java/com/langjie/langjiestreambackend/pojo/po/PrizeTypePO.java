package com.langjie.langjiestreambackend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/26 13:22
 **/

@Data
@AllArgsConstructor
@TableName("prize_type")
public class PrizeTypePO {
    @TableId(type = IdType.ASSIGN_UUID)
    private String prize_type_id;
    private String prize_type_code;
    private String prize_type_name;
}
