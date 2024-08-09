package com.langjie.langjiestreambackend.controller;

import com.langjie.langjiestreambackend.annotation.LogAnnotation;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.service.TouristService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/9 11:05
 **/

@RestController
@RequestMapping("/tourist")
public class TouristController {
    private TouristService touristService;
    public TouristController(TouristService touristService){this.touristService = touristService;}

    /**
    * @Author: ZooMEISTER
    * @Description: 游客controller测试接口
    * @DateTime: 2024/8/9 11:47
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    @LogAnnotation(description = "游客Controller接口测试")
    public String TouristControllerTest(){
        return touristService.TouristServiceTest() + "\nTourist Controller Test Success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客注册接口
    * @DateTime: 2024/8/9 11:55
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/register")
    @LogAnnotation(description = "游客注册接口")
    public ResultVO TouristRegister(@RequestParam("user_name") String user_name,
                                    @RequestParam("user_password") String user_password){
        return touristService.TouristRegister(user_name, user_password);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客登陆接口
    * @DateTime: 2024/8/9 11:55
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/login")
    @LogAnnotation(description = "游客登录接口")
    public ResultVO TouristLogin(@RequestParam("user_name") String user_name,
                                 @RequestParam("user_password") String user_password){
        return touristService.TouristLogin(user_name, user_password);
    }
}
