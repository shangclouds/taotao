package com.taotao.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @program:taotao
 * @description:freemarker测试
 * @author:xjw
 * @create:2018-02-28 10:32
 **/
public class TestFreeMarker {
    @Test
    public void testFreemarker() throws  Exception{
        //1.创建模板
        //2.创建一个configuration对象
        Configuration configuration=new Configuration(Configuration.getVersion());
        //3.设置模板所在的路劲
        configuration.setDirectoryForTemplateLoading(new File("D:/shangyun/taotao/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
        //4.设置字符集
        configuration.setDefaultEncoding("utf-8");
        //5.使用configuration来加载模板文件，需指定模板文件的文件名
        Template template = configuration.getTemplate("hello.ftl");
        //6.创建一个数据集，可以是pojo，也可以是map,推荐使用map
        Map data=new HashMap();
        data.put("hello","hello freemarker");
        //7.创建一个writer对象，需指定输出文件的路劲和文件名
        Writer writer=new FileWriter("D:/shangyun/out/html/hello.txt");
        //8.使用模板对象的process方法输出文件
        template.process(data,writer);
        //9.关闭流
        writer.close();
    }

}
