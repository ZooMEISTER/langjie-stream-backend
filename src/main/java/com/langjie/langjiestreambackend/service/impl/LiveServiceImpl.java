package com.langjie.langjiestreambackend.service.impl;

import com.langjie.langjiestreambackend.constant.ResultType;
import com.langjie.langjiestreambackend.exception.code.LiveResultCode;
import com.langjie.langjiestreambackend.interceptor.UserRequestInterceptor;
import com.langjie.langjiestreambackend.mapper.LiveMapper;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.po.LivePO;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import com.langjie.langjiestreambackend.pojo.vo.LiveVO_FULL;
import com.langjie.langjiestreambackend.pojo.vo.LiveVO_SUB;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.UserVO;
import com.langjie.langjiestreambackend.pojo.vo.result.EnterLiveRoomResultVO;
import com.langjie.langjiestreambackend.pojo.vo.result.GetLiveRoomAudienceResultVO;
import com.langjie.langjiestreambackend.pojo.vo.result.GetLiveRoomDetailResultVO;
import com.langjie.langjiestreambackend.service.LiveService;
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
    public LiveServiceImpl(Environment environment, RedisTemplate redisTemplate, LiveMapper liveMapper, UserMapper userMapper) {
        this.environment = environment;
        this.redisTemplate = redisTemplate;
        this.liveMapper = liveMapper;
        this.userMapper = userMapper;
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
    public ResultVO UserAddNewLiveRoom(String live_name, String live_description, String live_password, boolean autoAllocateStreamPath, String live_push_path, String live_pull_path) {
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
                        live_push_path,
                        live_pull_path
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
            return new ResultVO(ResultType.ERROR, LiveResultCode.ADD_NEW_LIVE_ROOM_FAIL_OTHER_ERROR, "直播间添加失败: " + e.toString());
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
    public List<LiveVO_SUB> UserGetLiveRoom() {
        List<LivePO> livePOList = liveMapper.getLiveRoom();
        List<LiveVO_SUB> liveVOSUBList = new ArrayList<>();
        for(LivePO livePO : livePOList){
            UserPO userPO = userMapper.getUserPOByUserId(livePO.getLive_creator());
            liveVOSUBList.add(new LiveVO_SUB(
                    livePO.getLive_id(),
                    livePO.getLive_id(),
                    livePO.getLive_name(),
                    livePO.getLive_description(),
                    livePO.getLive_creator(),
                    userPO.getUser_name(),
                    !livePO.getLive_password().equals(""),
                    livePO.getLive_create_time()
            ));
        }
        return liveVOSUBList;
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
            if(isPasswordRight){
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
                            livePO.getLive_push_path(),
                            livePO.getLive_pull_path(),
                            livePO.getLive_create_time());
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
    * @Description: 用户获取所有自己创建的直播间方法
    * @DateTime: 2024/8/21 16:54
    * @Params: []
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetAllMyLiveRoom() {
        return null;
    }
}
