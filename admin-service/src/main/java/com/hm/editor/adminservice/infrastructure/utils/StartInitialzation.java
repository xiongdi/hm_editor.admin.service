package com.hm.editor.adminservice.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * @desc:项目启动后初始化部分数据
 */
public abstract class StartInitialzation implements CommandLineRunner {

    protected Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) throws Exception {
        init();
    }

    public abstract void init();
}
