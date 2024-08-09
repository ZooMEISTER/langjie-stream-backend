package com.langjie.langjiestreambackend.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:48
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private String resultType;
    private int resultCode;
    private String msg;
}
