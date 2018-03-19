package com.tatao.test;

import com.taotao.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class TestFastDFS {

    public void uploadFile()throws Exception{
        //1,向工厂中添加jar包
        //2.创建一个配置文件，配置tracker服务器地址
        //3、加载配置文件
       ClientGlobal.init("D:/shangyun/taotao/taotao-manager-web/src/main/resources/resource/FastDFSClient.conf");
        //4.创建一个TrackerClient对象
        TrackerClient trackerClient=new TrackerClient();
        //5.由一个TrackerClient对象获取trackerserver对象
        TrackerServer trackerServer=trackerClient.getConnection();
        //6.创建一个storageServer的引用，null即可
        StorageServer storageServer=null;
        //7.创建一个stoageclient对象,trackerServer和storageserver两个参数
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
        //8、利用storageClient来上传文件
        String[] string = storageClient.upload_appender_file("C://mp21797287_1436323836502_3.jpg", "jpg", null);

        for (String s:string){
            System.out.println(s);
        }
    }

    public void testFast()throws Exception{
        FastDFSClient fastDFSClient=new FastDFSClient("D:/shangyun/taotao/taotao-manager-web/src/main/resources/resource/FastDFSClient.conf");
        fastDFSClient.uploadFile("C://mp21797287_1436323836502_3.jpg");
    }
}
