package com.example.orderservice.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MqMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messageId;

    /**
     * JSON
     */
    private String content;

    private String toExchange;

    private String classType;

    /**
     * 0-新建 1-已发送 2-错误抵达 3-已抵达
     */
    private Integer messageStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
