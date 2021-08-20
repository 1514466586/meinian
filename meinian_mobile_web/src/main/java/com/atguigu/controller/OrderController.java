package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.contant.MessageConstant;
import com.atguigu.contant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 17:59
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    JedisPool jedisPool;

    /**
     * @description: 由于页面表单数据来自多张表的数据, 不方便用pojo对象接收, 接收不完整
     */
    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map map) {
        try {
            System.out.println("map = " + map);
            //获取用户信息
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            String redisCode = jedisPool.getResource().get((telephone + RedisMessageConstant.SENDTYPE_ORDER));
            if (redisCode == null || !redisCode.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            Result result = orderService.saveOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }
    /**
     * @description:
     * @param: id 订单id
     * @return: com.atguigu.entity.Result
     * @author 15144
     * @date: 2021/8/17 10:24
     */

    @RequestMapping("/findById")
    public Result findById(Integer orderId) {
       Map map= orderService.findById(orderId);
       return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }


}
