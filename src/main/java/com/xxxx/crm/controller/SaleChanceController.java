package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 16:25
 */
@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    // 营销机会管理页面
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @ResponseBody
    @PostMapping("add")
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
       String userName= CookieUtil.getCookieValue(request,"userName");
       saleChance.setCreateMan(userName);
       saleChanceService.addSaleChance(saleChance);
       return success("营销机会数据添加成功");
    }

    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(){
        return "saleChance/add_update";
    }
}
