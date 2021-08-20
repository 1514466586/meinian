package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/14 11:39
 */
public interface SetmealDao {
    void addSetmealAndTravelGrop(HashMap<String, Integer> map);

    void add(Setmeal setmeal);


    Page findPage(String queryString);

    List getSetmeal();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map> getSetmealReport();
}
