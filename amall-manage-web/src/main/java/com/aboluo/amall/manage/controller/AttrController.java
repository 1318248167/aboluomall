package com.aboluo.amall.manage.controller;

import com.aboluo.amall.api.bean.PmsBaseAttrInfo;
import com.aboluo.amall.api.bean.PmsBaseAttrValue;
import com.aboluo.amall.api.bean.PmsBaseSaleAttr;
import com.aboluo.amall.api.service.AttrService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;

    //获取商品属性名信息
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }

    //获取商品属性值列表
    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        List<PmsBaseAttrValue> attrValueList = attrService.getAttrValueList(attrId);
        return attrValueList;
    }

    //保存属性信息
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return success;
    }


    //查询基础销售属性
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){

        final List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;

    }
}
