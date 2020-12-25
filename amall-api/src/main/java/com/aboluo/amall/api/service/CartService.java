package com.aboluo.amall.api.service;

import com.aboluo.amall.api.bean.OmsCartItem;

import java.util.List;

public interface CartService {

    OmsCartItem ifCartExitByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void flushCartCash(String memberId);

    List<OmsCartItem> cartList(String userId);

    void checkCart(OmsCartItem omsCartItem);
}
