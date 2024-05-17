package com.example.wareservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class MergeVo {

    private Long purchaseId;

    private List<Long> items;
}
