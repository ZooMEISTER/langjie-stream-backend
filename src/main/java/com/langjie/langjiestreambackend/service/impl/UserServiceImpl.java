package com.langjie.langjiestreambackend.service.impl;

import com.langjie.langjiestreambackend.constant.ResultType;
import com.langjie.langjiestreambackend.exception.code.UserResultCode;
import com.langjie.langjiestreambackend.mapper.LiveMapper;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.UserVO;
import com.langjie.langjiestreambackend.pojo.vo.result.GetUserInfoResultVO;
import com.langjie.langjiestreambackend.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/21 15:26
 **/

@Service
public class UserServiceImpl implements UserService {

    private Environment environment;
    private RedisTemplate redisTemplate;
    private LiveMapper liveMapper;
    private UserMapper userMapper;

    public UserServiceImpl(Environment environment, RedisTemplate redisTemplate, LiveMapper liveMapper, UserMapper userMapper) {
        this.environment = environment;
        this.redisTemplate = redisTemplate;
        this.liveMapper = liveMapper;
        this.userMapper = userMapper;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户查询用户信息方法
    * @DateTime: 2024/8/21 15:29
    * @Params: [user_id]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO UserGetUserInfo(String user_id) {
        try{
            UserPO userPO = userMapper.getUserPOByUserId(user_id);
            if(userPO != null){
                return new GetUserInfoResultVO(
                        ResultType.SUCCESS,
                        UserResultCode.USER_GET_USER_INFO_SUCCESS,
                        "获取用户信息成功",
                        new UserVO(
                                userPO.getUser_id(),
                                userPO.getUser_name(),
                                userPO.getUser_permission_level()
                        )
                );
            }
            else{
                return new ResultVO(ResultType.ERROR, UserResultCode.USER_GET_USER_INFO_FAIL_USER_NOT_EXIST, "获取用户信息失败: 用户不存在");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResultVO(ResultType.ERROR, UserResultCode.USER_GET_USER_INFO_FAIL_OTHER_REASON, "获取用户信息失败: 其他原因: " + e.toString());
        }
    }
}
