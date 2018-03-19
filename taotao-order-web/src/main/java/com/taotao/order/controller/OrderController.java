package com.taotao.order.controller;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @program:taotao
 * @description:订单控制器
 * @author:xjw
 * @create:2018-03-04 13:06
 **/
@Controller
public class OrderController{
    @Value("${TT_CART}")
    private String TT_CART;
    @RequestMapping("order/order-cart")
    public String showOrderItem(HttpServletRequest request){
        //用户必须要登录
        //获取用户的id
        TbUser user = (TbUser) request.getAttribute("user");
        //根据id获取用户的收货地址，在前台中进行展示
        //从cookie中获取购物车商品列表传递到页面
        List cartList = getCartItemList(request);
        request.setAttribute("cartList",cartList);
        //返回逻辑视图
        return "order-cart";
    }

    private List getCartItemList(HttpServletRequest request){
        //从cookie找那个获取购物车信息
        String json = CookieUtils.getCookieValue(request, TT_CART, true);
        //如果没有购物车信息
        if (!StringUtils.isNotBlank(json)){
            return new ArrayList<TbItem>();
        }
        //cookie中存在购物车信息
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }
}
