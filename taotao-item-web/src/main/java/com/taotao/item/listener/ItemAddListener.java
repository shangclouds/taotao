package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @program:taotao
 * @description:商品添加监听器
 * @author:xjw
 * @create:2018-02-28 23:40
 **/
public class ItemAddListener implements MessageListener {

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;
    @Override
    public void onMessage(Message message) {
        try {
            //获取消息中商品的id
            TextMessage textMessage= (TextMessage) message;
            String strId = textMessage.getText();

            long itemId = Long.parseLong(strId);
            //根据商品id获取商品的基本信息和详细信息
            //延迟一秒，预防数据未加入到数据库
            Thread.sleep(1000);
            TbItem tbItem = itemService.selectItemById(itemId);
            Item item=new Item(tbItem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //1.创建模板
            //2.创建configuration对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //3利用configuration对象获取模板
            Template template = configuration.getTemplate("item.ftl");
            //4.创建数据
            Map data=new HashMap();
            data.put("item",item);
            data.put("itemDesc",itemDesc);
            //5.创建输出源
            Writer out=new FileWriter(HTML_OUT_PATH+strId+".html");
            //6.生成静态页面
            template.process(data,out);
            //7.关闭资源
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
