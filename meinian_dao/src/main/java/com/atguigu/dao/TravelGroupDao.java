package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/13 15:05
 */

public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void addTravelGroupAndTravelItem(Map<String, Integer> map);

    Page findPage(@Param("queryString") String queryString);

    TravelGroup getById(Integer id);

    List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId);

    void edit(TravelGroup travelGroup);

    void delete(Integer id);

    void deleteById(Integer id);

    List<TravelGroup> findAll();

    //帮助套餐查询关联数据方法
    List<TravelGroup> findTravelGroupById(Integer id);
}
