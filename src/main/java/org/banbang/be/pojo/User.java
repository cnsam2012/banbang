package org.banbang.be.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 用户
 * 对应数据库表 `user`
 */
@Data
@ApiModel("用户")
public class User {
    @ApiModelProperty(hidden = true)
    private int id;

    @ApiModelProperty(value = "用户名", example = "zhijie", required = true)
    private String username;

    @ApiModelProperty(value = "用户密码", example = "shijian123", required = true)
    private String password;

    @ApiModelProperty(hidden = true)
    private String salt;

    @ApiModelProperty(value = "邮箱，用于接受激活邮件以及后续找回密码", example = "8anbang@gmail.com")
    private String email;

    @ApiModelProperty(hidden = true, example = "3")
    private int type;

    @ApiModelProperty(hidden = true, example = "1")
    private int status;

    @ApiModelProperty(hidden = true)
    private String activationCode;

    @ApiModelProperty(hidden = true)
    private String headerUrl;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private String wechatOpenId;
}
