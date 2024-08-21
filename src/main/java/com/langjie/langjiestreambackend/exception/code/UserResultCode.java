package com.langjie.langjiestreambackend.exception.code;

/**
 * @Author ZooMEISTER
 * @Description: 用户本身的相关请求的返回状态码
 * @DateTime 2024/8/9 11:59
 **/

public interface UserResultCode {
    public static final int REGISTER_SUCCESS = 11000;
    public static final int LOGIN_SUCCESS = 11001;
    public static final int REGISTER_FAIL_USERNAME_UNAVAILABLE = 12000;
    public static final int REGISTER_FAIL_OTHER_REASON = 12001;
    public static final int LOGIN_FAIL_USERNAME_NOT_EXIST = 12002;
    public static final int LOGIN_FAIL_WRONG_PASSWORD = 12003;
    public static final int USER_GET_USER_INFO_SUCCESS = 12100;
    public static final int USER_GET_USER_INFO_FAIL_USER_NOT_EXIST = 12101;
    public static final int USER_GET_USER_INFO_FAIL_OTHER_REASON = 12102;
}
