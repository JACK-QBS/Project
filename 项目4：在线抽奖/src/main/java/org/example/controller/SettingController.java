package org.example.controller;

import org.example.model.Setting;
import org.example.model.User;
import org.example.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/query")
    public Object query(HttpSession session) {
        //返回字段是 setting 对象属性，缺少的属性先构造好
        //需要 setting 对象，可以通过 user_id 从数据库查询
        //user_id 从 session 中的 user 获取
        User user = (User) session.getAttribute("user");//获取 User 对象
        Setting setting = settingService.query(user.getId());//获取 setting 对象
        setting.setUser(user);
        return setting;
    }

    @GetMapping("/update")
    public Object update(HttpSession session,Integer batchNumber) {
        //数据库中取用户：
        User user = (User) session.getAttribute("user");
        //根据 settingId 修改 batchNumber
        Setting setting = new Setting();
        setting.setId(user.getSettingId());
        setting.setBatchNumber(batchNumber);
        int n = settingService.update(setting);
        return null;
    }
}
