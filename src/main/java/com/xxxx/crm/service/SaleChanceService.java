package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 16:23
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {


    @Resource
    private SaleChanceMapper saleChanceMapper;

}
