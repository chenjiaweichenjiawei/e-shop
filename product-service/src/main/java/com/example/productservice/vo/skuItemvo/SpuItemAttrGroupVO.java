package com.example.productservice.vo.skuItemvo;

import com.example.productservice.vo.spusavevo.Attr;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SpuItemAttrGroupVO {
    private String groupName;
    private List<Attr> attrValues;
}
