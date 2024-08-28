package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.LiveVO_SUB;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import lombok.Data;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/22 11:04
 **/

@Data
public class GetAllLiveRoomResultVO extends ResultVO {
    private List<LiveVO_SUB> liveVO_subList;

    public GetAllLiveRoomResultVO(String resultType, int resultCode, String msg, List<LiveVO_SUB> liveVO_subList) {
        super(resultType, resultCode, msg);
        this.liveVO_subList = liveVO_subList;
    }
}
