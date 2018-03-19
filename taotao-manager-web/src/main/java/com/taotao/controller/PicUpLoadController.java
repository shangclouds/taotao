package com.taotao.controller;

import com.taotao.common.utils.FastDFSClient;
import com.taotao.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PicUpLoadController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpLoad(MultipartFile uploadFile){//uploadFile指定上传文件参数名称

        Map result=new HashMap();
        try {
            //1.接收上传的内容
            //2.获取上传文件的扩展名
            String orginalFilename=uploadFile.getOriginalFilename();
            String extName=orginalFilename.substring(orginalFilename.lastIndexOf(".")+1);
            //3.创建一个FastDFS的客户端
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:resource/FastDFSClient.conf");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //4.拼接返回的url和ip地址，拼装成完整的url
           url=IMAGE_SERVER_URL+url;


            //5、返回map
            result.put("error",0);
            result.put("url",url);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("error",1);
            result.put("message","图片上传失败");
        }
        return JsonUtils.objectToJson(result);
    }
}
