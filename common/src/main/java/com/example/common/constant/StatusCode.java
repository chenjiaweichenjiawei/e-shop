package com.example.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举类
 *
 * @author CJW
 * @since 2023/11/9
 */
@AllArgsConstructor
@Getter
public enum StatusCode {
    /**
     * 成功
     */
    SUCCESS("00000", "一切正常"),
    /**
     * 失败
     */
    ERROR("11111", "错误");
    /**
     * 状态码
     */
    private final String code;
    /**
     * 状态提示信息
     */
    private final String message;
}
