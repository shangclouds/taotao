package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {

    /**
     * 通过父节点查找
     * */
     List<EasyUITreeNode> getItemCatByParentId(long parentId);

}
