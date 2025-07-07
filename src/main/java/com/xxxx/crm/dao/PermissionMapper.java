package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.permission;

import java.security.Permission;
import java.util.List;


public interface PermissionMapper extends BaseMapper<permission,Integer> {


    //查询通过角色id查询权限记录数
     Integer countPermissionByRoleId(Integer roleId);


    //删除角色id权限记录
    void deletePermissionByRoleId(Integer roleId);

    //通过角色id查询权限id
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    //通过用户id查询权限值
    List<String> queryUserHasRolePermissionValueByUserId(Integer userId);

    //通过模块id查询权限记录数
    Integer countPermissionByModuleId(Integer id);

    //通过模块id删除权限记录
    Integer deletePermissionByModuleId(Integer id);
}