package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/17 14:23
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {

        return memberDao.getMemberByTelephone(telephone);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {

        List<Integer> list = new ArrayList<>();
        if (months!=null && months.size()>0){
            for (String month : months) {
                String lastDayOfMonth = DateUtils.getLastDayOfMonth(month);
                int count= memberDao.findMemberCountByMonth(lastDayOfMonth);
               list.add(count);
            }
        }
        return list;
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}
