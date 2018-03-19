package com.taotao.pageHelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelp {
    @Test
    public void testPageHelper(){
        //1.在mybatis核心配置文件中配置分页插件
        //2.在执行查询之前配置分页信息，利用pageHelper的静态方法进行配置
        PageHelper.startPage(1,10);
        //3.执行查询
        ApplicationContext context=new ClassPathXmlApplicationContext("/spring/applicationContext-dao.xml");

        TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
        TbItemExample example=new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //4.获取分页信息,使用PageInfo对象去
        PageInfo pageInfo=new PageInfo(list);
       Long total= pageInfo.getTotal();
       System.out.println(total);
    }

}
