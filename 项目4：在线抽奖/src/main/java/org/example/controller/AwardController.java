package org.example.controller;

import org.example.model.Award;
import org.example.model.User;
import org.example.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/award")
public class AwardController {

    @Autowired
    private AwardService awardService;

    //新增奖项
    @PostMapping("/add")
    public Object add(@RequestBody Award award, HttpSession session) {
        User user = (User) session.getAttribute("user");
        award.setSettingId(user.getSettingId());
        int n = awardService.insert(award);
        return award.getId();
    }

    //修改奖项：
    @PostMapping("/update")
    public Object update(@RequestBody Award award) {
        int n = awardService.update(award);
        return null;
    }

    //删除奖项：
    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable Integer id) {
        //根据主键删
        int n = awardService.delete(id);
        return null;
    }
}
