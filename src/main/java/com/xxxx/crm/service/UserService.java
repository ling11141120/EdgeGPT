package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/27 19:52
 * 1：参数判断 用户姓名，密码非空
 *  如果参数为空，抛出异常被controller层捕获
 *   2;调用数据访问层，通过用户名查询用户记录，返回用户对象
 *    3：判断用户对象是否为空
 *    如果对象为空，抛出异常
 */
@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel userLogin(String userName, String userPwd){
        //User user = userMapper.queryUserByName(userName);
     //参数判断 用户姓名，密码非空
        cheacLoginParams(userName,userPwd);
        //调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(null == user,"用户不存在");
        //判断密码是否正确md5
        cheacUserPwd(userPwd,user.getUserPwd());

         return buildUserInfo(user);


    }//修改密码
    /**
     *service层
     *            1：接受controller的四个参数
     *            2：通过用户id查询用户记录，返回用户对象
     *            3：参数校验
     *                待更新用户记录是否存在
     *                原始密码是否为空
     *                原始密码是否正确
     *                判断新密码是否为空
     *                判断新密码是否与原始密码一致
     *                判断确认密码是否为空
     *                判断确认密码是否与新密码一致
     *            4：设置用户新密码
     *                将新密码通过指定算法进行加密
     *            5：执行跟新操作，判断受影响的行数
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId, String oldPwd, String newPwd, String repeatPwd){
        //返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == user,"待更新用户记录不存在");
        //参数校验
        if (user != null) {
            checkPassWordParams(user,oldPwd,newPwd,repeatPwd);
            //设置用户新密码
            user.setUserPwd(Md5Util.encode(newPwd));
            AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"用户密码更新失败");
        }



    }

    //参数判断
    private void checkPassWordParams(User user, String oldPwd, String newPwd, String repeatPwd) {

        //原始密码
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确");
        //新密码
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        AssertUtil.isTrue(newPwd.equals(oldPwd),"新密码不能与原始密码一致");
        //确认密码
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空");
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致");

    }

    private UserModel buildUserInfo(User user) {//构建需要返回给客户端的用户对象

        UserModel userModel = new UserModel();
        //userModel.setUserId(user.getId());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void cheacUserPwd(String userPwd, String Pwd) {

        //密码判断 先加密在比较
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(Pwd),"密码错误");

    }

    private void cheacLoginParams(String userName, String userPwd) {

        //参数判断 用户姓名，密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    //添加用户
    /**
     * 1参数校验
     * 用户名
     * 邮箱，手机号
     * 2设置参数的默认值
     * isValid=1
     * createDate updateDate
     * 3执行添加，判断结果
     * 默认密码 md5加密
     * 4返回受影响的行数
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone());
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("1234"));

        AssertUtil.isTrue(userMapper.insertSelective(user)!=1,"用户添加失败！");

        relationUserRole(user.getId(),user.getRoleIds());

    }

    private void checkUserParams(String userName, String email, String phone) {

        //参数校验
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        //通过用户名查询用户记录数，若存在，则表示用户已存在
        AssertUtil.isTrue(userMapper.queryUserByName(userName)!=null,"用户已存在！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {

        // 1. 参数校验：判断用户ID是否存在且合法
        AssertUtil.isTrue(user.getId() == null, "用户ID不能为空！");
        User temp = selectByPrimaryKey(user.getId()); // 从数据库查询原始用户数据
        AssertUtil.isTrue(temp == null, "待更新用户记录不存在！");

        // 2. 参数校验：用户名 (userName) - **此项保持唯一性校验**
        // 校验提交的用户名是否为空白
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");

        // 只有当提交的用户名与原始用户名不同时，才需要进行重复性校验
        if (!user.getUserName().equals(temp.getUserName())) {
            // 查询数据库中是否存在与新用户名相同的用户
            User existingUserWithSameName = userMapper.queryUserByName(user.getUserName());
            // 如果查到了用户，并且这个用户的ID不是当前正在更新的用户的ID，则说明用户名重复
            // **这需要 userMapper.queryUserByName 返回 User 对象**
            AssertUtil.isTrue(existingUserWithSameName != null && !existingUserWithSameName.getId().equals(user.getId()), "用户名已存在！");
        }

        // 3. 参数校验：邮箱 (email) - **只校验是否为空白，不校验唯一性**
        // 如果提交的邮箱为空白，你可以选择将数据库中的邮箱字段也设置为 null (根据业务需求)
        if (!StringUtils.isNotBlank(user.getEmail())) { // 如果邮箱为空白
            user.setEmail(null); // 设置为null，以便更新数据库时清空该字段
        }
        // 如果你的业务需要邮箱格式校验，可以在这里添加，例如：
        // AssertUtil.isTrue(!isValidEmailFormat(user.getEmail()), "邮箱格式不正确！"); // 假设 isValidEmailFormat 是一个校验方法


        // 4. 参数校验：手机号 (phone) - **只校验格式和是否为空白，不校验唯一性**
        // 如果提交的手机号不为空白，则进行格式校验
        if (StringUtils.isNotBlank(user.getPhone())) {
            AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "手机号格式不正确！"); // 手机号格式校验
        } else {
            // 如果提交的手机号为空白，你可以选择将数据库中的手机号字段也设置为 null (根据业务需求)
            user.setPhone(null);
        }

        // 5. 设置更新时间
        user.setUpdateDate(new Date());

        // 6. 执行更新操作，并判断受影响的行数
        AssertUtil.isTrue(updateByPrimaryKeySelective(user) < 1, "用户更新失败！");

        // 7. 更新用户角色关联 (如果更新用户时也允许修改角色，并且user对象中包含roleIds)
        // 确保 relationUserRole 方法中的逻辑是正确的，并且能处理roleIds为null或空的情况。
        relationUserRole(user.getId(), user.getRoleIds());
    }

    // ... (relationUserRole 方法以及其他辅助方法)
/**
 * 用户删除
 * 1判断ids是否为空 长度是否大于0
 * */
    public void deleteByIds(Integer[] ids) {

        AssertUtil.isTrue(ids==null||ids.length==0,"待删除记录不存在！");
        AssertUtil.isTrue(deleteBatch(ids)<ids.length,"用户删除失败！");

        //遍历用户id的数组
        for (Integer userId : ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            if (count != null && count > 0) {
                int deletedCount = userRoleMapper.deleteUserRoleByUserId(userId);
                if (deletedCount <= 0) {
                    throw new ParamsException("用户角色删除失败！");
                }
            }
        }


    }

    /*** *
     用户角色关联
     添加操作
        原始角色不存在
           1：不添加新的角色 不操作中间表
           2：添加新的角色记录 给指定用户绑定相关的角色记录

     更新操作
           1：原始角色存在  添加新的角色记录 给指定用户绑定相关的角色记录  不添加新的角色记录 不操作中间表  删除 旧的角色记录
              移除部分角色，添加新的角色记录 给指定用户绑定相关的角色记录
           2：原始角色不存在  添加新的角色记录 给指定用户绑定相关的角色记录  不添加新的角色记录 不操作中间表

     如何进行角色分配
           1：将原有的角色记录进行移除 添加新的角色记录
           2：原始角色不存在  添加新的角色记录

     删除操作
     1：删除指定用户绑定的角色记录

      */

    @Transactional(propagation = Propagation.REQUIRED) // 添加事务注解，因为有数据库操作
    public void relationUserRole(Integer userId, String roleIds) {
        System.out.println("userRoleMapper is null in relationUserRole: " + (userRoleMapper == null));

        // 确保 userId 不为 null，这是最基本的参数校验
        AssertUtil.isTrue(userId == null, "用户ID不能为空，无法进行角色分配！");

        // 通过用户id查询当前用户绑定的角色记录数
        // 这里的 userRoleMapper 已经被确认不是 null，所以可以移除多余的 null 检查
        Integer count = userRoleMapper.countUserRoleByUserId(userId);

        // 如果用户有角色关联，先删除这些旧的关联记录
        if (count != null && count > 0){ // 加上 null 判断，虽然 countUserRoleByUserId 返回 Integer 不会是 primitive int，但作为 Integer 对象可以为 null
            // userRoleMapper 已经被确认不是 null，移除多余的 null 检查
            AssertUtil.isTrue(!Objects.equals(userRoleMapper.deleteUserRoleByUserId(userId), count),"角色分配失败！");
        }

        // 判断角色ID字符串是否存在且不为空，如果存在则添加新的角色记录
        // 使用 StringUtils.split 更安全地处理 roleIds，它能处理 null 输入，返回 null。
        // 然后再判断返回的数组是否为 null 或空。
        String[] roleIdArray = null;
        if (StringUtils.isNotBlank(roleIds)) { // 这一行是 219 行最可能的位置
            roleIdArray = StringUtils.split(roleIds, ","); // <--- 这行才是真正调用 split 的地方，如果 roleIds 是 null，这行会抛 NPE
        }

        if (roleIdArray != null && roleIdArray.length > 0) {
            List<UserRole> userRoleList = new ArrayList<>();
            for (String roleId : roleIdArray) {
                // 确保 roleId 不是空字符串，避免 Integer.parseInt("") 报错
                if (StringUtils.isNotBlank(roleId)) {
                    try {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(Integer.parseInt(roleId));
                        userRole.setUpdateDate(new Date());
                        userRole.setCreateDate(new Date());
                        userRoleList.add(userRole);
                    } catch (NumberFormatException e) {
                        // 记录日志或抛出自定义异常，表示角色ID格式不正确
                        System.err.println("Invalid role ID format: " + roleId + " for user " + userId);
                        // 可以选择跳过此角色或抛出业务异常
                    }
                }
            }

            // 批量添加用户角色记录
            if (!userRoleList.isEmpty()) {
                AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) < userRoleList.size(), "用户角色添加失败！");
            }
        }
    }

}
