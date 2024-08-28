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
    public static final int GET_ALL_LIVE_ROOM_SUCCESS = 13210;
    public static final int GET_ALL_LIVE_ROOM_FAIL = 13211;
    public static final int GET_ALL_LIVE_TYPE_SUCCESS = 13220;
    public static final int GET_ALL_LIVE_TYPE_FAIL = 13221;
    public static final int GET_ALL_PRIZE_TYPE_SUCCESS = 13230;
    public static final int GET_ALL_PRIZE_TYPE_FAIL = 13231;
    public static final int ADD_NEW_PRIZE_SUCCESS = 13240;
    public static final int ADD_NEW_PRIZE_FAIL = 13241;
    public static final int DELETE_PRIZE_SUCCESS = 13242;
    public static final int DELETE_PRIZE_FAIL = 13243;
    public static final int DRAW_PRIZE_SUCCESS = 13244;
    public static final int DRAW_PRIZE_FAIL = 13245;
    public static final int GET_ALL_LIVE_PRIZE_SUCCESS = 13250;
    public static final int GET_ALL_LIVE_PRIZE_FAIL = 13251;
    public static final int GET_LIVE_ROOM_WINNING_RECORD_SUCCESS = 13260;
    public static final int GET_LIVE_ROOM_WINNING_RECORD_FAIL = 13261;
    public static final int GET_LIVE_ROOM_AUDIENCE_SUCCESS = 13300;
    public static final int GET_LIVE_ROOM_AUDIENCE_FAIL = 13301;
    public static final int GET_ALL_LIVE_ROOM_CREATOR_SUCCESS = 13310;
    public static final int GET_ALL_LIVE_ROOM_CREATOR_FAIL = 13311;
    public static final int USER_GET_ALL_MY_LIVE_ROOM_SUCCESS = 13320;
    public static final int USER_GET_ALL_MY_LIVE_ROOM_FAIL = 13321;
    public static final int USER_UPDATE_LIVE_ROOM_INFO_SUCCESS = 13330;
    public static final int USER_UPDATE_LIVE_ROOM_INFO_FAIL = 13331;
    public static final int USER_DELETE_LIVE_ROOM_SUCCESS = 13340;
    public static final int USER_DELETE_LIVE_ROOM_FAIL = 13341;
}
