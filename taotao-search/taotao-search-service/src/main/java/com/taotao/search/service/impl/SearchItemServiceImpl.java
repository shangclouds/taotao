package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer httpSolrServer;
    @Override
    public TaotaoResult importItemToIndex() {
        System.out.println("service开始执行++++++++++++++++++++++++"+new Date());
        try {
            //1.查询所有商品数据
            List<SearchItem> searchItemList = searchItemMapper.searchItemList();
            //2.遍历商品数据到索引库
            int i=0;
            for (SearchItem searchItem:searchItemList){
                System.out.println((i++)+"service循环++++++++++++++++++++++++"+new Date());
                //创建文档对象
                SolrInputDocument solrInputFields=new SolrInputDocument();
                //向文档中添加域
                solrInputFields.addField("id",searchItem.getId());
                solrInputFields.addField("item_title",searchItem.getTitle());
                solrInputFields.addField("item_sell_point",searchItem.getSell_point());
                solrInputFields.addField("item_price",searchItem.getPrice());
                solrInputFields.addField("item_image",searchItem.getImage());
                solrInputFields.addField("item_category_name",searchItem.getCategory_name());
                solrInputFields.addField("item_desc",searchItem.getItem_desc());
                //把文档写入索引库
                httpSolrServer.add(solrInputFields);
            }
            //3.提交
            httpSolrServer.commit();
            System.out.println(searchItemList.size()+"service执行完成++++++++++++++++++++++++"+new Date());
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,"数据导入索引库失败");
        }
        //返回添加成功
        return TaotaoResult.ok();
    }


}
