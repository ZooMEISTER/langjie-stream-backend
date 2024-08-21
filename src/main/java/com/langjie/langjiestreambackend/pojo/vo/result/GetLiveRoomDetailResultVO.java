package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.LiveVO_FULL;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/16 16:48
 **/

@Data
public class GetLiveRoomDetailResultVO extends ResultVO {
    private LiveVO_FULL liveVO_full;

    public GetLiveRoomDetailResultVO(String resultType, int resultCode, String msg, LiveVO_FULL liveVO_full) {
        super(resultType, resultCode, msg);
        this.liveVO_full = liveVO_full;
    }
}
