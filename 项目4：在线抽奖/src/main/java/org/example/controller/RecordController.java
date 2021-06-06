package org.example.controller;

import org.example.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    //抽奖：
    @PostMapping("/add/{awardId}")
    public Object add(@PathVariable Integer awardId,
                      @RequestBody List<Integer> memberIds) {
        int n = recordService.add(awardId,memberIds);
        return null;
    }

    //删除当前奖项的 某个 获奖人员（根据人员id删除）
    @GetMapping("/delete/member")
    public Object deleteByMemberId(Integer id) {
        int n = recordService.deleteByMemberId(id);
        return null;
    }

    //删除当前奖项的 所有 获奖人员（根据奖项id删除）
    @GetMapping("/delete/award")
    public Object deleteByAwardId(Integer id) {
        int n = recordService.deleteByAwardId(id);
        return null;
    }
}
