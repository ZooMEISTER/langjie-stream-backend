package com.langjie.langjiestreambackend.exception.code;

/**
 * @Author ZooMEISTER
 * @Description: 拦截器返回结果码
 * @DateTime 2024/8/12 13:23
 **/

public interface InterceptorResultCode {
    public static final int INTERCEPTED_INVALID_TOKEN = 10001;
    public static final int INTERCEPTED_USER_NOT_EXIST = 10002;
    public static final int INTERCEPTED_NO_PERMISSION = 10003;
    public static final int INTERCEPTED_ILLEGAL_REQUEST = 10004;
    public static final int INTERCEPTED_TOKEN_EXPIRED = 10005;
    public static final int INTERCEPTED_OTHER_ERROR = 10006;
}
