package org.example.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户表
 */
@Getter
@Setter
@ToString
public class User {
    
    private Integer id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 头像url
     */
    private String head;

    /**
     * 创建时间
     */
    private Date createTime;

    private Integer settingId;
}