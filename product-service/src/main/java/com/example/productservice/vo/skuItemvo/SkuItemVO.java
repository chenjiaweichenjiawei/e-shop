package com.example.productservice.vo.skuItemvo;

import com.example.productservice.po.SkuImages;
import com.example.productservice.po.SkuInfo;
import com.example.productservice.po.SpuInfoDesc;
import com.example.productservice.vo.spusavevo.SeckillInfoVO;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVO {
    //1.sku基本信息获取 pms_sku_info
    SkuInfo info;

    boolean hasStock = true;
    //2.sku图片信息 pms_sku_images
    List<SkuImages> images;

    //3.spu的销售属性组合
    List<SkuItemSaleAttrsVO> saleAttr;

    //4.spu的详细介绍
    SpuInfoDesc desp;

    //5.规格参数
    List<SpuItemAttrGroupVO> groupAttrs;

    //6.当前商品的秒杀优惠信息
    SeckillInfoVO seckillInfo;

}
