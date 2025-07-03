package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
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
 * &#064;Date:  2025/7/2 20:16
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件查询计划项列表
     *
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlansByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo =
                new PageInfo<CusDevPlan>(selectByParams(cusDevPlanQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    //添加客户开发计划项数据
    //1，参数校验 机会id 计划向内容 计划时间
    //2，设置参数的默认值
    //是否有效 创建时间 修改时间
    //3，执行添加，判断结果
    @Transactional(propagation = Propagation.REQUIRED)
    //参数校验
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        cheakCusDevPlanParams(cusDevPlan);
        //设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"开发计划添加失败！");
    }


    private void cheakCusDevPlanParams(CusDevPlan cusDevPlan) {

        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(saleChanceId==null||saleChanceMapper.selectByPrimaryKey(saleChanceId)==null,"请选择关联的营销机会");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"开发计划项不能为空");
        //计划时间 非空
        AssertUtil.isTrue(cusDevPlan.getPlanDate()==null,"计划时间不能为空");
    }

    //更新客户开发计划项数据
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(cusDevPlan.getId()==null||cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId())==null,"待更新记录不存在");
        cheakCusDevPlanParams(cusDevPlan);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"开发计划项更新失败！");
    }

    public void deleteCusDevPlan(Integer id) {

        //判断id是否为空且数据存在
        //执行删除操作，修改isValid属性
        AssertUtil.isTrue(null==id,"待删除记录不存在");
        CusDevPlan cusDevPlan=cusDevPlanMapper.selectByPrimaryKey(id);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"开发计划项删除失败！");

    }
}
