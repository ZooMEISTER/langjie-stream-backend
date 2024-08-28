package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import lombok.Data;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/26 13:21
 **/

@Data
public class ListResultVO extends ResultVO {
    private List list;

    public ListResultVO(String resultType, int resultCode, String msg, List list) {
        super(resultType, resultCode, msg);
        this.list = list;
    }
}
