package com.aboluo.amall.manage.service.impl;

import com.aboluo.amall.api.bean.PmsBaseAttrInfo;
import com.aboluo.amall.api.bean.PmsBaseAttrValue;
import com.aboluo.amall.api.bean.PmsBaseSaleAttr;
import com.aboluo.amall.api.service.AttrService;
import com.aboluo.amall.manage.mapper.PmsBaseAttrInfoMapper;
import com.aboluo.amall.manage.mapper.PmsBaseAttrValueMapper;
import com.aboluo.amall.manage.mapper.PmsBaseSaleAttrMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    //查询三级商品属性名信息
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        //添加属性值
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {

            List<PmsBaseAttrValue> pmsBaseAttrValues = new ArrayList<>();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);

            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }


        return pmsBaseAttrInfos;
    }

    //查询三级商品属性值信息
    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    //保存商品属性值
    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        //判断是否有id
        if (StringUtils.isBlank(pmsBaseAttrInfo.getId())){ //id 为空 保存

            //添加平台属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);//保存三级商品属性名信息 返回主键（实体类有主键生成策略）
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue); //保存三级商品属性值信息
            }
        }else { //id 非空 修改

            //修改属性
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);

            //删除属性值
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

            //修改属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                if (pmsBaseAttrValue.getAttrId()==null){ //判断是否有 AttrId
                    pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                }
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }

            //添加修改属性

        }

        return "success";

    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {

        //将set转成字符串
        String valeIdStr = StringUtils.join(valueIdSet,",");//21，22，23

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrValueMapper.selectAttrValueListByValueId(valeIdStr);

        return pmsBaseAttrInfos;
    }
}
