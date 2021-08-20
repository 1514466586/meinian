package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
   @Reference
    AddressService addressService;
    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){
      List<Address> list= addressService.findAllMaps();
        HashMap map = new HashMap();

        ArrayList<Map> nameMaps = new ArrayList<>();//标记的地址的名称
        ArrayList<Map> gridnMaps = new ArrayList<>();//标记地址的经纬度
        for (Address address : list) {
            String addressName = address.getAddressName();
            HashMap<String, String> mapName = new HashMap<>();
            mapName.put("addressName",addressName);
            nameMaps.add(mapName);
            Map<String,String> gridnMap = new HashMap<>();
            gridnMap.put("lng",address.getLng());
            gridnMap.put("lat",address.getLat());
            gridnMaps.add(gridnMap);

        }
        map.put("gridnMaps",gridnMaps);
       map.put("nameMaps",nameMaps);
       return map;
    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = addressService.findPage(queryPageBean);

        return pageResult;

    }
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){
        try {
            addressService.addAddress(address);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }

    }
}
