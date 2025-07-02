package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    //参数校验，添加营销机会
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {
       checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
       saleChance.setIsValid(1);
       saleChance.setCreateDate(new Date());
       saleChance.setUpdateDate(new Date());
       if (StringUtils.isBlank(saleChance.getAssignMan())){
           saleChance.setState(StateStatus.UNSTATE.getType());
           saleChance.setAssignTime( null);
           saleChance.setDevResult(DevResult.UNDEV.getStatus());

       }else {

           saleChance.setState(StateStatus.STATED.getType());
           saleChance.setAssignTime(new Date());
           saleChance.setDevResult(DevResult.DEVING.getStatus());

       }
       AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"添加失败");
    }

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {

        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"手机号不能为空");
        AssertUtil.isTrue(PhoneUtil.isMobile(linkPhone),"手机号格式不正确");
    }

}
