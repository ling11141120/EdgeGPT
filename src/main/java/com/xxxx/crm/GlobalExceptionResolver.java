package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 11:43
 * &#064;Description: 全局异常处理类
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
/*    private final HttpServletResponse response;

    public GlobalExceptionResolver(HttpServletResponse response) {
        this.response = response;
    }*/



    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

/*
  设置默认异常处理，返回视图
 */      //判断是否抛出未登录异常 ，如果抛出 则要求用户登录，跳转到登录页面
        if(ex instanceof NoLoginException){
            //重定向到登录页面
            return new ModelAndView("redirect:/index");
        }

         ModelAndView modelAndView = new ModelAndView("error");
         modelAndView.addObject("code",500);
         modelAndView.addObject("message","系统异常，请稍后再试");
        //判断错误类型
        if (handler instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上声名的注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (responseBody != null){
                if (ex instanceof ParamsException){
                    ParamsException p=(ParamsException) ex;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }
                return modelAndView;
            }else {
                   //设置默认异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常！");
                //判断异常是否是自定异常
                if (ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                //设置响应类型和编码格式
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if (out != null){
                        out.close();
                    }
                }
                    return null;
            }
        }

         return modelAndView;

    }
}
