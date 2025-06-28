package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

}
