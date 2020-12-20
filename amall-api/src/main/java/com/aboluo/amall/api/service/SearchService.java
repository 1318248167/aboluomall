package com.aboluo.amall.api.service;

import com.aboluo.amall.api.bean.PmsSearchParam;
import com.aboluo.amall.api.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
