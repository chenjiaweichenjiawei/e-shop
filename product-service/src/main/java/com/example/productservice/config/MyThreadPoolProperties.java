package com.example.productservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author CJW
 * @since 2024/5/14
 */
@Data
@ConfigurationProperties(prefix = "my.thread")
public class MyThreadPoolProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
