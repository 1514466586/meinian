package com.atguigu.quartz;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/14 19:16
 */
//@Component 声明bean对象
public class JobDemo {
    public void run(){
        System.out.println("new Date() = " + new Date());
    }
}
