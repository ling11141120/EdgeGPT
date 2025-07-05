package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {


    //查询用户角色数量
    Integer countUserRoleByUserId(Integer userId);

    //删除用户角色
    Integer deleteUserRoleByUserId(Integer userId);
}