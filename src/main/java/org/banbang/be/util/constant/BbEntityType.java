package org.banbang.be.util.constant;

public enum BbEntityType {

    /**
     * 实体类型：帖子
     */
    ENTITY_TYPE_POST(1),

    /**
     * 实体类型：评论
     */
    ENTITY_TYPE_COMMENT(2),

    /**
     * 实体类型：人
     */
    ENTITY_TYPE_USER(3);

    private final int value;

    BbEntityType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
