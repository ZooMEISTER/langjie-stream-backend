package com.langjie.langjiestreambackend.service.impl;

import com.langjie.langjiestreambackend.constant.ResultType;
import com.langjie.langjiestreambackend.exception.UserResultCode;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import com.langjie.langjiestreambackend.pojo.vo.ResultVO;
import com.langjie.langjiestreambackend.pojo.vo.result.LoginSuccessResultVO;
import com.langjie.langjiestreambackend.service.TouristService;
import com.langjie.langjiestreambackend.utils.JWTUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @Author ZooMEISTER
 * @Description: 游客 Service Impl
 * @DateTime 2024/8/9 11:07
 **/

@Service
public class TouristServiceImpl implements TouristService {
    private UserMapper userMapper;
    public TouristServiceImpl(UserMapper userMapper){this.userMapper = userMapper;}

    /**
    * @Author: ZooMEISTER
    * @Description: 游客Service测试方法
    * @DateTime: 2024/8/9 11:37
    * @Params: []
    * @Return java.lang.String
    */
    @Override
    public String TouristServiceTest() {
        return "Tourist Service Test Success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客注册方法
    * @DateTime: 2024/8/9 11:52
    * @Params: [user_name, user_password]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    @Transactional
    public ResultVO TouristRegister(String user_name, String user_password) {
        try{
            synchronized (this){
                int isUsernameNotAvailable = userMapper.checkIfUserNameNotAvailable(user_name);
                if(isUsernameNotAvailable > 0){
                    // 该用户名不可用
                    return new ResultVO(ResultType.ERROR, UserResultCode.REGISTER_FAIL_USERNAME_UNAVAILABLE, "注册失败: 用户名已存在");
                }
                else{
                    // 该用户名可用
                    String user_id = UUID.randomUUID().toString().replace("-", "");
                    int res = userMapper.insertNewUser(user_id, user_name, user_password);
                    if(res > 0){
                        return new ResultVO(ResultType.SUCCESS, UserResultCode.REGISTER_SUCCESS, "注册成功");
                    }
                    else{
                        return new ResultVO(ResultType.ERROR, UserResultCode.REGISTER_FAIL_OTHER_REASON, "注册失败: 其他原因");
                    }
                }
            }
        }
        catch (Exception e) {
            return new ResultVO(ResultType.ERROR, UserResultCode.REGISTER_FAIL_OTHER_REASON, "注册失败: " + e.toString());
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客登陆方法
    * @DateTime: 2024/8/9 11:52
    * @Params: [user_name, user_password]
    * @Return com.langjie.langjiestreambackend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO TouristLogin(String user_name, String user_password) {
        try{
            int isUsernameNotAvailable = userMapper.checkIfUserNameNotAvailable(user_name);
            if(isUsernameNotAvailable > 0){
                // 该用户名存在
                UserPO userPO = userMapper.getUserPOByUserName(user_name);
                if(user_password.equals(userPO.getUser_password())){
                    // 生成token
                    String newToken = JWTUtils.genAccessToken(userPO.getUser_id());
                    return new LoginSuccessResultVO(ResultType.SUCCESS, UserResultCode.LOGIN_SUCCESS, "登陆成功", newToken);
                }
                else{
                    return new ResultVO(ResultType.ERROR, UserResultCode.LOGIN_FAIL_WRONG_PASSWORD, "登陆失败: 密码错误");
                }
            }
            else{
                // 该用户名不存在
                return new ResultVO(ResultType.ERROR, UserResultCode.LOGIN_FAIL_USERNAME_NOT_EXIST, "登陆失败: 用户名不存在");
            }
        }
        catch (Exception e){
            return new ResultVO(ResultType.ERROR, UserResultCode.REGISTER_FAIL_OTHER_REASON, "注册失败: " + e.toString());
        }
    }
}
