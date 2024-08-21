package com.langjie.langjiestreambackend.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.langjie.langjiestreambackend.constant.ResultType;
import com.langjie.langjiestreambackend.exception.NoPermissionException;
import com.langjie.langjiestreambackend.exception.UserNotExistException;
import com.langjie.langjiestreambackend.exception.code.InterceptorResultCode;
import com.langjie.langjiestreambackend.mapper.UserMapper;
import com.langjie.langjiestreambackend.pojo.po.UserPO;
import com.langjie.langjiestreambackend.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author ZooMEISTER
 * @Description: 用户请求拦截器，用于请求鉴权等
 * @DateTime 2024/8/12 11:54
 **/

@Component
public class UserRequestInterceptor implements HandlerInterceptor {

    private UserMapper userMapper;
    public UserRequestInterceptor(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 用于保存本次请求的user_id
    private static final ThreadLocal<String> currentRequestUserID = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果请求是浏览器预检，直接放行
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 获取请求路径
        String requestPath = request.getServletPath();
        // 请求的 权限等级
        int expectPermissionLevel = -1;

        // 实际等级权限
        if(requestPath.startsWith("/tourist")) expectPermissionLevel = 0;
        else if(requestPath.startsWith("/live")) expectPermissionLevel = 1;
        else if(requestPath.startsWith("/user")) expectPermissionLevel = 1;

        // 判断等级权限
        if(expectPermissionLevel > 0){
            try{
                // 获取请求头中的 token
                String token = request.getHeader("Authorization").substring(7);
                // 解析 token，获得 user_id
                Jws<Claims> claimsJws = JWTUtils.parseClaim(token);
                String user_id = String.valueOf(claimsJws.getPayload().get("user_id"));
                UserPO userPO = userMapper.getUserPOByUserId(user_id);
                if(userPO != null){
                    // 用户存在，获取该用户的权限等级并比较
                    if(expectPermissionLevel > userPO.getUser_permission_level()){
                        throw new NoPermissionException();
                    }
                    currentRequestUserID.set(user_id);
                    // 验证通过，放行
                    return true;
                }
                else {
                    throw new UserNotExistException();
                }
            }
            catch (Exception e){
                JSONObject jsonObject = new JSONObject();

                if(e instanceof SignatureException){
                    // token 无效
                    jsonObject.put("resultType", ResultType.ERROR);
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_INVALID_TOKEN);
                    jsonObject.put("msg", "token 无效");
                }
                else if(e instanceof UserNotExistException){
                    // 用户不存在
                    jsonObject.put("resultType", ResultType.ERROR);
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_USER_NOT_EXIST);
                    jsonObject.put("msg", "用户不存在");
                }
                else if(e instanceof NoPermissionException){
                    // 没有权限
                    jsonObject.put("resultType", ResultType.ERROR);
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_NO_PERMISSION);
                    jsonObject.put("msg", "没有权限");
                }
                else if(e instanceof ExpiredJwtException){
                    // token过期
                    jsonObject.put("resultType", ResultType.ERROR);
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_TOKEN_EXPIRED);
                    jsonObject.put("msg", "TOKEN 已过期");
                }
                else{
                    // 其他错误
                    jsonObject.put("resultType", ResultType.ERROR);
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_OTHER_ERROR);
                    jsonObject.put("msg", "其他错误: " + e.toString());
                }

                String jsonObjectStr = JSONObject.toJSONString(jsonObject);
                ReturnJson(response, jsonObjectStr);

                currentRequestUserID.remove();
                return false;
            }
        }
        else if(expectPermissionLevel == 0){
            // 该类请求直接放行
            return true;
        }
        else{
            // 该类请求路径错误，非法请求，直接拦截
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resultType", ResultType.ERROR);
            jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_ILLEGAL_REQUEST);
            jsonObject.put("msg", "非法请求");
            String jsonObjectStr = JSONObject.toJSONString(jsonObject);
            ReturnJson(response, jsonObjectStr);

            currentRequestUserID.remove();
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        currentRequestUserID.remove();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 拦截器返回结果的方法
    * @DateTime: 2024/8/12 13:21
    * @Params: [response, json]
    * @Return void
    */
    private void ReturnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 从本线程ThreadLocal中获取用户id
    * @DateTime: 2024/8/12 14:49
    * @Params:
    * @Return
    */
    public static String getUserIDFromInterceptor(){
        return currentRequestUserID.get();
    }
}
