package com.aboluo.amall.search;

import com.aboluo.amall.api.bean.PmsSearchSkuInfo;
import com.aboluo.amall.api.bean.PmsSkuInfo;
import com.aboluo.amall.api.service.SkuService;
import io.searchbox.client.JestClient;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AmallSearchServiceApplicationTests {


    @Autowired
    JestClient jestClient;

    @Reference
    SkuService skuService;

    @Test
    public void contextLoads() throws InvocationTargetException, IllegalAccessException {

        List<PmsSkuInfo> pmsSkuInfos = new ArrayList<>();

        pmsSkuInfos = skuService.getAllSku("61");

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {

            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();

            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfos);

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }

    }

}
