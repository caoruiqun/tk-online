package com.taikang.user.service;

import com.taikang.user.pojo.User;

/**
 * @Author: 98050
 * @Time: 2018-10-21 18:41
 * @Feature:
 */
public interface UserService {
    /**
     * 检查用户名和手机号是否可用
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    void sendVerifyCode(String phone);

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    void register(User user, String code);

    /**
     * 用户验证 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User queryUserByUserNameAndPassword(String username, String password);

    /**
     * 根据用户名修改密码
     * @param username
     * @param newPassword
     * @return
     */
//    boolean updatePassword(String username, String oldPassword, String newPassword);
}
