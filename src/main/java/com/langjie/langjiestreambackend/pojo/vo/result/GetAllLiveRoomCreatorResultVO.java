package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.UserVO;
import lombok.Data;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/22 10:26
 **/

@Data
public class GetAllLiveRoomCreatorResultVO extends ResultVO {
    private List<UserVO> allLiveRoomCreator;

    public GetAllLiveRoomCreatorResultVO(String resultType, int resultCode, String msg, List<UserVO> allLiveRoomCreator) {
        super(resultType, resultCode, msg);
        this.allLiveRoomCreator = allLiveRoomCreator;
    }
}
