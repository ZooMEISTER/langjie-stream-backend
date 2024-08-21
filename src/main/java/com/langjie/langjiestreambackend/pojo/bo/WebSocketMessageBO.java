package com.langjie.langjiestreambackend.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: WebSocket连接发送的消息对象
 * @DateTime 2024/8/20 12:04
 **/

@Data
@AllArgsConstructor
public class WebSocketMessageBO {
    private String msgId;
    private String senderId;
    private String senderName;
    private String msgType;
    private String msg;
    private String sendTime;
}
