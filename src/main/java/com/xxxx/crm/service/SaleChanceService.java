package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 16:23
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {


    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {


        Map<String,Object> map = new HashMap<>();

        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());



        return map;
    }

    @Transactional
    public void addSaleChance(SaleChance saleChance) {
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new java.util.Date());
        saleChance.setUpdateDate(new java.util.Date());
        saleChanceMapper.insertSelective(saleChance);
    }

}
