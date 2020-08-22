package com.taikang.user.service.impl;

import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.common.utils.NumberUtils;
import com.taikang.user.mapper.UserMapper;
import com.taikang.user.pojo.User;
import com.taikang.user.service.UserService;
import com.taikang.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 98050
 * @Time: 2018-10-21 18:42
 * @Feature:
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone";

    private static final String KEY_PREFIX2 = "tk:user:info";

    @Override
    public Boolean checkData(String data, Integer type) {
        //判断数据类型
        User user = new User();
        switch (type){
            case 1 :
                user.setUsername(data);
                break;
            case 2 :
                user.setPhone(data);
                break;
            default:
                //抛出异常
                throw new TkException(ExceptionEnum.INVAILD_USER_DATA_TYPE);
        }
        return userMapper.selectCount(user) == 0;
    }


    /**
     * 发送短信验证码
     * @param phone
     */
    @Override
    public void sendVerifyCode(String phone) {
        //1.生成验证码
        String code = NumberUtils.generateCode(6);

        //封装数据
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);

        //2.发送短信
        amqpTemplate.convertAndSend("tk.sms.exchange","sms.verify.code",msg);

        //3.将code存入redis
        String key = KEY_PREFIX + phone;
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
    }

    @Override
    public void register(User user, String code) {
        //1.校验验证码
        //1.1从redis中取出验证码
        String key = KEY_PREFIX + user.getPhone();
        String codeCache = redisTemplate.opsForValue().get(key);
        //1.2.检查验证码是否正确
        if(StringUtils.equals(code, codeCache)){
            //不正确
            throw new TkException(ExceptionEnum.INVAILD_VERIFY_CODE);
        }
        //2.密码加密
        //2.1生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //2.2加密
        String encodePassword = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(encodePassword);
        //3.写入数据库
//        user.setId(null);
        user.setCreated(new Date());
        int count = userMapper.insertSelective(user);
        //4.如果注册成功，则删掉redis中的code
        if (count==1){
            try{
                redisTemplate.delete(KEY_PREFIX + user.getPhone());
            }catch (Exception e){
                logger.error("删除缓存验证码失败，code:{}",code,e);
            }
        }
    }

//    /**
//     * 用户验证
//     * @param username
//     * @param password
//     * @return
//     */
//    @Override
//    public User queryUser(String username, String password) {
//        /**
//         * 逻辑改变，先去缓存中查询用户数据，查到的话直接返回，查不到再去数据库中查询，然后放入到缓存当中
//         */
//        //1.缓存中查询
//        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(KEY_PREFIX2);
//        String userStr = (String) hashOperations.get(username);
//        User user;
//        if (StringUtils.isEmpty(userStr)){
//            //在缓存中没有查到，去数据库查,查到放入缓存当中
//            User record = new User();
//            record.setUsername(username);
//            user = this.userMapper.selectOne(record);
//            hashOperations.put(user.getUsername(), JsonUtils.toString(user));
//        } else {
//            user =  JsonUtils.toBean(userStr,User.class);
//        }
//
//
//        //2.校验用户名
//        if (user == null){
//            return null;
//        }
//        //3. 校验密码
//        boolean result = CodecUtils.passwordConfirm(username + password,user.getPassword());
//        if (!result){
//            return null;
//        }
//
//        //4.用户名密码都正确
//        return user;
//    }
//
//    /**
//     * 根据用户名修改密码
//     * @param username
//     * @param newPassword
//     * @return
//     */
//    @Override
//    public boolean updatePassword(String username,String oldPassword, String newPassword) {
//        /**
//         * 这里面涉及到对缓存的操作：
//         * 先把数据存到数据库中，成功后，再让缓存失效。
//         */
//        //1.读取用户信息
//        User user = this.queryUser(username,oldPassword);
//        if (user == null){
//            return false;
//        }
//        //2.更新数据库中的用户信息
//        User updateUser = new User();
//        updateUser.setId(user.getId());
//        //2.1密码加密
//        String encodePassword = CodecUtils.passwordBcryptEncode(username.trim(),newPassword.trim());
//        updateUser.setPassword(encodePassword);
//        this.userMapper.updateByPrimaryKeySelective(updateUser);
//        //3.处理缓存中的信息
//        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(KEY_PREFIX+username);
//        hashOperations.delete(username);
//        return true;
//    }

}
