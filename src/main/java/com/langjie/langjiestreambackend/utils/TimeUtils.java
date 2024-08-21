package com.langjie.langjiestreambackend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/20 12:33
 **/

public class TimeUtils {

    public static Long getCurrentMs(){
        return System.currentTimeMillis();
    }

    public static Date getCurrentTime(){
        return new Date(System.currentTimeMillis());
    }

    public static String getCurrentTimeMsString(){
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getCurrentTimeInString(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return formatter.format(new Date(System.currentTimeMillis()));
    }
}
