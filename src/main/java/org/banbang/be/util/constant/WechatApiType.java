package org.banbang.be.util.constant;

public enum WechatApiType {
    /**APP版 通过code获取access_token,openid的接口。所需参数 appid secret code*/
    GET_TOKEN_OPENID("https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code"),

    /**APP版 通过access_token,openid获取微信用户信息的接口。所需参数 access_token openid*/
    GET_WX_USERINFO("https://api.weixin.qq.com/sns/userinfo?access_token={}&openid={}"),

    /**小程序版 通过code获取access_token,openid的接口。所需参数 appid secret code*/
    APPLETS_GET_TOKEN_OPENID("https://api.weixin.qq.com/sns/jscode2session?appid={}&secret={}&js_code={}&grant_type=authorization_code");


    private final String value;

    WechatApiType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
