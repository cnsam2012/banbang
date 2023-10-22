package org.banbang.be.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.banbang.be.dao.UserMapper;
import org.banbang.be.pojo.LoginTicket;
import org.banbang.be.pojo.User;
import org.banbang.be.pojo.ro.WeLoginRo;
import org.banbang.be.pojo.vo.WeUserInfoVo;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.MailClient;
import org.banbang.be.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.banbang.be.util.WechatLoginUtil;
import org.banbang.be.util.constant.BbActivationStatus;
import org.banbang.be.util.constant.BbExpiredSeconds;
import org.banbang.be.util.constant.BbUserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.baomidou.mybatisplus.extension.toolkit.Db.getOne;


/**
 * 用户相关
 */
@Service
@Slf4j
public class UserService {

    @Value("${wechat.applets.appid}")
    private String APPLETS_APPID;

    @Value("${wechat.applets.secret}")
    private String APPLETS_SECRET;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 网站域名
     */
    @Value("${community.path.domain}")
    private String domain;

    /**
     * 项目名(访问路径)
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 根据 Id 查询用户，不返回密码与盐值
     *
     * @param id
     * @return
     */
    public User findUserById(int id) {
        // return userMapper.selectById(id);

        User user = getCache(id); // 优先从缓存中查询数据
        if (user == null) {
            user = initCache(id);
        }

        user.setPassword("");
        user.setSalt("");
        return user;
    }


