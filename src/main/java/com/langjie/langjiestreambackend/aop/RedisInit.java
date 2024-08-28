package com.langjie.langjiestreambackend.aop;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.langjie.langjiestreambackend.mapper.LiveMapper;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/26 12:33
 **/

@Component
public class RedisInit {

    private UserMapper userMapper;
    private LiveMapper liveMapper;
    private RedisTemplate redisTemplate;
    private Environment environment;

    public RedisInit(UserMapper userMapper, LiveMapper liveMapper, RedisTemplate redisTemplate, Environment environment) {
        this.userMapper = userMapper;
        this.liveMapper = liveMapper;
        this.redisTemplate = redisTemplate;
        this.environment = environment;
    }

    @PostConstruct
    public void RedisInitialize(){
        // 初始化Redis中的数据
        List<UserPO> userPOList = userMapper.getAllUserPO();
        for(UserPO userPO : userPOList){
            redisTemplate.opsForValue().set(environment.getProperty("stream.server.redis.user") + userPO.getUser_id(), JSONObject.toJSONString(userPO));
        }
    }
}
