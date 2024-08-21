package com.langjie.langjiestreambackend.controller;

import com.langjie.langjiestreambackend.annotation.LogAnnotation;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 15:26
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户查询用户信息接口
    * @DateTime: 2024/8/21 15:29
    * @Params: [user_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/user-get-user-info")
    @LogAnnotation(description = "用户查询用户信息")
    public ResultVO UserGetUserInfo(@RequestParam("user_id") String user_id){
        return userService.UserGetUserInfo(user_id);
    }
}
