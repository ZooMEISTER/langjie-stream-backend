package com.langjie.langjiestreambackend.constant;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/20 12:40
 **/

public interface WebSocketMessageType {
    public static final String ERROR = "ERROR";
    public static final String INFO = "INFO";
    public static final String USER_MESSAGE = "USER_MESSAGE";
    public static final String USER_MESSAGE_HISTORY = "USER_MESSAGE_HISTORY";
    public static final String SERVER_ERROR = "SERVER_ERROR";
    public static final String SERVER_INFO_CONNECT_DISCONNECT = "SERVER_INFO_CONNECT_DISCONNECT";
    public static final String SERVER_INFO_USER_IN_OUT = "SERVER_INFO_USER_IN_OUT";
    public static final String SERVER_MESSAGE = "SERVER_MESSAGE";
    public static final String USER_WIN_PRIZE = "USER_WIN_PRIZE";
    public static final String YOU_ARE_THE_WINNER = "YOU_ARE_THE_WINNER";
}
