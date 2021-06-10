package org.example.service;

import org.example.mapper.AwardMapper;
import org.example.mapper.MemberMapper;
import org.example.mapper.SettingMapper;
import org.example.model.Award;
import org.example.model.Member;
import org.example.model.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingMapper settingMapper;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private MemberMapper memberMapper;

    public Setting query(Integer userId) {
        Setting setting = settingMapper.queryByUserId(userId);
        List<Award> awards = awardMapper.queryBySettingId(setting.getId());//奖项属性
        setting.setAwards(awards);
        List<Member> members = memberMapper.queryBySettingId(setting.getId());//成员属性
        setting.setMembers(members);
        return setting;
    }

    public int update(Setting setting) {
        return settingMapper.updateByPrimaryKeySelective(setting);
    }
}
