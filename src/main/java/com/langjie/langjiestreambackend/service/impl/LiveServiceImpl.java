package com.langjie.langjiestreambackend.service.impl;

import com.langjie.langjiestreambackend.constant.ResultType;
import com.langjie.langjiestreambackend.constant.WebSocketMessageType;
import com.langjie.langjiestreambackend.exception.code.LiveResultCode;
import com.langjie.langjiestreambackend.interceptor.UserRequestInterceptor;
import com.langjie.langjiestreambackend.mapper.LiveMapper;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.bo.LiveRoomWinningRecordBO;
import com.langjie.langjiestreambackend.pojo.po.*;
import com.langjie.langjiestreambackend.pojo.vo.LiveVO_FULL;
import com.langjie.langjiestreambackend.pojo.vo.LiveVO_SUB;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.UserVO;
import com.langjie.langjiestreambackend.pojo.vo.result.*;
import com.langjie.langjiestreambackend.service.LiveService;
import com.langjie.langjiestreambackend.service.WebSocket;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author ZooMEISTER
 * @Description: 直播间 Service Impl
 * @DateTime 2024/8/9 17:05
 **/

@Service
public class LiveServiceImpl implements LiveService {

    private Environment environment;
    private RedisTemplate redisTemplate;
    private LiveMapper liveMapper;
    private UserMapper userMapper;
    private WebSocket webSocket;

