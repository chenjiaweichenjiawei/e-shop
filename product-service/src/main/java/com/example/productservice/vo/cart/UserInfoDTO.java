package com.example.productservice.vo.cart;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserInfoDTO {

    private Long userId;

    private String userKey;

    private boolean tempUser = false;
}
