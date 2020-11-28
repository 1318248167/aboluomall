package com.aboluo.amall.user.mapper;

import com.aboluo.amall.api.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UserMapper extends Mapper<UmsMember> {

    List<UmsMember> selectAllUser();
}
