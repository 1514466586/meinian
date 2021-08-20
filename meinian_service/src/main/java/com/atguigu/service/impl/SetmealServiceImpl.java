package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.contant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/14 11:38
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired(required = false)
    private JedisPool jedisPool;

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        //1.保存套餐
        setmealDao.add(setmeal);//需要主键回填
        Integer setmealId = setmeal.getId();
        //2.保存关联数据
        setSetmealAndTravelGrop(travelgroupIds, setmealId);
        //解决垃圾图片问题
        //图片名字保存到redis
        String pic = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }


    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = setmealDao.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List getSetmeal() {
      List list=  setmealDao.getSetmeal();
        return list;
    }

    @Override
    public List<Map> getSetmealReport() {

        return setmealDao.getSetmealReport();
    }

    @Override
    public Setmeal getSetmealById(Integer id) {

        return setmealDao.getSetmealById(id);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);

    }

    private void setSetmealAndTravelGrop(Integer[] travelgroupIds, Integer setmealId) {
        if (travelgroupIds != null && travelgroupIds.length > 0) {
            for (Integer travelgroupId : travelgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("travelgroupId", travelgroupId);
                map.put("setmealId", setmealId);
                setmealDao.addSetmealAndTravelGrop(map);
            }
        }
    }
}
