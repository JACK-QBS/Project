package org.example.controller;

import org.example.model.Member;
import org.example.model.User;
import org.example.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    //新增抽奖人员
    @PostMapping("/add")
    public Object add(@RequestBody Member member, HttpSession session) {
        User user = (User) session.getAttribute("user");
        member.setSettingId(user.getSettingId());
        int n = memberService.add(member);
        return member.getId();
    }

    //修改抽奖人员
    @PostMapping("/update")
    public Object update(@RequestBody Member member) {
        int n = memberService.update(member);
        return null;
    }

    //删除抽奖人员
    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable Integer id) {
        int n = memberService.delete(id);
        return null;
    }
}
