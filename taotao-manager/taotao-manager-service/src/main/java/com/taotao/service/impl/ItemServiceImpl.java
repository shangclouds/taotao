package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyuiDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "itemAddTopic")
    private Destination destination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${TIME_EXPIRE}")
    private int TIME_EXPIRE;

    @Override
    public TbItem selectItemById(Long itemId) {
        //从缓存中查询数据，若缓存中不存在，则从数据库中查询获取
        try{
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":base");
            if (StringUtils.isNotBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //从数据库中查询数据
        TbItem item=tbItemMapper.selectByPrimaryKey(itemId);
        //将从数据库查询的数据添加到缓存中
        try {
            jedisClient.set(ITEM_INFO+":"+itemId+":base", JsonUtils.objectToJson(item));
            //设置缓存过期时间
            jedisClient.expire(ITEM_INFO+":"+itemId+":base",TIME_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public EasyuiDatagridResult selectAllItem(int page, int rows) {
        //1.在mybatis核心配置文件中配置分页插件
        //2.在myBatista执行之前，利用PageHelper来设置分页信息
        PageHelper.startPage(page,rows);
        //3.执行查询语句
        TbItemExample example=new TbItemExample();
        List<TbItem> list=tbItemMapper.selectByExample(example);
        //4.获取分页信息，利用pageInfo对象实现
        PageInfo pageInfo=new PageInfo(list);
        Long total=pageInfo.getTotal();
        EasyuiDatagridResult easyuiDatagridResult=new EasyuiDatagridResult();
        easyuiDatagridResult.setTotal(total);
        easyuiDatagridResult.setRows(list);
        return easyuiDatagridResult;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //1.生成商品的id
         final  long itemId=IDUtils.genItemId();//内部类调用的对象需要用final修饰
        //2.补全商品的属性
        item.setId(itemId);
        //1.正常、2.下架、3.删除
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //3.向商品表插入数据
        tbItemMapper.insert(item);
        //4.创建一个商品描述表对应的pojo
        TbItemDesc tbItemDesc=new TbItemDesc();
        //5.补全商品描述表的属性
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //6.向商品描述表插入数据
        itemDescMapper.insert(tbItemDesc);

        //向activemq发布消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //将添加的id发送出去
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });

        //7.返回结果
        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {

        //从缓存中查询数据，若缓存中不存在，则从数据库中查询获取
        try{
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)){
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return itemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //从数据库中查询数据
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //将从数据库查询的数据添加到缓存中
        try {
            jedisClient.set(ITEM_INFO+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
            //设置缓存过期时间
            jedisClient.expire(ITEM_INFO+":"+itemId+":DESC",TIME_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemDesc;
    }
}
