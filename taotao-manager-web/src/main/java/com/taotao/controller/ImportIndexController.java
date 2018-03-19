package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImportIndexController {
    @Autowired
    private SearchItemService searchItemService;
    @RequestMapping("/search/index/import")
    @ResponseBody
    public TaotaoResult importIndex(){
        System.out.println("正常执行++++++++++++++++++++++++++++++++++");
        TaotaoResult taotaoResult = searchItemService.importItemToIndex();
        System.out.println("service执行完成++++++++++++++++++++++++++++++");
        return taotaoResult;
    }
}
