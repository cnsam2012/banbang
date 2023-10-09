package org.banbang.be;

import lombok.extern.slf4j.Slf4j;
import org.banbang.be.dao.DiscussPostMapper;
import org.banbang.be.util.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
public class BbbeExeClass {

    public static void main(String[] args) {
        System.out.println("HLW");
        System.out.println(CommunityUtil.generateUUID());
    }

    @Test
    public void slf4jTest() {
        log.info("this is a logn info");
        log.info("{}", "doneododododododo");
    }
}
