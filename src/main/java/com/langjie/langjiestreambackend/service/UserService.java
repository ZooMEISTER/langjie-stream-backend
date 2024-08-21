package com.langjie.langjiestreambackend.service;

import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 15:26
 **/

@Service
public interface UserService {
    ResultVO UserGetUserInfo(String user_id);
}