    public LiveServiceImpl(Environment environment, RedisTemplate redisTemplate, LiveMapper liveMapper, UserMapper userMapper, WebSocket webSocket) {
        this.environment = environment;
        this.redisTemplate = redisTemplate;
        this.liveMapper = liveMapper;
        this.userMapper = userMapper;
        this.webSocket = webSocket;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客Service测试方法
    * @DateTime: 2024/8/12 13:53
    * @Params: []
    * @Return java.lang.String
    */
    @Override
    public String LiveServiceTest() {
        // ThreadLocal中拿到本次请求的user_id
        return UserRequestInterceptor.getUserIDFromInterceptor() + "\nLive Service Test Success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客添加新直播间方法，当live_pull_path为 #AUTOMATIC_ALLOCATE# 时，服务器自动分配推流以及拉流地址
    * @DateTime: 2024/8/12 14:59
    * @Params: [live_name, live_description, live_password, live_pull_path]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    @Transactional
    public ResultVO UserAddNewLiveRoom(String live_name, String live_description, String live_password, String live_type, boolean autoAllocateStreamPath, String live_push_path, String live_pull_path, String live_start_time, String live_end_time) {
        try{
            int isLiveNameNotAvailable = liveMapper.checkIfLiveNameNotAvailable(live_name);
            if(isLiveNameNotAvailable > 0){
                // 该直播间名不可用
                return new ResultVO(ResultType.ERROR, LiveResultCode.ADD_NEW_LIVE_ROOM_FAIL_NAME_ALREADY_EXIST, "直播间添加失败: 直播间名已存在");
            }
            else{
                // 该直播间名可用
                if(autoAllocateStreamPath){
                    // 自动分配推拉流路径，其实就是分配串流密钥
                    String streamKey = UUID.randomUUID().toString().replace("-", "");
                    live_push_path = "rtmp://" +
                            environment.getProperty("stream.server.ip") +
                            ":" + environment.getProperty("stream.server.port.push") +
                            "/" + environment.getProperty("stream.server.rtmp.application") +
                            "/" + streamKey;
                    live_pull_path = "http://" +
                            environment.getProperty("stream.server.ip") +
                            ":" + environment.getProperty("stream.server.port.pull") +
                            "/flv?port=" + environment.getProperty("stream.server.port.push") +
                            "&app=" + environment.getProperty("stream.server.rtmp.application") +
                            "&stream=" + streamKey;
                }

                // 直播间id
                String live_id = UUID.randomUUID().toString().replace("-", "");

                // 添加新的直播间到数据库
                int res = liveMapper.insertNewLiveRoom(
                        live_id,
                        live_name,
                        live_description,
                        live_password,
                        UserRequestInterceptor.getUserIDFromInterceptor(),
                        live_type,
                        live_push_path,
                        live_pull_path,
                        new Date(Long.parseLong(live_start_time)),
                        new Date(Long.parseLong(live_end_time))
                );

                if(res > 0){
                    return new ResultVO(ResultType.SUCCESS, LiveResultCode.ADD_NEW_LIVE_ROOM_SUCCESS, "添加直播间成功");
                }
                else{
                    return new ResultVO(ResultType.ERROR, LiveResultCode.ADD_NEW_LIVE_ROOM_FAIL_OTHER_ERROR, "添加新直播间失败");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有直播间信息方法
    * @DateTime: 2024/8/22 13:29
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllLiveRoom() {
        try{
            List<LiveVO_SUB> liveVO_subList = new ArrayList<>();
            List<LivePO> livePOList = liveMapper.getAllLiveRoom();
            for(LivePO livePO : livePOList){
                UserPO userPO = userMapper.getUserPOByUserId(livePO.getLive_creator());
                liveVO_subList.add(new LiveVO_SUB(
                        livePO.getLive_id(),
                        livePO.getLive_id(),
                        livePO.getLive_name(),
                        livePO.getLive_description(),
                        livePO.getLive_creator(),
                        userPO.getUser_name(),
                        livePO.getLive_type(),
                        !livePO.getLive_password().equals(""),
                        String.valueOf(livePO.getLive_start_time().getTime()),
                        String.valueOf(livePO.getLive_end_time().getTime()),
                        String.valueOf(livePO.getLive_create_time().getTime())
                ));
            }
            return new GetAllLiveRoomResultVO(ResultType.SUCCESS, LiveResultCode.GET_ALL_LIVE_ROOM_SUCCESS, "获取所有直播间信息成功", liveVO_subList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_ALL_LIVE_ROOM_FAIL, "获取所有直播间信息失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 瀛湖获取直播间信息方法
    * @DateTime: 2024/8/16 9:47
    * @Params: []
    * @Return java.util.List<com.langjie.langjiestreambackend.pojo.vo.LiveVO>
    */
    @Override
    public ResultVO UserGetLiveRoom(List<String> nameArray, List<String> creatorArray) {
        try{
            System.out.println(creatorArray);
            List<LiveVO_SUB> liveVOSUBList = new ArrayList<>();
            // 这里组装查询的sql语句的条件
            String sqlString = "";
            if(nameArray != null){
                sqlString += " live_id IN " + nameArray.toString().replace("[", "('").replace(", ", "', '").replace("]", "')") + " AND ";
            }
            if(creatorArray != null){
                sqlString += " live_creator IN " + creatorArray.toString().replace("[", "('").replace(", ", "', '").replace("]", "')") + " AND ";
            }

            List<LivePO> livePOList = liveMapper.getLiveRoom(sqlString);

            for(LivePO livePO : livePOList){
                UserPO userPO = userMapper.getUserPOByUserId(livePO.getLive_creator());
                liveVOSUBList.add(new LiveVO_SUB(
                        livePO.getLive_id(),
                        livePO.getLive_id(),
                        livePO.getLive_name(),
                        livePO.getLive_description(),
                        livePO.getLive_creator(),
                        userPO.getUser_name(),
                        livePO.getLive_type(),
                        !livePO.getLive_password().equals(""),
                        String.valueOf(livePO.getLive_start_time().getTime()),
                        String.valueOf(livePO.getLive_end_time().getTime()),
                        String.valueOf(livePO.getLive_create_time().getTime())
                ));
            }
            return new GetAllLiveRoomResultVO(ResultType.SUCCESS, LiveResultCode.GET_ALL_LIVE_ROOM_SUCCESS, "获取直播间信息成功", liveVOSUBList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_ALL_LIVE_ROOM_FAIL, "获取所有直播间信息失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户申请加入直播间方法
    * @DateTime: 2024/8/16 12:48
    * @Params: [live_id, password]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserRequestToEnterLiveRoom(String live_id, String password) {
        try{
            LivePO livePO = liveMapper.getLivePOById(live_id);
            Boolean isPasswordRight = livePO.getLive_password().equals(password);
            // 密码正确 或 请求用户为该直播间创建者
            if(isPasswordRight || UserRequestInterceptor.getUserIDFromInterceptor().equals(livePO.getLive_creator())){
                // 用户试图加入直播间
                // 把用户信息和直播间信息存放到Redis中，并设定为ON，作为认证信息
                redisTemplate.opsForValue().set(environment.getProperty("stream.server.redis.user-enter-live-room-status") + live_id + ":" + UserRequestInterceptor.getUserIDFromInterceptor(), "ON");

                // 生成identification，好吧，暂时这玩意没用，懒得删了，反正不影响
                String identification = UUID.randomUUID().toString().replace("-", "");

                return new EnterLiveRoomResultVO(ResultType.SUCCESS, LiveResultCode.ENTER_LIVE_ROOM_REQUEST_APPROVE, "允许加入直播间", identification);
            }
            else{
                // 密码错误
                return new ResultVO(ResultType.ERROR, LiveResultCode.ENTER_LIVE_ROOM_REQUEST_REJECT_WRONG_PASSWORD, "加入直播间错误，密码错误");
            }
        }
        catch (Exception e){
            // 其他错误
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.ENTER_LIVE_ROOM_REQUEST_REJECT_OTHER_REASON, "加入直播间错误，其他原因: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取直播间详细信息方法
    * @DateTime: 2024/8/16 16:51
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetLiveDetail(String live_id) {
        try{
            // 先去Redis里看看有没有认证信息
            if(redisTemplate.opsForValue().get(environment.getProperty("stream.server.redis.user-enter-live-room-status") + live_id + ":" + UserRequestInterceptor.getUserIDFromInterceptor()) == null){
                // Redis中不存在认证信息
                return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_INFO_FAIL_NO_AUTH, "获取直播间信息失败: 认证信息不存在");
            }
            else{
                // redis中存在认证信息
                // 认证信息的value
                String idString = redisTemplate.opsForValue().get(environment.getProperty("stream.server.redis.user-enter-live-room-status") + live_id + ":" + UserRequestInterceptor.getUserIDFromInterceptor()).toString();
                if(idString.equals("HOLD") || idString.equals("ON")){
                    // Redis中存在认证信息，且认证通过
                    if(idString.equals("HOLD")){
                        // 如果当前连接为HOLD状态，则重置为ON状态
                        redisTemplate.opsForValue().set(environment.getProperty("stream.server.redis.user-enter-live-room-status") + live_id + ":" + UserRequestInterceptor.getUserIDFromInterceptor(), "ON");
                    }
                    // 返回直播间的详细信息
                    LivePO livePO = liveMapper.getLivePOById(live_id);
                    LiveVO_FULL liveVO_full = new LiveVO_FULL(
                            livePO.getLive_id(),
                            livePO.getLive_name(),
                            livePO.getLive_description(),
                            livePO.getLive_creator(),
                            livePO.getLive_type(),
                            "###########",
                            livePO.getLive_push_path(),
                            livePO.getLive_pull_path(),
                            String.valueOf(livePO.getLive_start_time().getTime()),
                            String.valueOf(livePO.getLive_end_time().getTime()),
                            String.valueOf(livePO.getLive_create_time().getTime()));
                    return new GetLiveRoomDetailResultVO(ResultType.SUCCESS, LiveResultCode.GET_LIVE_ROOM_INFO_SUCCESS, "获取直播间信息成功", liveVO_full);
                }
                else{
                    // 认证失败
                    return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_INFO_FAIL_NO_AUTH, "获取直播间信息失败: 认证信息错误");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_INFO_FAIL_OTHER_REASON, "获取直播间信息失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取直播间观众列表方法
    * @DateTime: 2024/8/20 14:22
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetLiveRoomAudience(String live_id) {
        try{
            // 返回的用户列表
            List<UserVO> userVOList = new ArrayList<>();
            // 从redis中获取所有当前连接
            Set<String> webSocketConnectionSet = redisTemplate.keys(Objects.requireNonNull(environment.getProperty("stream.server.redis.user-enter-live-room-status")) + "*");
            for(String webSocketConnectionKey : webSocketConnectionSet){
                // 遍历每一个key
                if(webSocketConnectionKey.split(":")[2].equals(live_id) && redisTemplate.opsForValue().get(webSocketConnectionKey).equals("ON")){
                    // 遍历到的这个连接是该直播间的连接，且该连接活跃
                    UserPO userPO = userMapper.getUserPOByUserId(webSocketConnectionKey.split(":")[3]);
                    userVOList.add(new UserVO(
                            userPO.getUser_id(),
                            userPO.getUser_name(),
                            userPO.getUser_permission_level()
                    ));
                }
            }
            // 返回观众列表
            return new GetLiveRoomAudienceResultVO(ResultType.SUCCESS, LiveResultCode.GET_LIVE_ROOM_AUDIENCE_SUCCESS, "获取观众列表成功", userVOList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_AUDIENCE_FAIL, "获取观众列表失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有直播间创建者方法
    * @DateTime: 2024/8/22 10:21
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllLiveRoomCreator() {
        try{
            List<String> allLiveRoomCreatorID = liveMapper.getAllLiveRoomCreator();
            List<UserVO> allLiveRoomCreator = new ArrayList<>();
            for(String liveRoomCreatorID : allLiveRoomCreatorID){
                UserPO userPO = userMapper.getUserPOByUserId(liveRoomCreatorID);
                allLiveRoomCreator.add(new UserVO(
                        userPO.getUser_id(),
                        userPO.getUser_name(),
                        userPO.getUser_permission_level()
                ));
            }
            return new GetAllLiveRoomCreatorResultVO(ResultType.SUCCESS, LiveResultCode.GET_ALL_LIVE_ROOM_CREATOR_SUCCESS, "获取所有直播间创建者成功", allLiveRoomCreator);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_ALL_LIVE_ROOM_CREATOR_FAIL, "获取所有直播间创建者失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有自己创建的直播间方法
    * @DateTime: 2024/8/21 16:54
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllMyLiveRoom() {
        return null;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取自己的单个直播间详细信息方法
    * @DateTime: 2024/8/22 16:03
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetMySingleLiveRoomInfo(String live_id) {
        try{
            // 返回直播间的详细信息
            LivePO livePO = liveMapper.getLivePOById(live_id);
            // 检查一下请求的用户是否是该直播间的创建用户
            if(UserRequestInterceptor.getUserIDFromInterceptor().equals(livePO.getLive_creator())){
                LiveVO_FULL liveVO_full = new LiveVO_FULL(
                        livePO.getLive_id(),
                        livePO.getLive_name(),
                        livePO.getLive_description(),
                        livePO.getLive_creator(),
                        livePO.getLive_type(),
                        livePO.getLive_password(),
                        livePO.getLive_push_path(),
                        livePO.getLive_pull_path(),
                        String.valueOf(livePO.getLive_start_time().getTime()),
                        String.valueOf(livePO.getLive_end_time().getTime()),
                        String.valueOf(livePO.getLive_create_time().getTime()));
                return new GetLiveRoomDetailResultVO(ResultType.SUCCESS, LiveResultCode.GET_LIVE_ROOM_INFO_SUCCESS, "获取直播间信息成功", liveVO_full);
            }
            else{
                return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_INFO_FAIL_OTHER_REASON, "获取直播间信息失败：你不是该直播间的创建者");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_INFO_FAIL_OTHER_REASON, "获取直播间信息失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户编辑自己的直播间信息方法
    * @DateTime: 2024/8/22 16:28
    * @Params: [live_id, live_name, live_description, live_password, live_push_path, live_pull_path]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserEditLiveRoomInfo(String live_id, String live_name, String live_description, String live_password, String live_push_path, String live_pull_path, String live_start_time, String live_end_time) {
        try{
            LivePO livePO = liveMapper.getLivePOById(live_id);
            if(UserRequestInterceptor.getUserIDFromInterceptor().equals(livePO.getLive_creator())){
                int res = liveMapper.updateLiveRoomInfo(live_id, live_name, live_description, live_password, live_push_path, live_pull_path, new Date(Long.parseLong(live_start_time)), new Date(Long.parseLong(live_end_time)));
                if(res > 0){
                    return new ResultVO(ResultType.SUCCESS, LiveResultCode.USER_UPDATE_LIVE_ROOM_INFO_SUCCESS, "更新直播间信息成功");
                }
                else{
                    return new ResultVO(ResultType.ERROR, LiveResultCode.USER_UPDATE_LIVE_ROOM_INFO_FAIL, "更新直播间信息失败: 其他原因");
                }
            }
            else{
                return new ResultVO(ResultType.ERROR, LiveResultCode.USER_UPDATE_LIVE_ROOM_INFO_FAIL, "更新直播间信息失败: 你不是该直播间的创建者");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.USER_UPDATE_LIVE_ROOM_INFO_FAIL, "更新直播间信息失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户删除自己的直播间方法
    * @DateTime: 2024/8/23 9:33
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserDeleteLiveRoom(String live_id) {
        try{
            LivePO livePO = liveMapper.getLivePOById(live_id);
            if(UserRequestInterceptor.getUserIDFromInterceptor().equals(livePO.getLive_creator())){
                int res = liveMapper.deleteLiveRoom(live_id);
                if (res > 0){
                    return new ResultVO(ResultType.SUCCESS, LiveResultCode.USER_DELETE_LIVE_ROOM_SUCCESS, "删除直播间成功");
                }
                else{
                    return new ResultVO(ResultType.ERROR, LiveResultCode.USER_DELETE_LIVE_ROOM_FAIL, "删除直播间失败: 其他错误");
                }
            }
            else{
                return new ResultVO(ResultType.ERROR, LiveResultCode.USER_DELETE_LIVE_ROOM_FAIL, "删除直播间失败: 你不是该直播间的创建者");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.USER_DELETE_LIVE_ROOM_FAIL, "删除直播间失败: 其他错误: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有直播间类型方法
    * @DateTime: 2024/8/23 16:25
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllLiveType() {
        try{
            List<LiveTypePO> liveTypePOList = liveMapper.userGetAllLiveType();
            return new GetAllLiveTypeResultVO(ResultType.SUCCESS, LiveResultCode.GET_ALL_LIVE_TYPE_SUCCESS, "获取所有直播间类型成功", liveTypePOList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_ALL_LIVE_TYPE_FAIL, "获取所有直播间类型失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取所有奖品类型方法
    * @DateTime: 2024/8/26 13:20
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllPrizeType() {
        try{
            List<PrizeTypePO> prizeTypePOList = liveMapper.getAllPrizeType();
            return new ListResultVO(ResultType.SUCCESS, LiveResultCode.GET_ALL_PRIZE_TYPE_SUCCESS, "获取奖品类型成功", prizeTypePOList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_ALL_PRIZE_TYPE_FAIL, "获取奖品类型失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户添加新奖品方法
    * @DateTime: 2024/8/26 13:46
    * @Params: [prize_name, prize_description, prize_count, prize_level]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserAddNewPrize(String live_id, String prize_pic, String prize_name, String prize_description, Integer prize_count, String prize_level) {
        try{
            LivePO livePO = liveMapper.getLivePOById(live_id);
            if(livePO.getLive_creator().equals(UserRequestInterceptor.getUserIDFromInterceptor())){
                // 新奖品id
                String prize_id = UUID.randomUUID().toString().replace("-", "");
                // 添加新奖品
                int res = liveMapper.addNewPrize(prize_id, live_id, prize_pic, prize_name, prize_description, prize_count, prize_level);
                if(res > 0){
                    return new ResultVO(ResultType.SUCCESS, LiveResultCode.ADD_NEW_PRIZE_SUCCESS, "添加新奖品成功");
                }
                else{
                    return new ResultVO(ResultType.ERROR, LiveResultCode.ADD_NEW_PRIZE_FAIL, "添加新奖品失败: 其他错误");
                }
            }
            else{
                return new ResultVO(ResultType.ERROR, LiveResultCode.ADD_NEW_PRIZE_FAIL, "添加新奖品失败: 你不是该直播间的创建者");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.ADD_NEW_PRIZE_FAIL, "添加新奖品失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取当前直播间所有奖品方法
    * @DateTime: 2024/8/26 14:22
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllLivePrize(String live_id) {
        try{
            List<PrizePO> prizePOList = liveMapper.getAllLivePrize(live_id);
            return new ListResultVO(ResultType.SUCCESS, LiveResultCode.GET_ALL_LIVE_PRIZE_SUCCESS, "获取奖品成功", prizePOList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_ALL_LIVE_PRIZE_FAIL, "获取奖品失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户删除奖品方法
    * @DateTime: 2024/8/26 15:17
    * @Params: [prize_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserDeletePrize(String prize_id) {
        try{
            int res = liveMapper.deletePrize(prize_id);
            if(res > 0){
                return new ResultVO(ResultType.SUCCESS, LiveResultCode.DELETE_PRIZE_SUCCESS, "删除奖品成功");
            }
            else{
                return new ResultVO(ResultType.ERROR, LiveResultCode.DELETE_PRIZE_FAIL, "删除奖品失败: 其他原因");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.DELETE_PRIZE_FAIL, "删除奖品失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户抽奖方法
    * @DateTime: 2024/8/26 15:47
    * @Params: [prize_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    @Transactional
    public ResultVO DrawPrize(String prize_id) {
        try{
            // 获取奖品信息
            PrizePO prizePO = liveMapper.getPrizePOById(prize_id);
            // 获取该房间中的所有用户
            // 从redis中获取所有当前连接的用户id
            Set<String> webSocketConnectionSet = redisTemplate.keys(Objects.requireNonNull(environment.getProperty("stream.server.redis.user-enter-live-room-status")) + prizePO.getLive_id() + ":*");
            // 抽取一个欧洲人
            int luckyIndex = (int) (Math.random() * webSocketConnectionSet.size());
            String[] userArray = webSocketConnectionSet.toArray(new String[0]);
            String luckyGuyId = userArray[luckyIndex].substring((Objects.requireNonNull(environment.getProperty("stream.server.redis.user-enter-live-room-status")) + prizePO.getLive_id() + ":").length());
            UserPO userPO = userMapper.getUserPOByUserId(luckyGuyId);
            // 存放中奖信息
            // 中奖记录id
            String winning_record_id = UUID.randomUUID().toString().replace("-", "");
            liveMapper.addNewWinningRecord(winning_record_id, prize_id, luckyGuyId);
            // 扣减库存
            liveMapper.prizeStockDecr(prize_id);
            // 给直播间发送中奖消息
            webSocket.sendMsgToSingleLiveRoom(prizePO.getLive_id(), "SERVER", "服务器", WebSocketMessageType.USER_WIN_PRIZE, winning_record_id, userPO.getUser_name() + " 抽中了 " + prizePO.getPrize_name());
            // 给中奖用户发送中奖消息
            webSocket.sendMsgToSingleViewer(prizePO.getLive_id(), "SERVER", "服务器", luckyGuyId, WebSocketMessageType.YOU_ARE_THE_WINNER, "你赢得了 " + prizePO.getPrize_name());

            // 返回中奖信息
            return new ResultVO(ResultType.SUCCESS, LiveResultCode.DRAW_PRIZE_SUCCESS, userPO.getUser_name() + " 抽中了 " + prizePO.getPrize_name());
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户获取某直播间的获奖记录方法
    * @DateTime: 2024/8/27 11:37
    * @Params: [live_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetPrizeWinner(String live_id) {
        try{
            List<LiveRoomWinningRecordBO> liveRoomWinningRecordBOList = liveMapper.getLiveRoomWinningRecord(live_id);
            return new ListResultVO(ResultType.SUCCESS, LiveResultCode.GET_LIVE_ROOM_WINNING_RECORD_SUCCESS, "获取中奖记录成功", liveRoomWinningRecordBOList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, LiveResultCode.GET_LIVE_ROOM_WINNING_RECORD_FAIL, "获取中奖记录失败: " + e.toString());
        }
    }
}
