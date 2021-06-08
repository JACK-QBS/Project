package org.example.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 抽奖人员
 */
@Getter
@Setter
@ToString
public class Member {
    
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String no;

    /**
     * 抽奖设置id
     */
    private Integer settingId;

    /**
     * 创建时间
     */
    private Date createTime;
}