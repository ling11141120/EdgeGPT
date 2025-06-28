package com.xxxx.crm.dao;

import com.xxxx.crm.vo.SaleChance;

public interface SaleChanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SaleChance record);

    int insertSelective(SaleChance record);

    SaleChance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SaleChance record);

    int updateByPrimaryKey(SaleChance record);
}