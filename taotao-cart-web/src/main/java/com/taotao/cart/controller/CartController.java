package com.taotao.cart.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @program:taotao
 * @description:购物车控制器
 * @author:xjw
 * @create:2018-03-03 10:54
 **/
@Controller
public class CartController {

    @Value("${TT_CART}")
    private String TT_CART;
    @Value("${CART_EXPIRE}")
    private int CART_EXPIRE;
    @Autowired
    private ItemService itemService;
    @RequestMapping("/cart/add/{itemId}")
    public String addCartItem(@PathVariable Long itemId,
                              @RequestParam(defaultValue = "1") Integer num,
                              HttpServletRequest request, HttpServletResponse response){
        //获取购物车商品列表
        List<TbItem> itemList = getCartItemList(request);
        boolean flag=false;//用来判断在购物车中是否商品已经存在
        //判断商品在购物车中是否存在
        for (TbItem item:itemList){
            //如果存在，则商品数量相加
            if (item.getId()==itemId.longValue()){//long是对象，需要比较值，用longValue()
                item.setNum(item.getNum()+num);
                flag=true;
                break;
            }

        }
        //如果不存在，则添加一个新的商品
        if (!flag){
            //调用服务获取商品
            TbItem item = itemService.selectItemById(itemId);
            //设置购买的商品数量
            item.setNum(num);

            //解决图片显示问题
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)){
                String[] images = image.split(",");
                image=images[0];
            }
            //将商品添加到购物车中
            itemList.add(item);
        }
        //把购物车列表加入到cookie中
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(itemList),CART_EXPIRE,true);
        //返回添加成功页面
        return "cartSuccess";
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
    /**
     * 展示购物车
     * **/
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request){
        //从cookie中获取购物车列表
        List cartItemList = getCartItemList(request);
        //将购物车列表数据返回到页面中
        request.setAttribute("cartList",cartItemList);
        //返回视图
        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
       //从cookie中获取购物车信息
        List<TbItem> cartList = getCartItemList(request);
        //查找到对应的商品，更新商品的数量
        for(TbItem item:cartList){
            if (item.getId()==itemId.longValue()){
                //更新商品的数量
                item.setNum(num);
                break;
            }
        }
        //将购物车列表写入到cookie中
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(cartList),
                CART_EXPIRE,true);

        //返回结果
        return TaotaoResult.ok();
    }

    //删除购物车
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(HttpServletResponse response,HttpServletRequest request,@PathVariable Long itemId){
       //从cookie中取出购物车列表
        List<TbItem> cartList = getCartItemList(request);
        for (TbItem item:cartList){
            if (item.getId()==itemId.longValue()){
                cartList.remove(item);
                break;
            }
        }
        //将新的购物车列表写入到cookie中
        CookieUtils.setCookie(request,response,TT_CART,JsonUtils.objectToJson(cartList),
                CART_EXPIRE,true);

        //重定向
        return "redirect:/cart/cart.html";
    }
}
