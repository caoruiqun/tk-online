package com.taikang.user.web;

import com.taikang.user.pojo.User;
import com.taikang.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @Author: 98050
 * @Time: 2018-10-21 18:40
 * @Feature:
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户数据检查
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type){
        Boolean result = userService.checkData(data,type);
//        if (result == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
        return ResponseEntity.ok(result);
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> senVerifyCode(@RequestParam("phone") String phone){
        userService.sendVerifyCode(phone);
//        if (result == null || !result){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
        userService.register(user,code);
//        if(result == null || !result){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 用户验证 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUserNameAndPassword(@RequestParam("username") String username,
                                                               @RequestParam("password")String password){
        User user = userService.queryUserByUserNameAndPassword(username,password);
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

}
