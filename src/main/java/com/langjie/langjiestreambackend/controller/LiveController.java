package com.langjie.langjiestreambackend.controller;

import com.langjie.langjiestreambackend.annotation.LogAnnotation;
import com.langjie.langjiestreambackend.pojo.vo.LiveVO_SUB;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.service.LiveService;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: 直播间 Controller
 * @DateTime 2024/8/9 17:04
 **/

@RestController
@RequestMapping("/live")
public class LiveController {

    private LiveService liveService;
    public LiveController(LiveService liveService) { this.liveService = liveService; }

    /**
    * @Author: ZooMEISTER
    * @Description: 直播Controller测试接口
    * @DateTime: 2024/8/12 13:51
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    @LogAnnotation(description = "直播Controller接口测试")
    public String LiveControllerTest(){
        return liveService.LiveServiceTest() + "\nLive Controller Test Success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户添加新直播间接口
    * @DateTime: 2024/8/12 11:50
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/add-new-live-room")
    @LogAnnotation(description = "用户添加新直播间")
    public ResultVO UserAddNewLiveRoom(@RequestParam("live_name") String live_name,
                                       @RequestParam("live_description") String live_description,
                                       @RequestParam("live_password") String live_password,
                                       @RequestParam("autoAllocateStreamPath") boolean autoAllocateStreamPath,
                                       @RequestParam("live_push_path") String live_push_path,
                                       @RequestParam("live_pull_path") String live_pull_path){
        return liveService.UserAddNewLiveRoom(live_name, live_description, live_password, autoAllocateStreamPath, live_push_path, live_pull_path);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取直播间信息接口
    * @DateTime: 2024/8/16 9:48
    * @Params: []
    * @Return java.util.List<com.langjie.langjiestreambackend.pojo.vo.LiveVO>
    */
    @PostMapping("/get-live-room")
    @LogAnnotation(description = "用户获取直播间信息")
    public List<LiveVO_SUB> UserGetLiveRoom(){
        return liveService.UserGetLiveRoom();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户申请加入直播间接口
    * @DateTime: 2024/8/16 12:48
    * @Params: [live_id, password]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/request-to-enter-live-room")
    @LogAnnotation(description = "用户申请进入直播间")
    public ResultVO UserRequestToEnterLiveRoom(@RequestParam("live_id") String live_id,
                                               @RequestParam("password") String password){
        return liveService.UserRequestToEnterLiveRoom(live_id, password);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取直播间详细信息接口
    * @DateTime: 2024/8/16 16:51
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/get-live-detail")
    @LogAnnotation(description = "用户获取直播间的详细信息")
    public ResultVO UserGetLiveDetail(@RequestParam("live_id") String live_id){
        return liveService.UserGetLiveDetail(live_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取直播间观众列表接口
    * @DateTime: 2024/8/20 14:21
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/get-audience-list")
    @LogAnnotation(description = "用户获取某个直播间的当前观众列表")
    public ResultVO UserGetLiveRoomAudience(@RequestParam("live_id") String live_id){
        return liveService.UserGetLiveRoomAudience(live_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有自己创建的直播间接口
    * @DateTime: 2024/8/21 16:54
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/get-my-live-room")
    @LogAnnotation(description = "用户获取自己创建的直播间信息")
    public ResultVO UserGetAllMyLiveRoom(){
        // 什么，你问我为什么不用直播间的筛选接口来做这件事？我也不知道
        return liveService.UserGetAllMyLiveRoom();
    }
}
