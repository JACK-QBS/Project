package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Award;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AwardMapper extends BaseMapper<Award> {
    List<Award> queryBySettingId(Integer id);
}