package com.aboluo.amall.api.service;

import com.aboluo.amall.api.bean.UmsMember;
import com.aboluo.amall.api.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getMemberReceiveAddress(String memberId);
}
