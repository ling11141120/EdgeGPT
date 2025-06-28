package com.xxxx.crm.controller;

import com.github.pagehelper.PageException;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/27 19:52
 */

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        try {

            //userService.userLogin(userName,userPwd);
            UserModel userModel = userService.userLogin(userName,userPwd);
            resultInfo.setResult(userModel);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }
        return resultInfo ;
    }


    //修改密码
    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePassWord(HttpServletRequest  request, String oldPassword, String newPassword, String repeatPassword){

        ResultInfo resultInfo = new ResultInfo();
        try{
            //获取cookie中的userid
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //调用service层方法执行修改密码
            userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
            e.printStackTrace();
        }

            return resultInfo;
    }

    //修改密码页面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

}
