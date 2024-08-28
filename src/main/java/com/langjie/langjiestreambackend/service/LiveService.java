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
    ResultVO UserAddNewLiveRoom(String live_name, String live_description, String live_password, String live_type, boolean autoAllocateStreamPath, String live_push_path, String live_pull_path, String live_start_time, String live_end_time);
    ResultVO UserGetAllLiveRoom();
    ResultVO UserGetLiveRoom(List<String> nameArray, List<String> creatorArray);
    ResultVO UserRequestToEnterLiveRoom(String live_id, String password);
    ResultVO UserGetLiveDetail(String live_id);
    ResultVO UserGetLiveRoomAudience(String live_id);
    ResultVO UserGetAllLiveRoomCreator();
    ResultVO UserGetAllMyLiveRoom();
    ResultVO UserGetMySingleLiveRoomInfo(String live_id);
    ResultVO UserEditLiveRoomInfo(String live_id, String live_name, String live_description, String live_password, String live_push_path, String live_pull_path, String live_start_time, String live_end_time);
    ResultVO UserDeleteLiveRoom(String live_id);
    ResultVO UserGetAllLiveType();
    ResultVO UserGetAllPrizeType();
    ResultVO UserAddNewPrize(String live_id, String prize_pic, String prize_name, String prize_description, Integer prize_count, String prize_level);
    ResultVO UserGetAllLivePrize(String live_id);
    ResultVO UserDeletePrize(String prize_id);
    ResultVO DrawPrize(String prize_id);
    ResultVO UserGetPrizeWinner(String live_id);
}
