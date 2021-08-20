package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 18:00
 */
public interface OrderService {
    Result saveOrder (Map map);

    Map findById(Integer orderId);
}
