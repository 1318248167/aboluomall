package com.aboluo.amall.search.controller;

import com.aboluo.amall.api.bean.PmsSearchSkuInfo;
import com.aboluo.amall.api.bean.PmsSkuInfo;
import com.aboluo.amall.api.service.SkuService;
import com.alibaba.dubbo.config.annotation.Reference;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.collections.list.PredicatedList;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DataAdditionController {

    @Reference
    SkuService skuService;


    @Autowired
    JestClient jestClient;

    //向es添加数据
    @RequestMapping("addToElasticSearch")
    @ResponseBody
    public String getAllSku() throws InvocationTargetException, IllegalAccessException, IOException {


        List<PmsSkuInfo> pmsSkuInfos = new ArrayList<>();

        pmsSkuInfos = skuService.getAllSku("61");

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {

            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index build = new Index.Builder(pmsSearchSkuInfo).index("amall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            jestClient.execute(build);
        }

        return "Successfully added to elasticSearch";
    }


    @RequestMapping("query")
    @ResponseBody
    public String query() throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter -> term(s)
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","43");
        boolQueryBuilder.filter(termQueryBuilder);
        //must -> match
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","OPPO");
        boolQueryBuilder.must(matchQueryBuilder);
        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //highlight
        searchSourceBuilder.highlight(null);

        String s = searchSourceBuilder.toString();

        System.err.println(s);


        List<PmsSearchSkuInfo> pmsSearchSkuInfos  =new ArrayList<>();

        Search search = new Search.Builder(s).addIndex("amall").addType("PmsSkuInfo").build();

        SearchResult execute = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        System.out.println(pmsSearchSkuInfos.size());
        return "success";
    }
}
