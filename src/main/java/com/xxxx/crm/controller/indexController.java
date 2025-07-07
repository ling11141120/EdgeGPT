package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/27 19:15
 */
@Controller
public class indexController  extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 系统登录⻚
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    // 系统界⾯欢迎⻚
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主⻚⾯
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){

        //查询用户对象 设置session作用域
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user =userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);

        //通过当前用户id查询当前登录用户拥有的资源列表（查询对应资源的授权码）
        List<String> permissions = permissionService.queryUserHasRolePermissionValueByUserId(userId);
        request.getSession().setAttribute("permissions",permissions);



        return "main";
    }
}
