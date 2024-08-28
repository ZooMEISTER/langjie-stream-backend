package com.langjie.langjiestreambackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.langjie.langjiestreambackend.pojo.bo.LiveRoomWinningRecordBO;
import com.langjie.langjiestreambackend.pojo.po.LivePO;
import com.langjie.langjiestreambackend.pojo.po.LiveTypePO;
import com.langjie.langjiestreambackend.pojo.po.PrizePO;
import com.langjie.langjiestreambackend.pojo.po.PrizeTypePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
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
    @Insert("INSERT INTO all_live(live_id, live_name, live_description, live_password, live_creator, live_type, live_push_path, live_pull_path, live_start_time, live_end_time) VALUES(#{live_id}, #{live_name}, #{live_description}, #{live_password}, #{live_creator}, #{live_type}, #{live_push_path}, #{live_pull_path}, #{live_start_time}, #{live_end_time})")
    int insertNewLiveRoom(String live_id, String live_name, String live_description, String live_password, String live_creator, String live_type, String live_push_path, String live_pull_path, Date live_start_time, Date live_end_time);

    // 获取所有直播间列表
    @Select("SELECT * FROM all_live WHERE deleted=0")
    List<LivePO> getAllLiveRoom();

    // 根据条件获取直播间列表
    @Select("SELECT * FROM all_live WHERE ${sqlString} deleted=0")
    List<LivePO> getLiveRoom(String sqlString);

    // 获取所有直播间创建者
    @Select("SELECT DISTINCT(live_creator) FROM all_live")
    List<String> getAllLiveRoomCreator();

    // 往数据库中插入新服务器数据
    @Insert("INSERT INTO all_message_server(msg_id, msg_type, msg, msg_send_time) VALUES(#{msg_id}, #{msg_type}, #{msg}, #{msg_send_time})")
    int insertNewServerMsg(String msg_id, String msg_type, String msg, Date msg_send_time);

    // 往数据库中插入新用户信息数据
    @Insert("INSERT INTO all_message_user(msg_id, msg_live_id, msg_sender_id, msg_type, msg, msg_send_time) VALUES(#{msg_id}, #{msg_live_id}, #{msg_sender_id}, #{msg_type}, #{msg}, #{msg_send_time})")
    int insertNewUserMsg(String msg_id, String msg_live_id, String msg_sender_id, String msg_type, String msg, Date msg_send_time);

    // 更新直播间数据库的直播间信息
    @Update("UPDATE all_live SET live_name=#{live_name}, live_description=#{live_description}, live_password=#{live_password}, live_push_path=#{live_push_path}, live_pull_path=#{live_pull_path}, live_start_time=#{live_start_time}, live_end_time=#{live_end_time} WHERE live_id=#{live_id}")
    int updateLiveRoomInfo(String live_id, String live_name, String live_description, String live_password, String live_push_path, String live_pull_path, Date live_start_time, Date live_end_time);

    // 用户删除直播间
    @Update("UPDATE all_live SET deleted=1 WHERE live_id=#{live_id}")
    int deleteLiveRoom(String live_id);

    // 用户获取所有的直播间类型
    @Select("SELECT * FROM live_type")
    List<LiveTypePO> userGetAllLiveType();

    // 用户获取所有奖品类型等级
    @Select("SELECT * FROM prize_type")
    List<PrizeTypePO> getAllPrizeType();

    // 添加新奖品
    @Insert("INSERT INTO all_prize(prize_id, live_id, prize_pic, prize_name, prize_description, prize_count_total, prize_count_remain, prize_level) " +
            "VALUES(#{prize_id}, #{live_id}, #{prize_pic}, #{prize_name}, #{prize_description}, #{prize_count}, #{prize_count}, #{prize_level})")
    int addNewPrize(String prize_id, String live_id, String prize_pic, String prize_name, String prize_description, Integer prize_count, String prize_level);

    // 获取某个直播间的所有奖品
    @Select("SELECT * FROM all_prize WHERE live_id=#{live_id} AND prize_count_remain > 0 AND deleted=0")
    List<PrizePO> getAllLivePrize(String live_id);

    // 用户删除某个奖品
    @Update("UPDATE all_prize SET deleted=1 WHERE prize_id=#{prize_id}")
    int deletePrize(String prize_id);

    // 获取某个奖品信息
    @Select("SELECT * FROM all_prize WHERE prize_id=#{prize_id}")
    PrizePO getPrizePOById(String prize_id);

    // 存放中奖信息
    @Insert("INSERT INTO all_winning_record(winning_record_id, prize_id, user_id) VALUES(#{winning_record_id}, #{prize_id}, #{user_id})")
    int addNewWinningRecord(String winning_record_id, String prize_id, String user_id);

    // 扣减奖品库存
    @Update("UPDATE all_prize SET prize_count_remain=prize_count_remain-1 WHERE prize_id=#{prize_id}")
    int prizeStockDecr(String prize_id);

    // 获取某个直播间的所有抽奖记录
    @Select("SELECT awr.winning_record_id, awr.prize_id, awr.user_id, awr.record_time, au.user_name, au.user_real_name, ap.live_id, ap.prize_pic, ap.prize_name, ap.prize_description, ap.prize_level\n" +
            "FROM\n" +
            "all_winning_record awr INNER JOIN all_prize ap\n" +
            "ON awr.prize_id = ap.prize_id\n" +
            "INNER JOIN all_user au ON awr.user_id = au.user_id\n" +
            "WHERE ap.live_id=#{live_id} AND ap.deleted = 0")
    List<LiveRoomWinningRecordBO> getLiveRoomWinningRecord(String live_id);
}
