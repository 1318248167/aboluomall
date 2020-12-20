package com.aboluo.amall.search.controller;

import com.aboluo.amall.api.bean.*;
import com.aboluo.amall.api.service.AttrService;
import com.aboluo.amall.api.service.SearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    //访问首页
    @RequestMapping("index")
    public String index() {
        return "index";
    }


    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {

        //调用搜索服务 返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);

        //抽取结果属性集包含的平台属性集合
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }

        //TODO 待处理当只有一件商品时的属性及属性列表的展示（应该不展示）
        //根据valueId将属性列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        //对平台属性进一步处理 去掉当前条件中valueId所在的属性组
        String[] delValueIds = pmsSearchParam.getValueId();
        if (delValueIds != null) {
            //面包屑
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            for (String delValueId : delValueIds) { //如果delValueIds不为空 则每一个delValueId都有一个面包屑生成
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();//使用迭代器删除
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, delValueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();
                        //查找面包屑属性名
                        if (delValueId.equals(valueId)) {
                            //删除该属性值所在的属性组
                            iterator.remove();
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
                //添加面包屑
                modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
            }
        }

        //拼接url
        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            //关键字
            modelMap.put("keywords", keyword);
        }


        return "list";
    }

    //拼接url
    private String getUrlParam(PmsSearchParam pmsSearchParam, String... delValue) {

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueIds = pmsSearchParam.getValueId();

        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {

            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "keyword=" + keyword;
        }

        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam = urlParam + "&";
            }
            urlParam = urlParam + "catalog3Id=" + catalog3Id;
        }

        //拼接面包屑url
        if (delValue.length != 0) {
            if (valueIds != null) {
                for (String valueId : valueIds) {
                    for (String value : delValue) {
                        if (!valueId.equals(value)) {
                            urlParam += "&valueId=" + valueId;
                        }
                    }
                }
            }
        }

        //拼接路径url
        if (delValue.length == 0) {
            if (valueIds != null) {
                for (String valueId : valueIds) {
                    urlParam += "&valueId=" + valueId;
                }
            }
        }

        return urlParam;
    }
}




