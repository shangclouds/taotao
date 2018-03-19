package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> selectContentCategory(@RequestParam(value = "id",defaultValue = "0")long parentId){

        List<EasyUITreeNode> easyUITreeNodes = contentCategoryService.selectContentCategoryByParentId(parentId);
        return easyUITreeNodes;
    }
    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCategory(long parentId, String name){
        TaotaoResult taotaoResult = contentCategoryService.addContentCategory(parentId, name);
        return taotaoResult;
    }
    @RequestMapping("/content/category/update")
    @ResponseBody
    public TaotaoResult updateContentCategory(long id,String name){
        return  contentCategoryService.updateContentCategory(id,name);
    }
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public TaotaoResult deleteContentCategory(long id){
        TaotaoResult result = contentCategoryService.deleteContentCategory(id);
        return  result;
    }
}
