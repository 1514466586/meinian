package com.atguigu.dao;

import com.atguigu.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 18:01
 */
public interface OrderDao {
    List<Order> findOrderCountByCondition(Order order);

    void add(Order order);

    Map<String, Object> findById(Integer orderId);

    int getTodayOrderNumber(String date);

    int getTodayVisitsNumber(String date);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> map);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> map);

    List<Map<String,Object>> findHotSetmeal();
}
