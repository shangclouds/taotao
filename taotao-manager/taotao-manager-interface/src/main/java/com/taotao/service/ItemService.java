package com.taotao.service;

import com.taotao.common.pojo.EasyuiDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
    TbItem selectItemById(Long itemId);

    EasyuiDatagridResult selectAllItem(int page,int rows);

    TaotaoResult addItem(TbItem item, String desc);
    TbItemDesc getItemDescById(Long itemId);
}
