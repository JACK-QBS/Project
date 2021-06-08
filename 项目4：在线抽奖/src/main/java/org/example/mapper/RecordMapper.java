package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.base.BaseMapper;
import org.example.model.Record;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RecordMapper extends BaseMapper<Record> {
    int batchInsert(@Param("awardId")Integer awardId,
                    @Param("memberIds") List<Integer> memberIds);

    int delete(Record r);
}