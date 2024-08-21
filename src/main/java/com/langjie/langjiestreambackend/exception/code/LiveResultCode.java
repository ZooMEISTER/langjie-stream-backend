package com.langjie.langjiestreambackend.exception.code;

/**
 * @Author ZooMEISTER
 * @Description: 直播相关请求返回状态码
 * @DateTime 2024/8/12 15:00
 **/

public interface LiveResultCode {
    public static final int ADD_NEW_LIVE_ROOM_SUCCESS = 13000;
    public static final int ADD_NEW_LIVE_ROOM_FAIL_NAME_ALREADY_EXIST = 13001;
    public static final int ADD_NEW_LIVE_ROOM_FAIL_OTHER_ERROR = 13002;
    public static final int ENTER_LIVE_ROOM_REQUEST_APPROVE = 13100;
    public static final int ENTER_LIVE_ROOM_REQUEST_REJECT_WRONG_PASSWORD = 13101;
    public static final int ENTER_LIVE_ROOM_REQUEST_REJECT_OTHER_REASON = 13102;
    public static final int GET_LIVE_ROOM_INFO_SUCCESS = 13200;
    public static final int GET_LIVE_ROOM_INFO_FAIL_NO_AUTH = 13201;
    public static final int GET_LIVE_ROOM_INFO_FAIL_OTHER_REASON = 13202;
    public static final int GET_LIVE_ROOM_AUDIENCE_SUCCESS = 13300;
    public static final int GET_LIVE_ROOM_AUDIENCE_FAIL = 13301;
}
