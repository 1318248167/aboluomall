package com.aboluo.amall.user.controller;

import com.aboluo.amall.api.bean.UmsMember;
import com.aboluo.amall.api.bean.UmsMemberReceiveAddress;
import com.aboluo.amall.api.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Reference
    UserService userService;

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsMemberList = userService.getAllUser();

        return umsMemberList;
    }

    @RequestMapping("getMemberReceiveAddress")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getMemberReceiveAddress(String memberId){

        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = userService.getMemberReceiveAddress(memberId);

        return umsMemberReceiveAddressList;
    }
}
