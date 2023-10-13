package org.banbang.be.util.constant;

public enum BbSystemUser {

    /**
     * 系统用户的 id
     */
    SYSTEM_USER_ID(1);

    private final int value;

    BbSystemUser(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
