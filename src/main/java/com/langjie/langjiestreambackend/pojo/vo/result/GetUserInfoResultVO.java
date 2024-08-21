package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.UserVO;
import lombok.Data;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 15:32
 **/

@Data
public class GetUserInfoResultVO extends ResultVO {
    private UserVO userVO;

    public GetUserInfoResultVO(String resultType, int resultCode, String msg, UserVO userVO) {
        super(resultType, resultCode, msg);
        this.userVO = userVO;
    }
}
