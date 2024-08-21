package com.langjie.langjiestreambackend.scheduler;

import com.langjie.langjiestreambackend.utils.TimeUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @Author ZooMEISTER
 * @Description: 清除直播间历史消息的定时任务
 * @DateTime 2024/8/21 14:22
 **/

@Component
public class LiveRoomHistoryMsgClearScheduler {

    private Environment environment;
    private RedisTemplate redisTemplate;

    public LiveRoomHistoryMsgClearScheduler(Environment environment, RedisTemplate redisTemplate) {
        this.environment = environment;
        this.redisTemplate = redisTemplate;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 清理直播间的过期历史消息
    * @DateTime: 2024/8/21 14:27
    */
    @Scheduled(fixedDelay = 30000)
    public void clearLiveRoomHistoryMsg(){
        // 从 Redis 中把每个直播间的所有历史消息全部取出来
        // 先遍历每个直播间
        for(String key : (Set<String>) Objects.requireNonNull(redisTemplate.keys("langjie_stream:live_room_chat_history:*"))){
            // 直接remove range by score
            redisTemplate.opsForZSet().removeRangeByScore(
                    key,
                    0,
                    TimeUtils.getCurrentMs() - Long.parseLong(environment.getProperty("stream.server.redis.live-room-chat-history-time")));
        }
    }
}
