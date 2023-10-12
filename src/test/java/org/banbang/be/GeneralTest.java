package org.banbang.be;

import org.banbang.be.util.BbUtil;

public class GeneralTest {
    public static void main(String[] args) {
        System.out.println(BbUtil.generateUUID().substring(0, 5));
    }
}
