package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品分类管理
 * */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;
    /**
     * 根据父节点查询列表
     * */
    @Override
    public List<EasyUITreeNode> getItemCatByParentId(long parentId) {
        TbItemCatExample itemCatExample=new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = itemCatExample.createCriteria();
       //设置父节点id
        criteria.andParentIdEqualTo(parentId);
        //进行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(itemCatExample);
        //将查询结果装换成EasyUITreeNode
        List<EasyUITreeNode> resultList=new ArrayList<>();
        for (TbItemCat tbItemCat:list){
            EasyUITreeNode easyUITreeNode=new EasyUITreeNode();
            easyUITreeNode.setId(tbItemCat.getId());
            easyUITreeNode.setText(tbItemCat.getName());
            easyUITreeNode.setState(tbItemCat.getIsParent()?"closed":"open");
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }
}
