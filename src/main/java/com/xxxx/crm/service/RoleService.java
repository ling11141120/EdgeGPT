package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import com.xxxx.crm.vo.permission;
import org.apache.commons.lang3.StringUtils; // 确保导入这个类
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;


    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        // 修正后的验证：角色名称不能为空白（null、空字符串或只包含空格）
        // 如果 role.getRoleName() 是空白的，这个条件为 TRUE，然后抛出异常。
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输入角色名称！"); // 修正后的代码

        // 验证唯一性：确保角色名称不重复
        // roleMapper.selectByRoleName(role.getRoleName()) 应该在没有同名角色时返回 null。
        AssertUtil.isTrue(roleMapper.selectByRoleName(role.getRoleName()) != null, "角色名称已存在！");

        role.setIsValid(1);
        role.setCreateDate(new java.util.Date());
        role.setUpdateDate(new java.util.Date());

        // 验证数据库插入操作是否成功
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        //修改角色 参数校验 非空判断  数据存在
        // 角色ID判断
        // 默认值 修改时间
        //执行修改 判断受影响的行数
        AssertUtil.isTrue(role.getId() == null, "待更新记录不存在！");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        AssertUtil.isTrue(roleMapper.selectByRoleName(role.getRoleName()) != null && !(role.getId()).equals(role.getId()), "角色名称已存在！");
        role.setUpdateDate(new java.util.Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色更新失败！");


    }
    //删除操作
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        //参数判断
        AssertUtil.isTrue(roleId == null, "待删除记录不存在！");
        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(temp == null, "待删除记录不存在！");
        //执行删除
        if (temp != null) {
            temp.setIsValid(0);
        }
        if (temp != null) {
            temp.setUpdateDate(new java.util.Date());
        }

        AssertUtil.isTrue(roleMapper.deleteByPrimaryKey(roleId) < 1, "角色删除失败！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mids) {

        //将对应的角色id与资源id添加到对应的权限表中
        //先将已有的权限删除，在将需要设置的权限记录添加到权限表中  批量添加
        //通过角色id查询权限记录
        //如果有权限记录则删除
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        if (mids != null && mids.length > 0) {

            List<permission> permissionList = new ArrayList<>();

            for (Integer mid : mids) {

                permission permission = new permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                permission.setAclValue(moduleMapper.selectByPrimaryKey( mid).getOptValue());
                permission.setCreateDate(new java.util.Date());
                permission.setUpdateDate(new java.util.Date());
                //将对象设置到集合中
                permissionList.add(permission);
            }

            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)!=permissionList.size(),"角色授权失败");
        }



    }
}