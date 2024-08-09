package com.langjie.langjiestreambackend.service.impl;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.service.TouristService;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:07
 **/

@Service
public class TouristServiceImpl implements TouristService {

    /**
    * @Author: ZooMEISTER
    * @Description: 游客Service测试方法
    * @DateTime: 2024/8/9 11:37
    * @Params: []
    * @Return java.lang.String
    */
    @Override
    public String TouristServiceTest() {
        return "Tourist Service Test Success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客注册方法
    * @DateTime: 2024/8/9 11:52
    * @Params: [user_name, user_password]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO TouristRegister(String user_name, String user_password) {
        return null;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客登陆方法
    * @DateTime: 2024/8/9 11:52
    * @Params: [user_name, user_password]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO TouristLogin(String user_name, String user_password) {
        return null;
    }
}
