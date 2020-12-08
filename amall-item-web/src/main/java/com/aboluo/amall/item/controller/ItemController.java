package com.aboluo.amall.item.controller;

import com.aboluo.amall.api.bean.PmsProductSaleAttr;
import com.aboluo.amall.api.bean.PmsSkuInfo;
import com.aboluo.amall.api.bean.PmsSkuSaleAttrValue;
import com.aboluo.amall.api.service.SkuService;
import com.aboluo.amall.api.service.SpuService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    //获取sku
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap){

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        //sku对象
        modelMap.put("skuInfo",pmsSkuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrList);

        //查询当前sku的spu的其他sku集合
        Map<String,String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";
            }
            skuSaleAttrHash.put(k,v);
        }

        //将hash对象转换成json字符串
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        //将sku销售属性hash表放到页面
        modelMap.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        return "item";
    }

}
