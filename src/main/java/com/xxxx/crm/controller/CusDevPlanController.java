package com.xxxx.crm.controller;

import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import com.xxxx.crm.base.BaseController;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/2 19:06
 */
@RequestMapping("/cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController{

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("index")
    public String index(HttpServletRequest  request){
        request.setAttribute("ctx", request.getContextPath());
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanPage")

    public String toCusDevPlanPage(Integer sid, HttpServletRequest request){

        request.setAttribute("ctx", request.getContextPath());
        //通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        //将对象设置到请求与中
        request.setAttribute("saleChance",saleChance);


        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 查询营销机会的计划项数据列表
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams (CusDevPlanQuery cusDevPlanQuery) {
        return cusDevPlanService.queryByParamsForTable(cusDevPlanQuery);
    }
    //添加计划项
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！");

    }

    //计划项更新
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public  String addOrUpdateCusDevPlanPage(){
        return "cusDevPlan/add_update";
    }
}
