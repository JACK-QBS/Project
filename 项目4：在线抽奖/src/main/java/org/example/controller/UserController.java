package org.example.controller;

import org.example.exception.AppException;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * controller 调用 service
 * controller 用来接收请求，校验请求数据（包括是否为空，格式，数据类型是否有问题）
 */
@RestController
@RequestMapping("/user")//类上定义的统一服务路径
public class UserController {
    //注入service属性
    @Autowired
    private UserService userService;

    //登录：
    @PostMapping("/login")
    public Object login(@RequestBody User user, HttpServletRequest req) {
        //根据前端传入的账号（账号唯一），在数据库中查找用户信息，返回一个用户
        User exist = userService.query(user.getUsername());
        //用户不存在，抛自定义异常
        if (exist == null) throw new AppException("USR001","用户不存在");
        //用户存在，判断密码是否一致
        if (!exist.getPassword().equals(user.getPassword())) throw new AppException("USR002","用户名或密码错误");
        //用户名、密码验证通过，创建 session 并保存用户信息
        HttpSession session = req.getSession();
        //保存数据库查询出来的User，信息是全的
        session.setAttribute("user",exist);
        return null;
    }

    //注册：
    @PostMapping("/register")
    //注意：请求数据绑定 user 对象中的属性，及 headFile 变量名，要一致
    public Object register(User user, MultipartFile headFile) {
        userService.register(user,headFile);
        return null;
    }

    //注销：
    @GetMapping("/logout")
    public Object logout(HttpSession session) {
        session.removeAttribute("user");
        return null;
    }
}
