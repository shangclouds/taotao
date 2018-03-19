package com.taotao.controller;

import com.taotao.common.pojo.EasyuiDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyuiDatagridResult queryContentList(int page,int rows,long categoryId){

        EasyuiDatagridResult easyuiDatagridResult = contentService.selectContentByCategory(page,rows,categoryId);
        return easyuiDatagridResult;
    }
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content){
        TaotaoResult taotaoResult = contentService.addContent(content);
        return taotaoResult;
    }
    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent content){
        TaotaoResult taotaoResult = contentService.editContent(content);
        return taotaoResult;

    }
    @RequestMapping("/content/delete")
    @ResponseBody
    public  TaotaoResult deleteContent(String ids){
        TaotaoResult taotaoResult = contentService.deleteContent(ids);
        return taotaoResult;
    }
}
