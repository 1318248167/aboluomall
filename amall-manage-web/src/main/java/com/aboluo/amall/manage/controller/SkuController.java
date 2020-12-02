package com.aboluo.amall.manage.controller;

import com.aboluo.amall.api.bean.PmsSkuInfo;
import com.aboluo.amall.api.service.SkuService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class SkuController {

    @Reference
    SkuService skuService;


    //保存sku信息
    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){

        //将spuId封装给productId
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        //处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if (StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }

        skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
