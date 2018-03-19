package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @program:taotao
 * @description:添加商品，更新索引库监听器
 * @author:xjw
 * @create:2018-02-24 16:18
 **/
public class ItemAddlistener implements MessageListener{
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try {
            //获取消息,获取商品id
            TextMessage textMessage= (TextMessage) message;
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);
            //根据消息传递的id获取数据
            //等待，预防事务未提交
            Thread.sleep(1000);
            SearchItem searchItem = searchItemMapper.searchItemById(itemId);
            //创建文档
            SolrInputDocument solrInputFields=new SolrInputDocument();
            //向文档中添加域
            solrInputFields.addField("id",searchItem.getId());
            solrInputFields.addField("item_title",searchItem.getTitle());
            solrInputFields.addField("item_sell_point",searchItem.getSell_point());
            solrInputFields.addField("item_price",searchItem.getPrice());
            solrInputFields.addField("item_image",searchItem.getImage());
            solrInputFields.addField("item_category_name",searchItem.getCategory_name());
            solrInputFields.addField("item_desc",searchItem.getItem_desc());
            //将查询的数据更新到索引库中
            solrServer.add(solrInputFields);
            //提交
            solrServer.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
