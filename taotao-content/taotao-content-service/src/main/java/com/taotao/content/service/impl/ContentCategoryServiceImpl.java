package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<EasyUITreeNode> selectContentCategoryByParentId(long parentId) {
        //1.创建查询条件
        TbContentCategoryExample example=new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
       criteria.andParentIdEqualTo(parentId);
       //2.调用查询
        List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
        //3.格式转换
        List<EasyUITreeNode>resultList=new ArrayList<>();
        for (TbContentCategory contentCategory: contentCategoryList){
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(contentCategory.getId());
            node.setText(contentCategory.getName());
            node.setState(contentCategory.getIsParent()?"closed":"open");
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        //1.创建一个pojo对象
        TbContentCategory contentCategory=new TbContentCategory();
        //2.补全对象属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态，可选值：1（正常）、2(删除)
        contentCategory.setStatus(1);
        //排序，默认为1
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //3.将对象存入数据库
        contentCategoryMapper.insert(contentCategory);
        //判断父节点的状态
        TbContentCategory parent=contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()){
            //如果父节点是叶子节点
            parent.setIsParent(true);
            //更新父节点
            contentCategoryMapper.updateByPrimaryKeySelective(parent);
        }
        //4.返回结果

        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        //1.创建对象
        TbContentCategory contentCategory=new TbContentCategory();
        //2.补全对象属性
        contentCategory.setId(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        TaotaoResult result = TaotaoResult.ok();
        return result;
    }

    @Override
    public TaotaoResult deleteContentCategory(long id) {
        //1.从数据库获取对象
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //2.判断节点是否为父节点
        if (!contentCategory.getIsParent()){
            //3.查找该节点的兄弟节点的个数
            TbContentCategoryExample example=new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(contentCategory.getParentId());
            List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
            if (contentCategoryList.size()<=1){
                //4.查找该节点的父节点
                TbContentCategory parent=contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
               //5.将父节点改为叶子节点
                parent.setIsParent(false);
                contentCategoryMapper.updateByPrimaryKey(parent);
            }
            //5.查询当前节点的父节点下的叶子节点个数
            contentCategoryMapper.deleteByPrimaryKey(id);
        }else{
            //3.递归删除当前节点及所有子节点
            TbContentCategoryExample example=new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
           if (contentCategoryList.size()>0){
               for (TbContentCategory tbContentCategory:contentCategoryList){
                   deleteContentCategory(tbContentCategory.getId());
           }
            }
            contentCategoryMapper.deleteByPrimaryKey(id);
        }

        return TaotaoResult.ok();
    }

}
