package org.banbang.be.util.constant;

public enum BbKafkaTopic {

    /**
     * Kafka 主题：评论
     */
    TOPIC_COMMNET("comment"),

    /**
     * Kafka 主题：点赞
     */
    TOPIC_LIKE("like"),

    /**
     * Kafka 主题：关注
     */
    TOPIC_FOLLOW("follow"),

    /**
     * Kafka 主题：发帖
     */
    TOPIC_PUBLISH("publish"),

    /**
     * Kafka 主题：删帖
     */
    TOPIC_DELETE("delete");

    private final String value;

    BbKafkaTopic(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
