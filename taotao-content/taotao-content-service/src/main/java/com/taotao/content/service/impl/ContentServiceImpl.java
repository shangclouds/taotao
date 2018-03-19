package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyuiDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;
    @Override
    public EasyuiDatagridResult selectContentByCategory(int page,int pageSize,long contentCategoryId) {
        //1.创建对象
        TbContentExample example=new TbContentExample();
        //2.添加约束
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(contentCategoryId);
        //3.执行查询
        PageHelper.startPage(page,pageSize);
        List<TbContent> contentList = contentMapper.selectByExample(example);
        PageInfo pageInfo=new PageInfo(contentList);
        //4.返回结果
        EasyuiDatagridResult easyuiDatagridResult=new EasyuiDatagridResult();
        easyuiDatagridResult.setTotal(pageInfo.getTotal());
        easyuiDatagridResult.setRows(contentList);

        return easyuiDatagridResult;
    }

    @Override
    public TaotaoResult addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult editContent(TbContent content) {
        contentMapper.updateByPrimaryKey(content);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(String ids) {

        List<String> strings= Arrays.asList(ids.split(","));
        List<Long> idList= new ArrayList<>();
        for (String string:strings){
            idList.add(Long.valueOf(string));
        }
        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(idList);
        contentMapper.deleteByExample(example);
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long categoryId) {
        //第一步：先查询缓冲
        //添加缓冲不能影响业务逻辑
        try {

            String json = jedisClient.hget(INDEX_CONTENT, categoryId + "");

            if (StringUtils.isNotBlank(json)){
                List<TbContent> contentList = JsonUtils.jsonToList(json, TbContent.class);
                return contentList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //第二步：缓冲中不存在则向数据库中进行查询
        //创建对象
        TbContentExample example=new TbContentExample();
        //设置限制条件
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //查询结果
        List<TbContent> contentList = contentMapper.selectByExample(example);
        //第三步，若是数据是从数据库中进行查询的，则将数据加入到缓冲之中

        try {
            jedisClient.hset(INDEX_CONTENT,categoryId+"", JsonUtils.objectToJson(contentList));
        }catch (Exception e){
            e.printStackTrace();
        }
        // 第四步：返回结果
        return contentList;
    }
}
