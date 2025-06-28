package com.xxxx.crm.interceptor;

import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 14:40
 */
@Component
public class NoLoginInterceptor  implements HandlerInterceptor {

     @Resource
     private UserMapper userMapper;
    //拦截用户是否是登录状态，再目标资源执行前执行的方法 返回true,表示可以执行

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //获取cookie中的用户id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId == null || userMapper.selectByPrimaryKey(userId)==null) {
             //抛出未登录异常
            throw new NoLoginException();
        }

        return true;
    }
}
