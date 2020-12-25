package com.aboluo.amall.cart.controller;


import com.aboluo.amall.api.bean.OmsCartItem;
import com.aboluo.amall.api.bean.PmsSkuInfo;
import com.aboluo.amall.api.service.CartService;
import com.aboluo.amall.api.service.SkuService;
import com.aboluo.amall.util.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping("checkCart")
    public String checkCart(String isChecked,String skuId,HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        String memberId = "1";
        //调用服务 修改状态
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);
        cartService.checkCart(omsCartItem);
        //将最新数据从缓存中查出 渲染给内嵌页面

        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        modelMap.put("cartList",omsCartItems);

        //被勾选的商品总额、
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        return "cartListInner";
    }


    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        List<OmsCartItem> omsCartItems  =new ArrayList<>();

        String memberId = "1";
        if (StringUtils.isNotBlank(memberId)){
            //已经登陆 查询db
            omsCartItems = cartService.cartList(memberId);
        }else {
            //没有登陆 查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);
            }
        }

        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));//计算单件商品总价
        }

        modelMap.put("cartList",omsCartItems);
        //被勾选的商品总额、
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        return "cartList";
    }

    //获取总价
    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {

        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if (omsCartItem.getIsChecked().equals("1")){

                totalAmount = totalAmount.add(totalPrice);
            }
        }
        return totalAmount;
    }


    @RequestMapping("addToCart")
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response){

        List<OmsCartItem> omsCartItems = new ArrayList<>();

        //调用商品服务查询商品信息
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId);

        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("1111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));


        //判断用户是否登陆

        String memberId = "1";

        if (StringUtils.isBlank(memberId)){
            //用户没有登陆

            //获取cookie数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);

            if (StringUtils.isNotBlank(cartListCookie)){//cookie不为空
                //cookie原有的购物车数据
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);//解析cookie
                //判断添加的购物车数据是否在cookie中存在
                boolean exit = ifCartExit(omsCartItems,omsCartItem);
                if (exit){
                    //之前添加过 更新cookie购物车添加数量
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));//更新数量
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));//更新价格
                        }
                    }
                }else {
                    //之前未添加过 新增当前购物车
                    omsCartItems.add(omsCartItem);
                }
                omsCartItems.add(omsCartItem);//添加购物车数据
            }else {//cookie为空
                omsCartItems.add(omsCartItem);//添加购物车数据
            }

            //将购物车数据放入cookie 更新cookie
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(omsCartItems),60*60*72,true);
        }else {
            //用户已经登陆
            //查询数据库是否有购物车数据
            OmsCartItem omsCartItemFromDb = cartService.ifCartExitByUser(memberId,skuId);

            if (omsCartItemFromDb == null){ //该用户没有添加过当前商品
                omsCartItem.setMemberId(memberId);//添加memberId
                omsCartItem.setMemberNickname("testName");
                omsCartItem.setQuantity(new BigDecimal(quantity));
                cartService.addCart(omsCartItem);//插入数据库
            }else {
                //该用户添加过当前商品
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity().add(omsCartItem.getQuantity()));//设置商品数量
                cartService.updateCart(omsCartItemFromDb);//更新数据库
            }

            //同步缓存
            cartService.flushCartCash(memberId);


        }


        return "redirect:/success.html";
    }

    private boolean ifCartExit(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        boolean b = false;
        for (OmsCartItem cartItem : omsCartItems) {
            String productSkuId = cartItem.getProductSkuId();
            if (productSkuId.equals(omsCartItem.getProductSkuId()));
            b = true;
        }

        return b;
    }
}
