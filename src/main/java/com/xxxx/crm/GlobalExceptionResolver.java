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

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 如果是未登录异常，重定向到登录页面
        if (ex instanceof NoLoginException) {
            return new ModelAndView("redirect:/index");
        }

        // 默认返回 error.jsp 页面
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "系统异常，请稍后再试");

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            // ✅ 如果方法上有 @ResponseBody 注解，说明是要返回 JSON 数据
            if (responseBody != null) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请稍后再试");

                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }

                // 返回 null 表示已经手动响应
                return null;
            }

            // ✅ 否则是页面请求（返回 error.jsp），设置友好提示
            if (ex instanceof ParamsException) {
                ParamsException p = (ParamsException) ex;
                modelAndView.addObject("code", p.getCode());
                modelAndView.addObject("msg", p.getMsg());
            }
        }

        return modelAndView;
    }
}

