package com.aboluo.amall.api.service;

import com.aboluo.amall.api.bean.PmsBaseAttrInfo;
import com.aboluo.amall.api.bean.PmsBaseAttrValue;
import com.aboluo.amall.api.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
    //获取商品属性名列表
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    //获取商品属性值列表
    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
