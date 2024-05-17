package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.SpuImages;

import java.util.List;

/**
 * <p>
 * spu图片 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SpuImagesService extends IService<SpuImages> {
    /**
     * 保存spu图片信息
     *
     * @param id
     * @param images
     */
    void saveImages(Long id, List<String> images);
}
