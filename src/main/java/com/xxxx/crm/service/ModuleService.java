package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.model.TreeModule;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

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

    public Map<String, Object> queryModuleList() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", moduleMapper.queryModuleList().size());
        map.put("data", moduleMapper.queryModuleList());

        return map;
    }
    /**添加资源
     * 1，参数校验
     * 模块名称 moduleName
     *    非空 ，唯一
     * 地址 url
     *    二级菜单非空grade=1  不可重复
     * 上级菜单 parentId
     *    如果是一级菜单grade=0，则上级菜单为0
     *    二级菜单grade=1，则上级菜单非0
     * 层级 grade
     *  0 1 2
     * 权限码 optValue
     *    非空，不可重复
     * 2，设置参数的默认值
     *   是否有效 isValid=1
     *   系统当前时间
     *   系统当前时间
     * 3，执行添加，判断结果
     * <p>
     *
     * */

    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module) {
       //判断非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"菜单名称不能为空");
        //层级只有0  1 2
        AssertUtil.isTrue(module.getGrade()==null||!(module.getGrade()==0||module.getGrade()==1||module.getGrade()==2),"菜单层级有误");
        //模块名称同意层级下不能重复
        AssertUtil.isTrue(moduleMapper.queryModuleByName(module.getModuleName(),module.getGrade())!=null,"该层级下该名称已存在");
       //二级菜单非空grade=1 同一层级不可重复
        if (module.getGrade()==1){
            //非空
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url不能为空");
            AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl())!=null,"该层级下该权限值已存在");
            //父级菜单 一级菜单 null  二级菜单 非空
        }
        if (module.getGrade()==0){
            module.setParentId( -1);
        }
        if (module.getGrade()!=0){
            AssertUtil.isTrue(module.getParentId()==null,"请选择父级菜单");
           AssertUtil.isTrue(null==moduleMapper.selectByPrimaryKey(module.getParentId()),"请选择正确的父级菜单");
        }
        //授权码 非空，不可重复
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入授权码");
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByOptValue(module.getOptValue()),"授权码已存在");

        //设置默认值
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.insertSelective(module)<1,"菜单添加失败！");


    }

    //菜单更新
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {

        //参数校验 id非空，数据存在 通过id查询资源对象
        AssertUtil.isTrue(module.getId() == null,"待更新记录不存在！");
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(temp == null,"待更新记录不存在！");
        //层级验证
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade==0||grade == 1 || grade == 2),"层级值不合法！");
        //模块名称验证
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入模块名称！");
        temp = moduleMapper.queryModuleByName(module.getModuleName(),grade);
        if (temp != null){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该名称已存在！");
        }
        //地址url
        if (grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请输入URL！");
            temp = moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            if (temp != null){
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该地址已存在！");
            }
        }
        //权限码
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入权限码！");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if (temp != null){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该权限码已存在！");

        }

        //设置参数的默认值
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1, "模块更新失败！");
        //执行更新操作，判断受影响的行数


    }



    /**
     *
     * 删除资源
     * 判断删除的记录是否存在
     * 如果当前记录存在子记录 不可删除
     *删除资源时 将对应的权限表的记录也删除 判断权限表中是否存在关联数据
     *执行删除操作， 逻辑删除
     * 判断受影响的行数
     * <p>
     *
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {

        AssertUtil.isTrue(null==id,"待删除记录不存在");
        Module temp=moduleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==temp,"待删除记录不存在");
        //如果当前记录存在子记录 不可删除
        Integer count=moduleMapper.queryModuleParentId(id);
        AssertUtil.isTrue(count>0,"该资源存在子记录，不可删除");
        //
        count=permissionMapper.countPermissionByModuleId(id);
        if (count>0){
            permissionMapper.deletePermissionByModuleId(id);
        }
        if (temp != null) {
            temp.setIsValid((byte) 0);
        }
        if (temp != null) {
            temp.setUpdateDate(new Date());
        }
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp)<1,"删除失败");
    }
}
