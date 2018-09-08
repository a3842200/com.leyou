package com.leyou.user.controller;

import com.leyou.item.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 45207
 * @create 2018-09-06 23:30
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,
                                             @PathVariable(value = "type", required = false) Integer type){
        //初始化参数类型
        if (type == null) {
            type = 1;
        }
        //校验数据
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone){
        userService.sendCode(phone);
        //204 请求已接收
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(User user, @RequestParam("code") String code){
        //TODO 完成数据格式校验
        userService.register(user, code);
        return ResponseEntity.status((HttpStatus.CREATED)).build();

    }
}