    /**
     * 根据 username 查询用户，不返回密码与盐值
     *
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        User user = userMapper.selectByName(username);
        if (ObjectUtil.isNotEmpty(user)) {
            user.setPassword("");
            user.setSalt("");
        }
        return user;
    }


    /**
     * 用户注册
     *
     * @param user
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明注册成功
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 验证账号是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }

        // 验证邮箱是否已存在
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }

        // 注册用户
        user.setSalt(BbUtil.generateUUID().substring(0, 5)); // salt
        user.setPassword(BbUtil.md5(user.getPassword() + user.getSalt())); // 加盐加密
        user.setType(0); // 默认普通用户
        user.setStatus(0); // 默认未激活
        user.setActivationCode(BbUtil.generateUUID()); // 激活码

        // 随机头像（用户登录后可以自行修改）
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date()); // 注册时间
        userMapper.insertUser(user);

        // 给注册用户发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/echo/activation/用户id/激活码
        String url = domain + (contextPath.equals("/") ? "" : contextPath) + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活 班帮(Banbang) 账号", content);

        return map;
    }

    /**
     * 激活用户
     *
     * @param userId 用户 id
     * @param code   激活码
     * @return
     */
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            // 用户已激活
            return BbActivationStatus.ACTIVATION_REPEAT.value();
        } else if (user.getActivationCode().equals(code)) {
            // 修改用户状态为已激活
            userMapper.updateStatus(userId, 1);
            clearCache(userId); // 用户信息变更，清除缓存中的旧数据
            return BbActivationStatus.ACTIVATION_SUCCESS.value();
        } else {
            return BbActivationStatus.ACTIVATION_FAILURE.value();
        }
    }

    /**
     * 用户登录（为用户创建凭证）
     *
     * @param username
     * @param password
     * @param expiredSeconds 多少秒后凭证过期
     * @return Map<String, Object> 返回错误提示消息以及 ticket(凭证)
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            // 账号未激活
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        // 验证密码
        password = BbUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        // 用户名和密码均正确，为该用户生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(BbUtil.generateUUID()); // 随机凭证
        loginTicket.setStatus(0); // 设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000)); // 设置凭证到期时间

        // 将登录凭证存入 redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * 用户退出（将凭证状态设为无效）
     *
     * @param ticket
     */
    public void logout(String ticket) {
        // loginTicketMapper.updateStatus(ticket, 1);
        // 修改（先删除再插入）对应用户在 redis 中的凭证状态
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    /**
     * 根据 ticket 查询 LoginTicket 信息
     *
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket) {
        // return loginTicketMapper.selectByTicket(ticket);

        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        // TODO
        log.info("get from redis -- {}", (LoginTicket) redisTemplate.opsForValue().get(redisKey));
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 修改用户头像
     *
     * @param userId
     * @param headUrl
     * @return
     */
    public int updateHeader(int userId, String headUrl) {
        // return userMapper.updateHeader(userId, headUrl);
        int rows = userMapper.updateHeader(userId, headUrl);
        clearCache(userId);
        return rows;
    }

    /**
     * 修改用户密码（对新密码加盐加密存入数据库）
     *
     * @param userId
     * @param newPassword 新密码
     * @return
     */
    public int updatePassword(int userId, String newPassword) {
        User user = userMapper.selectById(userId);
        // 重新加盐加密
        newPassword = BbUtil.md5(newPassword + user.getSalt());
        clearCache(userId);
        return userMapper.updatePassword(userId, newPassword);
    }

    /**
     * 修改用户名
     *
     * @param userId
     * @param newUsername
     * @return
     */
    public int updateUsername(int userId, String newUsername) {
        clearCache(userId);
        return userMapper.updateUsername(userId, newUsername);
    }

    /**
     * 优先从缓存中取值
     *
     * @param userId
     * @return
     */
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 缓存中没有该用户信息时，则将其存入缓存
     *
     * @param userId
     * @return
     */
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * 用户信息变更时清除对应缓存数据
     *
     * @param userId
     */
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 获取某个用户的权限
     *
     * @param userId
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return BbUserAuth.AUTHORITY_ADMIN.value();
                    case 2:
                        return BbUserAuth.AUTHORITY_MODERATOR.value();
                    default:
                        return BbUserAuth.AUTHORITY_USER.value();
                }
            }
        });
        return list;
    }


    /**
     * 发送邮箱验证码
     *
     * @param account 账户名, 目前是用户名
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明发送验证码成功
     */
    public Map<String, Object> doSendEmailCode4ResetPwd(String account) {
        Map<String, Object> map = new HashMap<>(2);
        User user = userMapper.selectByName(account);
        if (user == null) {
            map.put("errMsg", "未发现账号");
            return map;
        }
        final String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            map.put("errMsg", "该账号未绑定邮箱");
            return map;
        }

        // 生成6位验证码
        String randomCode = BbUtil.getRandomCode(6);
        // 给注册用户发送激活邮件
        Context context = new Context();
        context.setVariable("email", "您的验证码是 " + randomCode);
        // http://localhost:8080/echo/activation/用户id/激活码
        String url = domain + (contextPath.equals("/") ? "" : contextPath) + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(email, "重置 Echo 账号密码", content);

        final String redisKey = "EmailCode4ResetPwd:" + account;

        redisTemplate.opsForValue().set(redisKey, randomCode, 600, TimeUnit.SECONDS);
        return map;
    }

    /**
     * 发送邮箱验证码
     *
     * @param account 账户名, 目前是用户名
     * @return Map<String, Object> 返回错误提示消息，如果返回的 map 为空，则说明发送验证码成功
     */
    public Map<String, Object> doResetPwd(String account, String password) {
        Map<String, Object> map = new HashMap<>(2);
        if (StringUtils.isBlank(password)) {
            map.put("errMsg", "密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(account);
        if (user == null) {
            map.put("errMsg", "未发现账号");
            return map;
        }
        final String passwordEncode = BbUtil.md5(password + user.getSalt());
        int i = userMapper.updatePassword(user.getId(), passwordEncode);
        if (i <= 0) {
            map.put("errMsg", "修改数据库密码错误");
        } else {
            clearCache(user.getId());
        }
        return map;
    }

    /**
     * 根据提供的code，为微信用户登录/注册，并更新登录状态
     *
     * @param code
     * @return
     */
    public WeUserInfoVo loginByWeApplets(String code) {
        Map<String, Object> data = new HashMap<>();

        //1.通过code 获取到用户的微信信息
        WeLoginRo loginRo = WechatLoginUtil.toAppletsWxLogin(APPLETS_APPID, APPLETS_SECRET, code);

        // 返回异常处理
        if (!ObjectUtil.isEmpty(loginRo.getErrcode())) {
            log.error("验证失败");
            return new WeUserInfoVo().setId(-1).setErrMsg(loginRo.getErrmsg());
        }

        //2.数据库对比 openid 是否已有信息 如果没有就保持数据库
        var openId = loginRo.getOpenid();
        User user = userMapper.selectByWechatOpenId(openId);
        log.info("数据库查询openid:{} , 返回:{}", openId, user);

        if (ObjectUtil.isEmpty(user)) {
            //定义用户默认信息
            var uNrandom = String.valueOf(System.currentTimeMillis());
            var userName = "班马" + RandomUtil.randomStringUpper(4) + uNrandom.substring(uNrandom.length() - 5);
            var userType = 0;
            var userStatus = 1;
            var userHeaderUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
            var userCreateTime = new Date();
            var wechatOpenId = openId;

            var userInserted = new User();
            userInserted.setUsername(userName);
            userInserted.setType(userType);
            userInserted.setStatus(userStatus);
            userInserted.setHeaderUrl(userHeaderUrl);
            userInserted.setCreateTime(userCreateTime);
            userInserted.setWechatOpenId(wechatOpenId);

            userMapper.insertWechatUser(userInserted);
            user = userMapper.selectByWechatOpenId(wechatOpenId); // 此处返回的User带Id

            if (ObjectUtil.isEmpty(user)) {
                log.error("新建用户失败");
                return new WeUserInfoVo().setId(-1).setErrMsg("新建用户失败，请联系管理员");
            }

            log.info("微信新用户！ -- {}", user);
        }

        // 为该用户生成登录凭证
        var expiredSeconds = BbExpiredSeconds.REMEMBER_EXPIRED_SECONDS.value();
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(BbUtil.generateUUID()); // 随机凭证
        loginTicket.setStatus(0); // 设置凭证状态为有效（当用户登出的时候，设置凭证状态为无效）
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000)); // 设置凭证到期时间

        // 将登录凭证存入 redis
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        return (
                new WeUserInfoVo()
                        .setByUserExpectTicket(user)
                        .setTicket(loginTicket.getTicket())
        );
    }

}
