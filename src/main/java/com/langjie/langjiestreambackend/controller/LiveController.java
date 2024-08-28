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
                                       @RequestParam("live_type") String live_type,
                                       @RequestParam("autoAllocateStreamPath") boolean autoAllocateStreamPath,
                                       @RequestParam("live_push_path") String live_push_path,
                                       @RequestParam("live_pull_path") String live_pull_path,
                                       @RequestParam("live_start_time") String live_start_time,
                                       @RequestParam("live_end_time") String live_end_time){
        return liveService.UserAddNewLiveRoom(live_name, live_description, live_password, live_type, autoAllocateStreamPath, live_push_path, live_pull_path, live_start_time, live_end_time);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有直播间接口
    * @DateTime: 2024/8/22 13:29
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @GetMapping("/get-all-live-room")
    @LogAnnotation(description = "用户获取所有直播间")
    public ResultVO UserGetAllLiveRoom(){
        return liveService.UserGetAllLiveRoom();
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
    public ResultVO UserGetLiveRoom(@RequestParam(required = false, name = "nameArray") List<String> nameArray,
                                    @RequestParam(required = false, name = "creatorArray") List<String> creatorArray){
        return liveService.UserGetLiveRoom(nameArray, creatorArray);
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
    * @Description: 用户获取所有直播间创建者接口
    * @DateTime: 2024/8/22 10:21
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @GetMapping("/get-all-live-room-creator")
    @LogAnnotation(description = "用户获取所有直播间的创建者")
    public ResultVO UserGetAllLiveRoomCreator(){
        return liveService.UserGetAllLiveRoomCreator();
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

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取自己的单个直播间详细信息接口
    * @DateTime: 2024/8/22 16:04
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/get-my-single-live-room-info")
    @LogAnnotation(description = "用户在编辑自己的直播间时请求直播间的信息")
    public ResultVO UserGetMySingleLiveRoomInfo(@RequestParam("live_id") String live_id){
        return liveService.UserGetMySingleLiveRoomInfo(live_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户编辑自己的直播间信息接口
    * @DateTime: 2024/8/22 16:29
    * @Params: [live_id, live_name, live_description, live_password, live_push_path, live_pull_path]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/edit-live-room-info")
    @LogAnnotation(description = "用户编辑自己的直播间的信息")
    public ResultVO UserEditLiveRoomInfo(@RequestParam("live_id") String live_id,
                                         @RequestParam("live_name") String live_name,
                                         @RequestParam("live_description") String live_description,
                                         @RequestParam("live_password") String live_password,
                                         @RequestParam("live_push_path") String live_push_path,
                                         @RequestParam("live_pull_path") String live_pull_path,
                                         @RequestParam("live_start_time") String live_start_time,
                                         @RequestParam("live_end_time") String live_end_time){
        return liveService.UserEditLiveRoomInfo(live_id, live_name, live_description, live_password, live_push_path, live_pull_path, live_start_time, live_end_time);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户删除自己的直播间接口
    * @DateTime: 2024/8/23 9:33
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/delete-live-room")
    @LogAnnotation(description = "用户删除直播间")
    public ResultVO UserDeleteLiveRoom(@RequestParam("live_id") String live_id){
        return liveService.UserDeleteLiveRoom(live_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有直播间接口
    * @DateTime: 2024/8/23 16:25
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @GetMapping("/get-live-type")
    @LogAnnotation(description = "用户获取所有直播间的类型")
    public ResultVO UserGetAllLiveType(){
        return liveService.UserGetAllLiveType();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有奖品类型接口
    * @DateTime: 2024/8/26 13:20
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @GetMapping("/get-prize-type")
    @LogAnnotation(description = "用户获取所有奖品类型")
    public ResultVO UserGetAllPrizeType(){
        return liveService.UserGetAllPrizeType();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户添加新奖品接口
    * @DateTime: 2024/8/26 13:46
    * @Params: [prize_name, prize_description, prize_count, prize_level]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/add-new-prize")
    @LogAnnotation(description = "用户添加新奖品")
    public ResultVO UserAddNewPrize(@RequestParam("live_id") String live_id,
                                    @RequestParam("prize_pic") String prize_pic,
                                    @RequestParam("prize_name") String prize_name,
                                    @RequestParam("prize_description") String prize_description,
                                    @RequestParam("prize_count") Integer prize_count,
                                    @RequestParam("prize_level") String prize_level){
        return liveService.UserAddNewPrize(live_id, prize_pic, prize_name, prize_description, prize_count, prize_level);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有该直播间的奖品接口
    * @DateTime: 2024/8/26 14:23
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/get-all-live-prize")
    @LogAnnotation(description = "用户获取所有奖品")
    public ResultVO UserGetAllLivePrize(@RequestParam("live_id") String live_id){
        return liveService.UserGetAllLivePrize(live_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户删除奖品接口
    * @DateTime: 2024/8/26 15:18
    * @Params: [prize_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/delete-prize")
    @LogAnnotation(description = "用户删除奖品")
    public ResultVO UserDeletePrize(@RequestParam("prize_id") String prize_id){
        return liveService.UserDeletePrize(prize_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户抽奖接口
    * @DateTime: 2024/8/26 15:47
    * @Params: [prize_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/draw")
    @LogAnnotation(description = "用户抽奖")
    public ResultVO DrawPrize(@RequestParam("prize_id") String prize_id){
        return liveService.DrawPrize(prize_id);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取某直播间的获奖记录接口
    * @DateTime: 2024/8/27 11:38
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @PostMapping("/get-prize-winner")
    @LogAnnotation(description = "用户获取某个直播间所有获奖记录")
    public ResultVO UserGetPrizeWinner(@RequestParam("live_id") String live_id){
        return liveService.UserGetPrizeWinner(live_id);
    }
}
