package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> selectContentCategoryByParentId(long parentId);
    TaotaoResult addContentCategory(long parentId, String name);
    TaotaoResult updateContentCategory(long id,String name);
    TaotaoResult deleteContentCategory(long id);
}
