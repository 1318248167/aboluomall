package com.aboluo.amall.manage.service.impl;

import com.aboluo.amall.api.bean.PmsProductImage;
import com.aboluo.amall.api.bean.PmsProductInfo;
import com.aboluo.amall.api.bean.PmsProductSaleAttr;
import com.aboluo.amall.api.bean.PmsProductSaleAttrValue;
import com.aboluo.amall.api.service.SpuService;
import com.aboluo.amall.manage.mapper.PmsProductImageMapper;
import com.aboluo.amall.manage.mapper.PmsProductInfoMapper;
import com.aboluo.amall.manage.mapper.PmsProductSaleAttrMapper;
import com.aboluo.amall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;


    //获取商品spu列表
    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);

        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return pmsProductInfos;
    }

    //保存商品spu属性
    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        //保存商品spu属性
        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        //保存商品图片属性
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        //保存商品销售属性
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            //保存商品销售属性值
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttr.getSpuSaleAttrValueList()) {
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
        }

    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {

            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());//销售属性id是系统字典表id 不是属性表id

            List<PmsProductSaleAttrValue> PmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

            productSaleAttr.setSpuSaleAttrValueList(PmsProductSaleAttrValues);

        }
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }


    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);

        return pmsProductSaleAttrs;
    }


}
