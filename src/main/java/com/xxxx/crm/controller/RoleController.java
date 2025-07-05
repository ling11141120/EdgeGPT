package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/3 19:32
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){

        return roleService.queryAllRoles(userId);
    }

    @RequestMapping("list")
    @ResponseBody
    public  Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }
    //进入角色管理页面
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("你真牛逼");
    }

    //打开添加角色页面
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer roleId, HttpServletRequest request){

        if (roleId != null){
            this.roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role",this.roleService.selectByPrimaryKey(roleId));
        }
        return "role/add_update";
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("你真牛逼");
    }

    //删除角色
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("你真牛逼");
    }

    @RequestMapping("addGrantPage")
    public String  addGrantPage(Integer roleId,HttpServletRequest request){
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        return success("你真牛逼");
    }

}
