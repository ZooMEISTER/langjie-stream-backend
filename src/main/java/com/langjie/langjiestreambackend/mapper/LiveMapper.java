package com.langjie.langjiestreambackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.langjie.langjiestreambackend.pojo.po.LivePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/12 15:09
 **/

@Mapper
public interface LiveMapper extends BaseMapper<LivePO> {

    // 根据id获取直播间
    @Select("SELECT * FROM all_live WHERE live_id=#{live_id}")
    LivePO getLivePOById(String live_id);

    // 检查某个直播间名是否可用
    @Select("SELECT COUNT(*) FROM all_live WHERE live_name=#{live_name}")
    int checkIfLiveNameNotAvailable(String live_name);

    // 添加新的直播间数据到数据库
    @Insert("INSERT INTO all_live(live_id, live_name, live_description, live_password, live_creator, live_push_path, live_pull_path) VALUES(#{live_id}, #{live_name}, #{live_description}, #{live_password}, #{live_creator}, #{live_push_path}, #{live_pull_path})")
    int insertNewLiveRoom(String live_id, String live_name, String live_description, String live_password, String live_creator, String live_push_path, String live_pull_path);

    // 获取所有直播间列表
    @Select("SELECT * FROM all_live WHERE deleted=0")
    List<LivePO> getAllLiveRoom();

    // 根据条件获取直播间列表
    @Select("SELECT * FROM all_live WHERE deleted=0")
    List<LivePO> getLiveRoom();


}
