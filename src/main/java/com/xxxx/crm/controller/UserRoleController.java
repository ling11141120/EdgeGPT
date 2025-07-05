package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/4 10:33
 */
@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;
}
