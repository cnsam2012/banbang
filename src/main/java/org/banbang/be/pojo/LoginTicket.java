package org.banbang.be.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket; // 凭证
    private int status; // 状态（是否有效）
    private Date expired; // 过期时间
}
