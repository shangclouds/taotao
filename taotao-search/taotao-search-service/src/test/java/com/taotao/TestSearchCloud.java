package com.taotao;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class TestSearchCloud {
//    @Test
    public void addDocument()throws Exception{
        //创建cloudSolrServer,构造参数为zookeeper的地址list列表
        CloudSolrServer cloudSolrServer=new CloudSolrServer("192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183");
        //需要指定默认的collection
        cloudSolrServer.setDefaultCollection("collection1");
        //创建Document对象
       SolrInputDocument document=new SolrInputDocument();
        //向文档中添加域
        document.setField("id","test001");
        document.setField("item_title","测试1");
        document.setField("item_price",100);
        //将文档写入索引库
        cloudSolrServer.add(document);
              //执行提交
        cloudSolrServer.commit();

    }
}
