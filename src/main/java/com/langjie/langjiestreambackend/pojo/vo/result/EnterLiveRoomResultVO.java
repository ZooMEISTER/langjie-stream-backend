package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/16 12:59
 **/

@Data
public class EnterLiveRoomResultVO extends ResultVO {
    // 批准加入直播间后，会给该观众分发一个验证id
    private String liveRoomAudienceIdentification;

    public EnterLiveRoomResultVO(String resultType, int resultCode, String msg, String liveRoomAudienceIdentification) {
        super(resultType, resultCode, msg);
        this.liveRoomAudienceIdentification = liveRoomAudienceIdentification;
    }

    public EnterLiveRoomResultVO(String liveRoomAudienceIdentification) {
        this.liveRoomAudienceIdentification = liveRoomAudienceIdentification;
    }
}
