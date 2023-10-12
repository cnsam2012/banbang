package org.banbang.be;

import lombok.extern.slf4j.Slf4j;
import org.banbang.be.util.BbUtil;
import org.junit.jupiter.api.Test;

@Slf4j
public class BbbeExeClass {

    public static void main(String[] args) {
        System.out.println("HLW");
        System.out.println(BbUtil.generateUUID());
    }

    @Test
    public void slf4jTest() {
        log.info("this is a logn info");
        log.info("{}", "doneododododododo");
    }
}
