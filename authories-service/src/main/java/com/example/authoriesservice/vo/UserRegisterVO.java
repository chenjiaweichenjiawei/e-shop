package com.example.authoriesservice.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author CJW
 * @since 2023/12/1
 */
@Data
@Component
public class UserRegisterVO {
    private String userName;
    private String password;
    private String phone;
    private String code;
}
