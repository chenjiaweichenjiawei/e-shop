package com.example.common.util;

import com.example.common.constant.StatusCode;
import lombok.Data;

/**
 * 统一响应结果类
 *
 * @author CJW
 * @since 2023/9/19
 */
@Data
public class Result<R> {
    /**
     * 响应状态码
     */
    private String code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 响应携带的数据
     */
    private R data;

    /**
     * 成功响应快捷方法
     *
     * @param object 携带的数据对象
     * @param <T>
     * @return 一个成功的响应
     */
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.code = StatusCode.SUCCESS.getCode();
        result.data = object;
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = StatusCode.SUCCESS.getCode();
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = StatusCode.ERROR.getCode();
        result.message = message;
        return result;
    }

    public static <T> Result<T> error(T object) {
        Result<T> result = new Result<T>();
        result.code = StatusCode.ERROR.getCode();
        result.data = object;
        return result;
    }
}
