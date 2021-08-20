package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/12 17:20
 */
@Transactional//加上事务
@Service(interfaceClass = TravelItemService.class)//发布服务注册到zk中心
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //利用分页插件,开启分页功能
        PageHelper.startPage(currentPage,pageSize);//limit ?,?第一个?表示开启索引,第二个?表示查询的条数
        Page page = travelItemDao.findPage(queryString);//返回当前页数据

        return new PageResult(page.getTotal(),page.getResult());//第一个参数代表总记录数,第二个参数代表分页数据集合
    }

    @Override
    public void delete(Integer id) {
        //查询自由行关联表中是否存在关联数据,如果存在,抛出异常,不进行删除
       long count= travelItemDao.findCountByTravelitemId(id);
       if (count>0){
           //有关联数据
           throw new RuntimeException("删除自由行失败,因为存在关联数据,需要先解除关系再删除");
       }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem getById(Integer id) {
        return travelItemDao.getById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
      travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {

        return travelItemDao.findAll();
    }
}
