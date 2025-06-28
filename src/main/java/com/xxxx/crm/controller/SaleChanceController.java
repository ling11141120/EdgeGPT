package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.SaleChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 16:25
 */
@Controller
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;
}
