package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.vo.permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/5 14:29
 */

@Service
public class PermissionService extends BaseService<permission,Integer> {


    @Resource
    private PermissionMapper permissionMapper;

    //通过用户ID查询用户拥有的角色ID
    public List<String> queryUserHasRolePermissionValueByUserId(Integer userId) {

        return permissionMapper.queryUserHasRolePermissionValueByUserId(userId);

    }
}
