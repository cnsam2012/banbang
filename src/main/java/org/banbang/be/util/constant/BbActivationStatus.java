package org.banbang.be.util.constant;

public enum BbActivationStatus {
    /**
     * 激活成功
     */
    ACTIVATION_SUCCESS(0),

    /**
     * 重复激活
     */
    ACTIVATION_REPEAT(1),

    /**
     * 激活失败
     */
    ACTIVATION_FAILURE(2);

    private final int value;

    BbActivationStatus(int i) {
        this.value = i;
    }

    public int value() {
        return value;
    }
}
