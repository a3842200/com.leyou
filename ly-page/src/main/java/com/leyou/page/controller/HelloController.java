package com.leyou.page.controller;

import com.leyou.page.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author 45207
 * @create 2018-08-31 22:38
 */
@Controller
@RequestMapping
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        User user = new User();
        user.setName("Chen Jack");
        user.setAge(50);
        user.setBirthday(new Date(451121245));
        user.setFriend(new User("李小龙", 78));
        model.addAttribute("user", user);
        return "hello";
    }
}
