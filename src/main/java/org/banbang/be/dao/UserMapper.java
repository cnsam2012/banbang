package org.banbang.be.dao;

import org.banbang.be.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * 根据 id 查询用户
     * @param id
     * @return
     */
    User selectById (int id);

    /**
     * 根据 WechatOpenId 查询用户信息
     * @param wechatOpenId
     * @return
     */
    User selectByWechatOpenId(String wechatOpenId);

    /**
     * 根据 username 查询用户
     * @param username
     * @return
     */
    User selectByName(String username);

    /**
     * 根据 email 查询用户
     * @param email
     * @return
     */
    User selectByEmail(String email);

    /**
     * 插入用户（注册）
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 插入微信用户（注册）
     * @param user
     * @return
     */
    int insertWechatUser(User user);

    /**
     * 修改用户状态
     * @param id
     * @param status 0：未激活，1：已激活
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 修改头像
     * @param id
     * @param headerUrl
     * @return
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 修改用户名
     * @param id
     * @param username
     * @return
     */
    int updateUsername(int id, String username);

    /**
     * 修改密码
     * @param id
     * @param password 新密码
     * @return
     */
    int updatePassword(int id, String password);

}
