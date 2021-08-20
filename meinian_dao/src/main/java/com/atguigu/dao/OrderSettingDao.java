package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/15 16:14
 */
public interface OrderSettingDao {
    void add(OrderSetting orderSetting);

    int findOrderSettingByOrderDate(Date orderDate);

    void edit(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map map);

   

    OrderSetting getOrderSettingByOrderDate(Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
