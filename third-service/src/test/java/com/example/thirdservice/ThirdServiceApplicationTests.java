package com.example.thirdservice;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class ThirdServiceApplicationTests {
    @Resource
    private OSSClient ossClient;

    @Test
    void contextLoads() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File("C:\\Users\\CJW\\Pictures\\微信图片_20231116141020.jpg"));
        ossClient.putObject("cjw-cloud-oss", "test.jpg", inputStream);
        System.out.println("发送成功");
    }
}
