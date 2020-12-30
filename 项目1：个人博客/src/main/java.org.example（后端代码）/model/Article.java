package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Article {
    private Integer id;
    private String title; //标题
    private String content; //文章内容
    private Date createTime; //创作时间
    private Integer viewCount; //访问次数
    private Integer userId;
}
