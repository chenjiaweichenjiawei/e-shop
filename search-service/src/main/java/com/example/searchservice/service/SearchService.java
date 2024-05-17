package com.example.searchservice.service;

import com.example.searchservice.vo.SearchParam;
import com.example.searchservice.vo.SearchResult;

/**
 * @author CJW
 * @since 2024/5/13
 */
public interface SearchService {
    /**
     * 检索商品
     *
     * @return
     */
    SearchResult searchProduct(SearchParam searchParam);
}
