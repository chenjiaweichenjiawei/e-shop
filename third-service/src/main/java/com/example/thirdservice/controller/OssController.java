package com.example.thirdservice.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.example.common.util.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CJW
 * @since 2023/11/16
 */
@RestController
@RequestMapping("/oss")
public class OssController {
    @Resource
    private OSSClient ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @RequestMapping("/policy")
    public Result<Map<String, String>> getPolicy() {
        String host = "https://" + bucket + "." + endpoint;
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String format = new SimpleDateFormat("yyyy:MM:dd").format(new Date());
        String dir = format + "/";
        //设置过期时间
        long expireTime = 6000;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);

        PolicyConditions policyConditions = new PolicyConditions();
        //设置文件的大小限制，单位为字节
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        //限制上传的文件前缀
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        //获取policy并Base64编码
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        //获取签名
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> policyMap = new HashMap<>(6);
        policyMap.put("accessId", accessId);
        policyMap.put("policy", encodedPolicy);
        policyMap.put("signature", postSignature);
        policyMap.put("dir", dir);
        policyMap.put("host", host);

        return Result.success(policyMap);
    }
}
