package org.banbang.be.util.constant;

public enum BbExpiredSeconds {

    /**
     * 默认的登录凭证超时时间 (12小时)
     */
    DEFAULT_EXPIRED_SECONDS(3600 * 12),

    /**
     * 记住我状态下的凭证超时时间 (7天)
     */
    REMEMBER_EXPIRED_SECONDS(3600 * 24 * 7);

    private final int value;

    BbExpiredSeconds(int i) {
        this.value = i;
    }

    public int value() {
        return value;
    }
}
