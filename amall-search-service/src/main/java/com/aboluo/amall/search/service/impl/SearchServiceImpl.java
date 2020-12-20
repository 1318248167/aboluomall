package com.aboluo.amall.search.service.impl;

import com.aboluo.amall.api.bean.PmsSearchParam;
import com.aboluo.amall.api.bean.PmsSearchSkuInfo;
import com.aboluo.amall.api.bean.PmsSkuAttrValue;
import com.aboluo.amall.api.service.SearchService;
import com.alibaba.dubbo.config.annotation.Service;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;


    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {

        String searchDsl  = getSearchDsl(pmsSearchParam);

        System.err.println(searchDsl);

        //用api进行复杂查询
        List<PmsSearchSkuInfo> pmsSearchSkuInfos  =new ArrayList<>();

        Search search = new Search.Builder(searchDsl).addIndex("amall").addType("PmsSkuInfo").build();

        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            Map<String, List<String>> highlight = hit.highlight;
            if (highlight!=null){
                //设置高亮
                String skuName = highlight.get("skuName").get(0);
                source.setSkuName(skuName);
            }
            pmsSearchSkuInfos.add(source);
        }

        return pmsSearchSkuInfos;
    }

    private String getSearchDsl(PmsSearchParam pmsSearchParam) {

        //属性值id列表
        String[] skuAttrValueIdList = pmsSearchParam.getValueId();

        //关键字
        String keyword = pmsSearchParam.getKeyword();

        //三级分类id
        String catalog3Id = pmsSearchParam.getCatalog3Id();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter -> term(s)

        if (StringUtils.isNotBlank(catalog3Id)){ //三级分类id不为空
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        if (skuAttrValueIdList!=null){ //属性值列表不为空
            for (String pmsSkuAttrValueId : skuAttrValueIdList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",pmsSkuAttrValueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //must -> match
        if (StringUtils.isNotBlank(keyword)){ //关键字不为king
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //from 分页
        searchSourceBuilder.from(0);

        //size 分页
        searchSourceBuilder.size(20);

        //highlight 高亮

        HighlightBuilder highlightBuilder  =new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlight(highlightBuilder);

        //sort 排序

//        searchSourceBuilder.sort("id",SortOrder.DESC);


        return searchSourceBuilder.toString();
    }
}
