package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.po.LiveTypePO;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import lombok.Data;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/23 16:30
 **/

@Data
public class GetAllLiveTypeResultVO extends ResultVO {
    private List<LiveTypePO> liveTypePOList;

    public GetAllLiveTypeResultVO(String resultType, int resultCode, String msg, List<LiveTypePO> liveTypePOList) {
        super(resultType, resultCode, msg);
        this.liveTypePOList = liveTypePOList;
    }
}
