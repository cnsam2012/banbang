package org.banbang.be.service;

import org.banbang.be.dao.DiscussPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TEST_MyService {

    @Autowired
    private DiscussPostMapper dpm;

    public void a() {
        System.out.println(dpm.selectDiscussPosts(0, 0, 10, 1));
    }
}
