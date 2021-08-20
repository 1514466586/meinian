package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/13 15:03
 */
@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;


    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup);
        Integer travelGroupId = travelGroup.getId();//必须要进行主键回填
        setTravelGroupAndTravelItem(travelItemIds,travelGroupId);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
       Page page =travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());//数据封装
    }

    @Override
    public TravelGroup getById(Integer id) {

        return travelGroupDao.getById(id);
    }

    @Override
    public List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId) {

        return travelGroupDao.getTravelItemIdsByTravelGroupId(travelGroupId);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);
        travelGroupDao.delete(travelGroup.getId());
        setTravelGroupAndTravelItem(travelItemIds,travelGroup.getId());
    }

    @Override
    public void deleteById(Integer id) {
        travelGroupDao.delete(id);
        travelGroupDao.deleteById(id);
    }

    @Override
    public List<TravelGroup> findAll() {
       return  travelGroupDao.findAll();
    }

    private void setTravelGroupAndTravelItem(Integer[] travelItemIds, Integer travelGroupId) {

        if (travelItemIds!=null && travelItemIds.length>0){
            //准备dao层需要的参数,利用map集合传递数据

            for (Integer travelItemId : travelItemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("travelGroupId",travelGroupId);
                map.put("travelItemId",travelItemId);
                travelGroupDao.addTravelGroupAndTravelItem(map);

            }

        }
    }
}
