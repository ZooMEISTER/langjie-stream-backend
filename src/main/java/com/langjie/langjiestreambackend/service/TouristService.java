package com.langjie.langjiestreambackend.service;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:06
 **/

@Service
public interface TouristService {
    String TouristServiceTest();
    ResultVO TouristRegister(String user_name, String user_password);
    ResultVO TouristLogin(String user_name, String user_password);
}
