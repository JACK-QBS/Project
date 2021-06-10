package org.example.service;

import org.example.mapper.RecordMapper;
import org.example.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;

    //数据库中批量插入数据
    public int add(Integer awardId, List<Integer> memberIds) {
        return recordMapper.batchInsert(awardId,memberIds);
    }

    public int deleteByMemberId(Integer id) {
        Record r = new Record();
        r.setMemberId(id);
        //如果属性不为空，就根据这个字段来删
        return recordMapper.delete(r);
    }

    public int deleteByAwardId(Integer id) {
        Record r = new Record();
        r.setAwardId(id);
        return recordMapper.delete(r);
    }
}
