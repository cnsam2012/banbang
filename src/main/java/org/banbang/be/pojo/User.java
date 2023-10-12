package org.banbang.be.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 * 对应数据库表 `user`
 */
@Data
public class User {
    private int id;

    private String username;

    private String password;

    private String salt;

    private String email;

    private int type;

    private int status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;
}
