package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Setting;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SettingMapper extends BaseMapper<Setting> {
    Setting queryByUserId(Integer userId);
}