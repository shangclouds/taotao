package com.taotao;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import javax.naming.directory.SearchResult;
import java.util.List;
import java.util.Map;

public class TestSolrJ {
//    @Test
    public void testAddDocument() throws Exception{
        //创建SolrServer对象，创建一个HttpSolrServer对象
        //需要制定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8079/solr/collection1");
        //创建一个文档对象SolrServerDocument
        SolrInputDocument solrDocument=new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
        solrDocument.addField("id","test001");
        solrDocument.addField("item_title","测试商品1");
        solrDocument.addField("item_price",1000);
        //把文档对象写入索引库
        solrServer.add(solrDocument);
        //提交
        solrServer.commit();
    }
//    @Test
    public void deleteDocumentById()throws Exception{
        //创建SolrServer对象，创建一个HttpSolrServer对象
        //需要制定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8079/solr/collection1");
        solrServer.deleteById("test001");
        solrServer.commit();

    }
//    @Test
    public void deleteDocumentByQuery()throws Exception{
        //创建SolrServer对象，创建一个HttpSolrServer对象
        //需要制定solr服务的url
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8079/solr/collection1");
        solrServer.deleteByQuery("id:test002");
        solrServer.commit();
    }
    //查询
    @Test
    public void queryDocument()throws Exception{
        //1.创建Solrserver对象
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.128:8079/solr/collection1");
        //2.创建SolrQuery对象
        SolrQuery solrQuery=new SolrQuery();
        //3.设置查询条件、过滤条件，分页条件、排序条件、高亮等
        //设置查询条件
        solrQuery.setQuery("手机");
        //设置分页
        solrQuery.setStart(0);
        solrQuery.setRows(10);
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        //设置默认查询域
        solrQuery.set("df","item_title");
        //执行查询，得到一个Response对象
        QueryResponse response = solrServer.query(solrQuery);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //取查询结果总记录数
        long totalNum = solrDocumentList.getNumFound();
        //4.循环遍历
        String title="";
        for (SolrDocument document:solrDocumentList){
            System.out.println(document.get("id"));
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> stringList = highlighting.get(document.get("id")).get("item_title");

            if (stringList!=null&&stringList.size()>0){
                title=stringList.get(0);
            }else {
                title= (String) document.get("item_title");
            }
            System.out.println(title);
            System.out.println(document.get("item_sell_point"));
            System.out.println(document.get("item_price"));
            System.out.println(document.get("item_image"));
            System.out.println(document.get("item_category_name"));
            System.out.println(document.get("item_desc"));
            System.out.println("=======================---------------------++++++++++++++++++++++");
        }
        //取高亮显示
    }
}
