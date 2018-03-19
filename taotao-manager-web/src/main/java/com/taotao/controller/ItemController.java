package com.taotao.controller;

import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.EasyuiDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/selectItemById/{itemId}")
    @ResponseBody
    public TbItem selectItemById(@PathVariable Long itemId){
        TbItem item=itemService.selectItemById(itemId);
        return item;
    }
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyuiDatagridResult selectItem( int page,int rows){
       System.out.println(page+"bbbbbbbbbbbbbbbbbbbb"+rows);
        return itemService.selectAllItem(page,rows);
    }
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(TbItem item,String desc){
        TaotaoResult result = itemService.addItem(item, desc);
        return  result;
    }
}
