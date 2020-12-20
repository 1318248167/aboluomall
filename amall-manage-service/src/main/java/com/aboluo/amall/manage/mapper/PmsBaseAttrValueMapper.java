package com.aboluo.amall.manage.mapper;

import com.aboluo.amall.api.bean.PmsBaseAttrInfo;
import com.aboluo.amall.api.bean.PmsBaseAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrValueMapper extends Mapper<PmsBaseAttrValue> {
    List<PmsBaseAttrInfo> selectAttrValueListByValueId(@Param("valeIdStr") String valeIdStr);
}
