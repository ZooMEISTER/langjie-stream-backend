package com.langjie.langjiestreambackend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/23 16:26
 **/

@Data
@AllArgsConstructor
@TableName("live_type")
public class LiveTypePO {
    @TableId(type = IdType.ASSIGN_UUID)
    private String live_type_id;
    private String live_type_code;
    private String live_type_name;
}
