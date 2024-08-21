package com.langjie.langjiestreambackend.pojo.vo.result;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 13:26
 **/

@Data
public class LoginSuccessResultVO extends ResultVO {

    private String user_id;
    private String token;

    public LoginSuccessResultVO(String resultType, int resultCode, String msg, String user_id, String token) {
        super(resultType, resultCode, msg);
        this.user_id = user_id;
        this.token = token;
    }
}
