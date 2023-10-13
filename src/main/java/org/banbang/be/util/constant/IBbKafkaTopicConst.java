package org.banbang.be.util.constant;

public interface IBbKafkaTopicConst {
    // Kafka 主题：评论
    String TOPIC_COMMNET = "comment";

    // Kafka 主题：点赞
    String TOPIC_LIKE = "like";

    // Kafka 主题：关注
    String TOPIC_FOLLOW = "follow";

    // Kafka 主题：发帖
    String TOPIC_PUBLISH = "publish";

    // Kafka 主题：删帖
    String TOPIC_DELETE = "delete";
}
