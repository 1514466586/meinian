package com.atguigu.dao;

import com.atguigu.pojo.Member;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 18:02
 */
public interface MemberDao {
    Member getMemberByTelephone(String telephone);

    void add(Member member);

    int findMemberCountByMonth(String month);
    int getTodayNewMember(String date);
    int getTotalMember();
    int getThisWeekAndMonthNewMember(String date);

}
