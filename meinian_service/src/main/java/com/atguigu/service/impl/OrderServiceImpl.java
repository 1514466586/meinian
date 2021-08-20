package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.contant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 18:00
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;

    @Override
    public Map<String,Object> findById(Integer orderId) {
        try {
            Map<String,Object> map=  orderDao.findById(orderId);
            Date orderDate = (Date) map.get("orderDate");

            map.put("orderDate",DateUtils.parseDate2String(orderDate));
            return orderDao.findById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result saveOrder(Map map) {
        //1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
        String orderDate = (String) map.get("orderDate");
        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));


        Date date = null;
        try {
            date = DateUtils.parseString2Date(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);
        //2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
        if (orderSetting == null) {//预约设置不存在
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        } else if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3. 判断是否是会员(根据手机号码查询t_member)
        //        - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
        //        - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
        String telephone = (String) map.get("telephone");
        Member member = memberDao.getMemberByTelephone(telephone);
        if (member == null) {
            //不存在就快速注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);//要进行主键回填

        } else {
            //检查是否重复预约
            Order orderParam = new Order();
            orderParam.setOrderDate(date);
            orderParam.setSetmealId(setmealId);
            orderParam.setMemberId(member.getId());
            List<Order> orderList = orderDao.findOrderCountByCondition(orderParam);
            if (orderList !=null && orderList.size()>0){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }

        }
        //4.进行预约
        //        - 向t_order表插入一条记录
        //        - t_ordersetting表里面预约的人数reservations+1
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderType("微信预约");
        order.setOrderStatus("未出游");
        order.setSetmealId(setmealId);
        orderDao.add(order);//要主键回填
       orderSetting.setReservations(orderSetting.getReservations()+1);

       orderSettingDao.editReservationsByOrderDate(orderSetting);


        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }
}
