package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.model.TreeModule;
import com.xxxx.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module ,Integer> {


    //查询所有的资源列表
    public List<TreeModule> queryAllModules();

    //查询所有的资源数据
    public List<Module> queryModuleList();

    //根据资源名称和层级查询资源数据
    Module queryModuleByName(@Param("moduleName") String moduleName, @Param("grade") Integer grade);

    //根据资源层级和URL查询资源数据
    Module queryModuleByGradeAndUrl( @Param("grade") Integer grade, @Param("url")  String url);

    //根据资源权限码查询资源数据
    Module queryModuleByOptValue(  @Param("optValue") String optValue);

    //查询指定资源是否存在子记录
    Integer queryModuleParentId(Integer id);
}