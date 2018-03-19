package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program:taotao
 * @description:商品详情控制器
 * @author:xjw
 * @create:2018-02-27 13:22
 **/
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable long itemId, Model model){
        //查询商品基本信息
        TbItem tbItem = itemService.selectItemById(itemId);
        Item item=new Item(tbItem);
        //查询商品详情
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        //把数据返回到页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }
}
