package com.aboluo.amall.api.service;

import com.aboluo.amall.api.bean.PmsBaseSaleAttr;
import com.aboluo.amall.api.bean.PmsProductImage;
import com.aboluo.amall.api.bean.PmsProductInfo;
import com.aboluo.amall.api.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {

    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
