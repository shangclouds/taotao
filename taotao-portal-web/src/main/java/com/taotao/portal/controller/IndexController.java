package com.taotao.portal.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Value("${AD1_CATEGORY_ID}")
    private Integer AD1_CATEGORY_ID;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;

    @Autowired
    private ContentService contentService;
    @RequestMapping("/index")
    public String showIndex(Model model){
        //根据cid将查询广告位数据
        List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
        //将查询的数据转换成AD1Node
        List<AD1Node> ad1Nodes=new ArrayList<>();
        for (TbContent content:contentList){
            AD1Node ad1Node=new AD1Node();
            ad1Node.setAlt(content.getTitle());
            ad1Node.setSrc(content.getPic());
            ad1Node.setSrc(content.getPic2());
            ad1Node.setHref(content.getUrl());

            ad1Node.setHeight(AD1_HEIGHT);
            ad1Node.setHeightB(AD1_HEIGHT_B);
            ad1Node.setWidth(AD1_WIDTH);
            ad1Node.setWidthB(AD1_WIDTH_B);
        ad1Nodes.add(ad1Node);
        }
        //将转换后的数据转换成Json数据
        String s = JsonUtils.objectToJson(ad1Nodes);
        System.out.println(ad1Nodes.size()+"是否查询出数据");
        //将json数据返回到首页
        model.addAttribute("ad1",s);
        return "index";
    }

}
