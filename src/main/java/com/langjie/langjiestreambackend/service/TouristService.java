package com.langjie.langjiestreambackend.service;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: 游客 Service
 * @DateTime 2024/8/9 11:06
 **/

@Service
public interface TouristService {
    String TouristServiceTest();
    ResultVO TouristRegister(String user_name, String user_password, String user_real_name, String user_organization);
    ResultVO TouristLogin(String user_name, String user_password);
}
