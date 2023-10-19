package org.banbang.be.util;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.banbang.be.pojo.ro.WeLoginRo;
import org.banbang.be.util.constant.WechatApiType;
import org.junit.jupiter.api.Test;


@Slf4j
public class WechatLoginUtil {
    /**
     * 微信登录
     *
     * @param code 前端请求获取 => 微信code
     * @return void
     * @author zhangjunrong
     * @date 2022/12/19 20:16
     */
    public static WeLoginRo toAppletsWxLogin(String appId, String secret, String code) {
        //1.通过前端给的code获取openid和access_token还有unionid
        String getTokenOpenid = StrFormatter.format(
                WechatApiType.APPLETS_GET_TOKEN_OPENID.value(),
                appId,
                secret,
                code
        );
        String data = HttpUtil.get(getTokenOpenid);
        log.info("GET {}", getTokenOpenid);
        log.info("getting openid - {}", data);

        //json => bean
        WeLoginRo weLoginRo = JSONUtil.toBean(data, WeLoginRo.class);

        //获取用户信息失败
        if (!ObjectUtil.isEmpty(weLoginRo.getErrcode())) {
            log.error("get id failed: {}", weLoginRo.getErrmsg());
            log.error("the object got from api is: {}", weLoginRo.toString());
        }

        return weLoginRo;
    }


    @Test
    void test() {
        WechatLoginUtil.toAppletsWxLogin(
                "wx7850d4bdeb683e5a",
                "71461445fa0f91c2204296f91758813c",
                "0b1baEFa1W6pcG0TJ7Ia1JpBdx3baEFp"
        );
    }
}
