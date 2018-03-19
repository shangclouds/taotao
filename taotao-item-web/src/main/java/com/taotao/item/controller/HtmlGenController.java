package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @program:taotao
 * @description:生成静态页面
 * @author:xjw
 * @create:2018-02-28 17:31
 **/
@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @RequestMapping("/genHtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //获取configuration对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //获取模板
        Template template = configuration.getTemplate("hello.ftl");
        //添加数据集
        Map data=new HashMap();
        data.put("hello","freemarker spring");
        //建立输出流
        Writer out=new FileWriter("D:/shangyun/out/html/test.txt");
        template.process(data,out);
        return "ok";
    }
}
