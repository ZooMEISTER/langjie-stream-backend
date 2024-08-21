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
 * @DateTime 2024/8/21 16:23
 **/

@Data
@AllArgsConstructor
@TableName("all_message")
public class MessagePO {
    @TableId(type = IdType.AUTO)
    private String msg_id;
    private String msg_live_id;
    private String msg_sender_id;
    private String msg_type;
    private Date msg_send_time;
}
