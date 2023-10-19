package org.banbang.be;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan("org.banbang.be.dao")
@Slf4j
public class BbbeApplication {

    @PostConstruct
    public void init() {
        // 解决 ES 和 Redis 底层的 Netty 启动冲突问题
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(BbbeApplication.class, args);
    }

}
