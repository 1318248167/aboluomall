package com.aboluo.amall.user.service.impl;

import com.aboluo.amall.api.bean.UmsMember;
import com.aboluo.amall.api.bean.UmsMemberReceiveAddress;
import com.aboluo.amall.api.service.UserService;
import com.aboluo.amall.user.mapper.UmsMemberReceiveAddressMapper;
import com.aboluo.amall.user.mapper.UserMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMemberList = userMapper.selectAllUser();
//        通用mapper
//        List<UmsMember> umsMemberList = userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getMemberReceiveAddress(String memberId) {

        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
//        example的使用
//        Example example = new Example(UmsMemberReceiveAddress.class);
//        example.createCriteria().andEqualTo("memberId",memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = umsMemberReceiveAddressMapper.selectByExample(example);
        return umsMemberReceiveAddressList;
    }
}














