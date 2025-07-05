package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModule;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/4 20:09
 */
@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    // 查询所有模块

    public List<TreeModule> queryAllModules(Integer roleId) {
        List<TreeModule> treeModuleList = moduleMapper.queryAllModules();
        //查询指定角色已经授权过的资源列表
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        if (permissionIds != null && !permissionIds.isEmpty()) {
            // 循环所有的资源列表，判断用户拥有的资源id中有没有匹配的
           treeModuleList.forEach(treeModule -> {
               if (permissionIds.contains(treeModule.getId())) {
                   treeModule.setChecked(true);
               }
           });


        }

        return treeModuleList;

    }
}
