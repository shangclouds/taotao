package com.taotao.content.service;

import com.taotao.common.pojo.EasyuiDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    //查询
    EasyuiDatagridResult selectContentByCategory(int page,int pageSize,long contentCategoryId);

    //添加
    TaotaoResult addContent(TbContent content);
    //编辑
    TaotaoResult editContent(TbContent content);
    //删除
    TaotaoResult deleteContent(String ids);
    //根据类别id实现前台的动态展示
    List<TbContent> getContentByCid(long categoryId);
}
