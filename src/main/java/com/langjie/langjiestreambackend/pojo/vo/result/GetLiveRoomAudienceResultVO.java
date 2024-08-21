package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.UserVO;
import lombok.Data;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/20 14:56
 **/

@Data
public class GetLiveRoomAudienceResultVO extends ResultVO {
    private List<UserVO> userVOList;

    public GetLiveRoomAudienceResultVO(String resultType, int resultCode, String msg, List<UserVO> userVOList) {
        super(resultType, resultCode, msg);
        this.userVOList = userVOList;
    }
}
