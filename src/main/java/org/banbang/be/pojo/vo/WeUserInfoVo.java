package org.banbang.be.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.banbang.be.pojo.User;

@Data
@Accessors(chain = true)
public class WeUserInfoVo {
    /**
     * 用户id
     */
    private int id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String headerUrl;

    /**
     * ticket 用作token（用户登录凭证）
     */
    private String ticket;

    /**
     * 若异常，存储错误信息
     */
    private String errMsg;

    /**
     * 通过user设置WeUserVo属性
     * 注意token未赋值
     * @param user
     * @return
     */
    public WeUserInfoVo setByUserExpectTicket(User user) {
        this.setUsername(user.getUsername());
        this.setId(user.getId());
        this.setHeaderUrl(user.getHeaderUrl());
        return this;
    }
}
