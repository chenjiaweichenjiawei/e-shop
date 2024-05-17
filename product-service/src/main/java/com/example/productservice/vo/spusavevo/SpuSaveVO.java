
package com.example.productservice.vo.spusavevo;

import com.example.productservice.validation.AddSpu;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SpuSaveVO {

    @NotEmpty(groups = {AddSpu.class})
    private String spuName;
    private String spuDescription;
    @NotEmpty(groups = {AddSpu.class})
    private Long catalogId;
    @NotEmpty(groups = {AddSpu.class})
    private Long brandId;
    private double weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    @NotEmpty(groups = {AddSpu.class})
    private List<BaseAttrs> baseAttrs;
    @NotEmpty(groups = {AddSpu.class})
    private List<Skus> skus;

}