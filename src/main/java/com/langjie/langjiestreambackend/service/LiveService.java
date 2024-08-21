package com.langjie.langjiestreambackend.service;

import com.langjie.langjiestreambackend.pojo.vo.LiveVO_SUB;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: 直播间 Service
 * @DateTime 2024/8/9 17:05
 **/

@Service
public interface LiveService {
    String LiveServiceTest();
    ResultVO UserAddNewLiveRoom(String live_name, String live_description, String live_password, boolean autoAllocateStreamPath, String live_push_path, String live_pull_path);
    List<LiveVO_SUB> UserGetLiveRoom();
    ResultVO UserRequestToEnterLiveRoom(String live_id, String password);
    ResultVO UserGetLiveDetail(String live_id);
    ResultVO UserGetLiveRoomAudience(String live_id);
    ResultVO UserGetAllMyLiveRoom();
}
