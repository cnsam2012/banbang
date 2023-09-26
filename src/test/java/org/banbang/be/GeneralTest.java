package org.banbang.be;

import org.banbang.be.util.CommunityUtil;

public class GeneralTest {
    public static void main(String[] args) {
        System.out.println(CommunityUtil.generateUUID().substring(0, 5));
    }
}
