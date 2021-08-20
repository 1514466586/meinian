package com.atguigu.controller;

import com.atguigu.contant.MessageConstant;
import com.atguigu.contant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 17:25
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    //手机快速登录时发送手机验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        try {
            //1.生成四位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //2.发送验证码到手机号
            SMSUtils.sendShortMessage(telephone,code.toString());
            //3.验证码存到redis便于后期验证
            //并且验证码5分钟有效
            Jedis resource = jedisPool.getResource();
            resource.setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,String.valueOf(code));
            System.out.println("telephone = " + telephone);
            System.out.println("code = " + code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }

}
